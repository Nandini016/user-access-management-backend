package com.user.management.service;

import com.user.management.dto.DocumentMetadata;
import com.user.management.model.Document;
import com.user.management.model.User;
import com.user.management.repository.CompanyRepository;
import com.user.management.repository.DocumentRepository;
import com.user.management.repository.UserRepository; // <-- replace this line if your package differs
import com.user.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DocumentService - local filesystem storage and role-based authorization.
 *
 * Requirements:
 *  - USER: can manage documents only for their own company
 *  - SUPERADMIN / ADMIN: can manage documents for any existing company
 */
@Service
public class DocumentService {

    private final DocumentRepository docRepo;
    private final CompanyRepository companyRepo;
    private final UserRepository userRepo;
    private final Path storageRoot;

    // Allowed types and max size (tweak as needed)
    private final List<String> allowedTypes = List.of(
            "application/pdf",
            "image/png",
            "image/jpeg",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-excel"
    );
    private final long maxSizeBytes = 10L * 1024 * 1024; // 10 MB

    public DocumentService(DocumentRepository docRepo,
                           CompanyRepository companyRepo,
                           UserRepository userRepo,
                           @Value("${app.documents.storage-root:./data/documents}") String storageRoot) {
        this.docRepo = docRepo;
        this.companyRepo = companyRepo;
        this.userRepo = userRepo;
        this.storageRoot = Paths.get(storageRoot).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.storageRoot);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create storage root: " + this.storageRoot, e);
        }
    }

    // ---------- helper: current user ----------
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    // ---------- helper: check if user is super admin ----------
    private boolean isSuperAdmin(User user) {
        if (user.getRole() == null || user.getRole().getName() == null) return false;
        String rn = user.getRole().getName().name();
        return rn.equals("SUPER_ADMIN") ;
    }

    // ---------- upload ----------
    public Document upload(Long companyId, MultipartFile file, String description) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required");
        }

        if (!companyRepo.existsById(companyId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found");
        }

        User current = getCurrentUser();

        // Authorization check: user must be superadmin OR belong to the company
        if (!isSuperAdmin(current) && (current.getCompany() == null || !companyId.equals(current.getCompany().getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to upload for this company");
        }

        if (file.getSize() > maxSizeBytes) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File too large. Max allowed = " + maxSizeBytes);
        }

        String contentType = Optional.ofNullable(file.getContentType()).orElse("application/octet-stream");
        if (!allowedTypes.contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File type not allowed: " + contentType);
        }

        String orig = Optional.ofNullable(file.getOriginalFilename()).orElse("file");
        String ext = orig.contains(".") ? orig.substring(orig.lastIndexOf(".")) : "";
        String storageName = UUID.randomUUID().toString() + ext;

        Path companyDir = storageRoot.resolve(String.valueOf(companyId));
        try {
            Files.createDirectories(companyDir);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create company directory", e);
        }

        Path target = companyDir.resolve(storageName);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file", e);
        }

        Document doc = new Document();
        doc.setFilename(orig);
        doc.setStorageName(storageName);
        doc.setContentType(contentType);
        doc.setSize(file.getSize());
        doc.setCompanyId(companyId);
        doc.setUploadedByUserId(current.getId());
        doc.setUploadedAt(Instant.now());
        doc.setDescription(description);
        doc.setPath(target.toString());

        return docRepo.save(doc);
    }

    // ---------- list ----------
    public List<DocumentMetadata> list(Long companyId) {
        User current = getCurrentUser();

        if (!companyRepo.existsById(companyId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found");
        }

        if (!isSuperAdmin(current) && (current.getCompany() == null || !companyId.equals(current.getCompany().getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return docRepo.findByCompanyId(companyId)
                .stream()
                .map(d -> new DocumentMetadata(
                        d.getId(),
                        d.getFilename(),
                        d.getContentType(),
                        d.getSize(),
                        d.getUploadedAt(),
                        d.getUploadedByUserId()
                ))
                .collect(Collectors.toList());
    }

    // ---------- get file path for download ----------
    public Path getFilePath(Long companyId, Long docId) {
        Document d = docRepo.findById(docId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        User current = getCurrentUser();

        if (!isSuperAdmin(current) && (current.getCompany() == null || !companyId.equals(current.getCompany().getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        if (!d.getCompanyId().equals(companyId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document does not belong to company");
        }

        Path p = Paths.get(d.getPath());
        if (!Files.exists(p)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stored file not found");
        }
        return p;
    }

    // ---------- return Document entity ----------
    public Document getDocumentEntity(Long id) {
        return docRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }

    // ---------- delete ----------
    public void delete(Long companyId, Long docId) {
        Document d = docRepo.findById(docId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        User current = getCurrentUser();

        if (!isSuperAdmin(current) && (current.getCompany() == null || !companyId.equals(current.getCompany().getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to delete this document");
        }

        if (!d.getCompanyId().equals(companyId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document does not belong to company");
        }

        // remove DB record first
        docRepo.delete(d);

        // then remove file (best-effort)
        try {
            Files.deleteIfExists(Paths.get(d.getPath()));
        } catch (IOException ignored) {
            // optionally log here. file cleanup can be retried by admin later.
        }
    }
}

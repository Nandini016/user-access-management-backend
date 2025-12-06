package com.user.management.controller;

import com.user.management.dto.DocumentMetadata;
import com.user.management.model.Document;
import com.user.management.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies/{companyId}/documents")
public class DocumentController {

    private final DocumentService docService;

    public DocumentController(DocumentService docService)
    { this.docService = docService; }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@PathVariable Long companyId,
                                    @RequestPart("file") MultipartFile file,
                                    @RequestPart(required = false) String description) throws IOException {
        Document saved = docService.upload(companyId, file, description);
        return ResponseEntity.ok(Map.of("id", saved.getId()));
    }

    @GetMapping
    public List<DocumentMetadata> list(@PathVariable Long companyId) {
        return docService.list(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long companyId, @PathVariable Long id) throws IOException {
        Path path = docService.getFilePath(companyId, id);
        Resource resource = new UrlResource(path.toUri());
        Document doc = docService.getDocumentEntity(id); // small helper to fetch doc for metadata

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long companyId, @PathVariable Long id) throws IOException {
        docService.delete(companyId, id);
        return ResponseEntity.noContent().build();
    }
}


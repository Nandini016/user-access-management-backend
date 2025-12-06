# 📦 DocumentManagement API – Test Results

## 🔄 Endpoint: Upload Document

### ✅ Request Details

- **Type**: POST
- **URL**: `http://localhost:8080/api/companies/1/documents`
- **Request Name**: Upload Document

### 📤 Request Body (JSON)
```json
{
  "Key": "file",
  "Type": "File",
  "description": "document as pdf"
}

```

### 📤 Response Body (JSON)
```json

{
  "id": 52
}

```
- **Response Status**: 200 OK
----
## 🔄 Endpoint: List Documents

### ✅ Request Details

- **Type**: GET
- **URL**: `http://localhost:8080/api/companies/1/documents`
- **Request Name**: List Document

### 📤 Request Body (JSON)
```json

```
### 📤 Response Body (JSON)
```json
[
  {
    "id": 1,
    "filename": "Railway_Traveller_Complete_Integrated.pdf",
    "contentType": "application/pdf",
    "size": 7903,
    "uploadedAt": "2025-12-06T14:15:27.335400Z",
    "uploadedBy": 4
  }
]
```
- **Response Status**: 200 OK
----
## 🔄 Endpoint: Download Document

### ✅ Request Details

- **Type**: GET
- **URL**: `http://localhost:8080/api/companies/1/documents/1`
- **Request Name**: Download Document
  ### 📤 Response Body (JSON)
```json

```
- **Response Status**: 200 OK
- 
## 🔄 Endpoint: Delete Document

### ✅ Request Details

- **Type**: DELETE
- **URL**: `http://localhost:8080/api/companies/{companyId}/documents/{id}`
- **Request Name**: Delete Document
  ### 📤 Response Body (JSON)
```json

```
- **Response Status**: 204 No Content
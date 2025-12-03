# 🏢 SuperAdmin – Company Management API Documentation

## 🔄 Endpoint:  Get All Companies

### ✅ Request Details

- **Type**: GET
- **URL**: `http://localhost:8080/admin/companies`
- **Request Name**: Get All Companies


```

### 📤 Response Body (JSON)
```json
[
  {
    "id": 1,
    "name": "Main Company"
  }
]
```
- **Response Status**: 200 OK
----
## 🔄 Endpoint:Create Company

### ✅ Request Details

- **Type**: POST
- **URL**: `http://localhost:8080/admin/companies`
- **Request Name**: Create Company

### 📤 Request Body (JSON)
```json
{
  "name": "GlobalTech Solutions"
}
```
### 📤 Response Body (JSON)
```json
{
  "id": 2,
  "name": "GlobalTech Solutions"
}
```
- **Response Status**: 200 OK
----
## 🔄 Endpoint: Update Company

### ✅ Request Details

- **Type**: PUT
- **URL**: http://localhost:8080/admin/companies/1
- **Request Name**: Update Company
  ### 📤 Response Body (JSON)
```json
{
  "name": "Updated Company Name"
}
```
### 📤 Response Body (JSON)
```json

{
  "id": 1,
  "name": "Updated Company Name"
}

```

- **Response Status**: 200 OK
- ## 🔄 Endpoint: Delete Company

### ✅ Request Details

- **Type**: DELETE
- **URL**:http://localhost:8080/admin/companies/{id}
- **Request Name**: Delete Company



```

- **Response Status**: 204 No Content
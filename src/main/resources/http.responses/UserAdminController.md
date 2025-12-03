# 🛠️ Admin User Management API – Documentation

## 🔄 Endpoint: Get All Users

### ✅ Request Details
- **Type**: GET
- **URL**: `http://localhost:8080/admin/users`
- **Request Name**: Admin – Get All Users

### 📤 Response Body (JSON)
```json
[
    {
        "id": 2,
        "email": "admin@example.com",
        "password": "$2a$10$jxwnHnNfkAovUnn3OdfIiOQ7X3BvLgGuGjKS1HhWie9tTy6yWtfG.",
        "role": {
            "id": 1,
            "name": "SUPER_ADMIN"
        },
        "company": {
            "id": 1,
            "name": "Main Company"
        },
        "approved": true
    },
    {
        "id": 1,
        "email": "user1@client.com",
        "password": "$2a$10$dI77Yv6BfYNyocQ2U3TQKe06Kh/B5/ZFbe9wpys8l6sKuJXOxo.vO",
        "role": {
            "id": 3,
            "name": "USER"
        },
        "company": {
            "id": 1,
            "name": "Main Company"
        },
        "approved": true
    },
    {
        "id": 4,
        "email": "john@example.com",
        "password": "$2a$10$DVoqq75rVwSPEkL1nRsuDuxmSHZnTUtVbegWKtr1/wlQ8Px18bURa",
        "role": {
            "id": 3,
            "name": "USER"
        },
        "company": {
            "id": 1,
            "name": "Main Company"
        },
        "approved": true
    }
]
```
- **Response Status**: 200 OK
----
## 🔄 Endpoint: Create Users

### ✅ Request Details
- **Type**: POST
- **URL**: `http://localhost:8080/admin/users`
- **Request Name**: Admin – Create Users
### 📤 Request Body (JSON)
```json
{
  "email": "newuser@example.com",
  "password": "Password@123",
  "companyId": 2
}


```
### 📤 Response Body (JSON)
```json
{
  "id": 5,
  "email": "newuser@example.com",
  "password": "$2a$10$7inKAXMJa4hSTf1VcLt7MuKUaGaqm9PR0Y8z7Q37KaBavYOMtRGOG",
  "role": {
    "id": 3,
    "name": "USER"
  },
  "company": {
    "id": 2,
    "name": "GlobalTech Solutions"
  },
  "approved": true
}
```
- **Response Status**: 200 OK
----
## 🔄 Endpoint: Update Users

### ✅ Request Details
- **Type**: PUT
- **URL**: `http://localhost:8080/admin/users/{id}`
- **Request Name**: Admin – Update Users
### 📤 Request Body (JSON)
```json
{

}
```
### 📤 Response Body (JSON)
```json
{


}
```
- **Response Status**: 200 OK
## 🔄 Endpoint: Get Pending Users

### ✅ Request Details
- **Type**: GET
- **URL**: `http://localhost:8080/admin/users/pending`
- **Request Name**: Admin – Get Pending Users


### 📤 Response Body (JSON)
```json
[
  {
    "id": 4,
    "email": "john@example.com",
    "password": "$2a$10$DVoqq75rVwSPEkL1nRsuDuxmSHZnTUtVbegWKtr1/wlQ8Px18bURa",
    "role": {
      "id": 3,
      "name": "USER"
    },
    "company": {
      "id": 1,
      "name": "Main Company"
    },
    "approved": false
  }
]
```
- **Response Status**: 200 OK
- ## 🔄 Endpoint: Approve User

### ✅ Request Details
- **Type**: PUT
- **URL**: `http://localhost:8080/admin/users/4/approve`
- **Request Name**: Admin – Get Pending Users
### 📤 Response Body (JSON)
```json
{
    "id": 4,
    "email": "john@example.com",
    "password": "$2a$10$DVoqq75rVwSPEkL1nRsuDuxmSHZnTUtVbegWKtr1/wlQ8Px18bURa",
    "role": {
        "id": 3,
        "name": "USER"
    },
    "company": {
        "id": 1,
        "name": "Main Company"
    },
    "approved": true
}
```
## 🔄 Endpoint: Delete User

### ✅ Request Details

- **Type**: DELETE
- **URL**: http://localhost:8080/admin/users/{id}
- **Request Name**: Delete User
- ### 📤 Request Body (JSON)
```json
{

}

```

### 📤 Response Body (JSON)

- **Response Status**: 200 OK
----

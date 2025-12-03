# 📦 UserManagement API – Test Results

## 🔄 Endpoint: Register User

### ✅ Request Details

- **Type**: POST
- **URL**: `http://localhost:8080/api/users/register`
- **Request Name**: Register User

### 📤 Request Body (JSON)
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "Password@123",
  "companyId": 1
}

```

### 📤 Response Body (JSON)
```json
{
  "id": 4,
  "email": "john@example.com",
  "role": {
    "id": 3,
    "name": "USER"
  },
  "company": {
    "id": 1,
    "name": "Main Company"
  }
}
```
- **Response Status**: 200 OK
----
## 🔄 Endpoint: Login

### ✅ Request Details

- **Type**: POST
- **URL**: `http://localhost:8080/api/users/login`
- **Request Name**: Login

### 📤 Request Body (JSON)
```json
{
  "email": "john@example.com",
  "password": "Password@123"
}
```
### 📤 Response Body (JSON)
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjQsInJvbGUiOiJVU0VSIiwiY29tcGFueU5hbWUiOiJNYWluIENvbXBhbnkiLCJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzY0NzY5NjY4LCJleHAiOjE3NjQ4NTYwNjh9.NAxSVBjGETA-JHp6TMNYG99b2mkGhUB5tXkgJp2Oozs",
  "role": "USER"
}
```
- **Response Status**: 200 OK
----
## 🔄 Endpoint: Get all users

### ✅ Request Details

- **Type**: GET
- **URL**: `http://localhost:8080/api/users`
- **Request Name**: Get Timeline
  ### 📤 Response Body (JSON)
```json
[
  {
    "id": 2,
    "email": "admin@example.com",
    "role": {
      "id": 1,
      "name": "SUPER_ADMIN"
    },
    "company": {
      "id": 1,
      "name": "Main Company"
    }
  },
  {
    "id": 1,
    "email": "user1@client.com",
    "role": {
      "id": 3,
      "name": "USER"
    },
    "company": {
      "id": 1,
      "name": "Main Company"
    }
  },
  {
    "id": 4,
    "email": "john@example.com",
    "role": {
      "id": 3,
      "name": "USER"
    },
    "company": {
      "id": 1,
      "name": "Main Company"
    }
  }
]
```
- **Response Status**: 200 OK
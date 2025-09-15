# Bookstore Backend

## Table of Contents

- [About](#about)
- [API Endpoints (CRUD)](#api-endpoints-crud)
    - [Books](#books)
        - [Bulk CSV Upload Pipeline](#bulk-csv-upload-pipeline)
    - [Authors](#authors)
    - [Publishers](#publishers)
    - [File Uploads (covers)](#file-uploads-covers)
- [Security (Auth & Users, Roles & Permissions)](#securityauth-users-roles--permissions)
- [Report Generation](#report-generation)
- [Testing](#testing)
- [License & Contact](#license--contact)

## About

> - Production-ready REST backend for an online bookstore built with **Java & Spring Boot**.  
> 
> - Implements JWT authentication, role and permission-based access (MANAGER/STAFF/USER), full CRUD for books/authors/publishers..., CSV bulk import for books, cover image uploads, and more.  
> 
> - Designed and implemented as part of an internship, demonstrating enterprise-level backend development.

---

## API Endpoints (CRUD)

This section documents the main REST endpoints for the application. All endpoints accept and return JSON unless otherwise noted. Protected endpoints require `Authorization: Bearer <JWT>`.

### Books
- `GET /books`
    - Description: List books with pagination, sorting and filters.
    - Query params: `page`, `size`, `sort`, `q` (search), `category`, `authorId`, `minPrice`, `maxPrice`
    - Response: paginated list `{ content: [...], page: ..., totalPages: ... }`

- `GET /books/{id}`
    - Description: Get book details by ID.

- `POST /books`
    - **Description:** Create a new book record with full metadata (IDs for related entities are used to link authors, genres, publisher, etc.).
    - **Content-Type:** `application/json`
    - **Required fields:** `bookId`, `title` (validated with `@NotBlank` on the DTO). All other fields are optional but supported.
    - **Validation notes:**
        - `bookId` — unique string identifier for the book (client- or server-generated depending on your design).
        - `authorIds` and `authorRoles` — if both present, arrays should align (same length) so each `authorId` maps to the corresponding `authorRole`.
        - `price` — decimal value (e.g. `12.99`).
        - `publishDate` / `firstPublishDate` — ISO `YYYY-MM-DD`.
        - `coverImageURL` — URL string (optional).
    - **Example request body (rich metadata):**
      ```json
      {
        "bookId": "bk-001",
        "title": "The Fellowship of the Ring",
        "publishDate": "1954-07-29",
        "firstPublishDate": "1954-07-29",
        "description": "First volume of The Lord of the Rings.",
        "format": "HARDCOVER",
        "isbn": "978-0544003415",
        "languageId": 1,
        "publisherId": 2,
        "edition": "50th Anniversary Edition",
        "pageNumber": 423,
        "price": 29.99,
        "authorIds": [1],
        "authorRoles": ["PRIMARY"],
        "genreIds": [10, 12],
        "settingIds": [5, 6],
        "characterIds": [100, 101, 102],
        "awardIds": [2001],
        "seriesId": 7,
        "seriesNumber": "1",
        "coverImageURL": "https://example.com/covers/fellowship.jpg"
      }
      ```

#### Bulk CSV Upload Pipeline

### Authors

> pending

### Publishers

> pending


### File Uploads (covers)


### Standard Error Format
Errors return example:
```json
{
  "message": "Book with id [145455] was not found",
  "status": 404
}
```

## Security/auth users roles & permissions

> pending

## Report Generation

> pending

## Testing

This project is demonstrated via a curated Postman workspace — tidy folders with one-click requests for reviewers.

- **[Workspace](https://web.postman.co/workspace/3af5d935-55b6-4e3a-84a9-20af0be97205)**

- **Minimal testing flow**
    1. `Auth → Login` → copy `accessToken`.
    2. Paste token into collection-level Authorization: `Bearer {{accessToken}}`.
    3. Run `books → Search (get all) books` (public).
    4. Run `book-instances → Create book instance (POST)` (manager or staff, with appropriate permission, token required).
    5. Run `books → Bulk input` and attach `books.csv` to the `file` form field.

## License & Contact

© 2025 Harutyun Grigoryan — **All rights reserved.**

For questions about this project, email: [harut.grigoryan0405@gmail.com](mailto:harut.grigoryan0405@gmail.com)  
LinkedIn: [Harutyun Grigoryan](https://www.linkedin.com/in/harutyun-grigoryan7)

# Bookstore Backend

## Table of Contents

- [About](#about)
- [Security (Auth & Users, Roles & Permissions)](#securityauth-users-roles--permissions)
- [API Endpoints (CRUD)](#api-endpoints-crud)
    - [Books](#books)
        - [Bulk CSV Upload Pipeline](#bulk-csv-upload-pipeline)
    - [Authors](#authors)
    - [Book Instances](#book-instances)
    - [File Uploads (covers)](#file-uploads-covers)
    - [Publishers](#publishers)
    - [Payments](#payments)
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
## Security/auth users roles & permissions

- **Overview:** Authentication is implemented using JSON Web Tokens (JWT). Authorization is enforced at the controller level using Spring Security `@PreAuthorize` annotations and SpEL expressions. The security model uses a small role set plus fine-grained permission checks; ownership checks rely on values available on the authenticated `principal` (e.g., `principal.userId`).

- **How to authenticate requests**
    - Include the JWT in the `Authorization` header for protected endpoints:
      ```
      Authorization: Bearer <JWT>
      ```

- **Auth endpoints (login / refresh / register)**
    - `POST /auth/login` — authenticates a user using `LoginRequestDTO` and returns `LoginResponseDTO` (authentication result returned by `AuthenticationService.authenticate(...)`).
    - `POST /auth/refresh` — accepts `RefreshTokenRequestDTO` and returns `LoginResponseDTO` with refreshed credentials (handled by `AuthenticationService.refresh(...)`).
    - `POST /auth/register` — registers a new user (`UserRegistrationDTO`) and returns `UserRegistrationResponseDTO` (creates account via `AuthenticationService.register(...)`).
    - **Notes:** callers must supply valid request bodies; responses are DTOs implemented in the security layer and contain the authentication payload as implemented in the project.

- **Roles & permissions**
    - Roles used: `MANAGER`, `STAFF`, `USER`.
    - Permission strings used in code (checked via `hasPermission(...)`):  
      `ADD_BOOK_DATA`, `UPLOAD_CSV`, `ADD_INFORMATION`, `REMOVE_INFORMATION`, `REMOVE_BOOK`, `ADD_BOOK_INSTANCE`, `GENERATE_REPORT`, `CREATE_ACCOUNT`, `UPDATE_ACCOUNT`, `REMOVE_ACCOUNT`, plus others used in controller annotations.
    - Pattern: high-trust operations require `MANAGER` or a `STAFF` user with a specific permission; many read operations are more permissive.

- **Principal / ownership checks**
    - SpEL ownership checks appear in multiple controllers, for example:
        - `@PreAuthorize("hasRole('USER') AND #paymentRequestDTO.userId.equals(authentication.principal.userId)")` (create payment)
        - `@PreAuthorize("authentication.principal.userId.equals(@paymentService.getUserByPaymentId(#id).id) OR hasRole('STAFF')")` (get payment)
        - `@PreAuthorize("authentication.principal.userId == #id OR hasRole('STAFF')")` (get user by id)
    - These checks require the security principal to expose `userId` for ownership validation.

- **Examples of endpoint → security requirements (exactly from controller annotations)**
    - `POST /books` — `@PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_BOOK_DATA')")`
    - `PUT /books/{id}` — `@PreAuthorize("hasRole('STAFF')")`
    - `DELETE /books/{id}` — `@PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_BOOK')")`
    - `POST /books/csv` — `@PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'UPLOAD_CSV')")`
    - `GET /book-instances/{id}` — `@PreAuthorize("hasAnyRole('USER','STAFF')")`
    - `POST /book-instances` — `@PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_BOOK_INSTANCE')")`
    - `PUT /book-instances/{id}` — `@PreAuthorize("hasRole('STAFF')")`
    - `DELETE /book-instances/{id}` — `@PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_INFORMATION')")`
    - `GET /reports/sales` — `@PreAuthorize("hasPermission('ANY', 'GENERATE_REPORT')")`
    - `POST /payments` — `@PreAuthorize("hasRole('USER') AND #paymentRequestDTO.userId.equals(authentication.principal.userId)")`
    - `GET /payments/{id}` — owner or staff as shown above.

- **Public (no-auth) endpoints**
    - Some read endpoints are explicitly public (`@PermitAll`), for example:
        - `GET /books` (list)
        - `GET /books/{id}` (details)
        - `GET /books/{id}/cover-image`
        - `GET /publishers/{id}`

- **Failure modes**
    - Missing/invalid JWT → `401 Unauthorized`.
    - Valid JWT but insufficient role/permission → `403 Forbidden`.
    - Ownership SpEL mismatch → `403 Forbidden`.

## API Endpoints (CRUD)

This section documents the main REST endpoints for the application. All endpoints accept and return JSON unless otherwise noted. Protected endpoints require `Authorization: Bearer <JWT>`.

### Books
- `GET /books`
    - **Description:** List books with pagination, sorting and filters.
    - **Filters:** `page`, `size`, `sort`, `title`, `publisher`, `authorId`, `authorName`, `edition`...
    - **Response:** paginated list `{ content: [...], page: ..., totalPages: ... }`

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

- `PUT /books/{id}` *(MANAGER or STAFF role required)*
  - **Description:** Update an existing book. Accepts a `BookUpdateRequestDTO`.
  - **Content-Type:** `application/json`

- `DELETE /books/{id}`  *(MANAGER role or STAFF with REMOVE_INFORMATION permission required)*
  - **Description:** Delete a single book by numeric DB id.
  - **Response:** `204 No Content` on success.

- `DELETE /books` (batch)
  - **Description:** Delete multiple books in one request. Accepts JSON body `List<Long>` of book ids.
  - **Body example:**
    ```json
    [123, 124, 125]
    ```
    - **Authorization:** same as single-delete
    - **Response:** `204 No Content` on success.

    - `POST /books/{id}/review` and `POST /books/{id}/rate` *(USER role required)
    - **Description:** Extra endpoints for review and rating exist (see controller) — adhere to their respective authorization rules (`ADD_INFORMATION`, `USER` role for rating, etc.).

    #### Bulk CSV Upload Pipeline
    - `POST /books/csv` (CSV upload) *(MANAGER role or STAFF with UPLOAD_CSV permission required)*
    - **Content-Type:** `multipart/form-data` (field `file`)
    - **Short Description:** Upload CSV of books (multipart/form-data `file` field). Returns boolean success currently. 
      - The processing of the input file is done utilizing multithreading to achieve fast execution.
    - **Behavior:**
    - The uploaded CSV is written to a temporary file and parsed using Apache Commons CSV (header-based).
    - Existing entities (genres, authors, publishers, languages, series, awards, settings, characters) are loaded and used to avoid duplicates; new entities are prepared and saved via `EntityMapRegistry` / mappers.
    - `FileInformation` objects are created for each `coverImg` URL found in the CSV and saved via `fileInformationRepository`.
    - A thread pool is created with size = `Runtime.getRuntime().availableProcessors()` and records are processed in chunks of `CHUNK_SIZE` (2500). Processing tasks persist books and related associations in batched saves.
    - If `app.image.processing.enabled` is `true`, `BookCoverImage` records are created and saved; image processing is triggered asynchronously.
    - A sample bulk-import 'books.csv' file can be found in the repository.


### Authors

- `GET /authors`
    - **Description:** List authors with pagination and optional filters.
    - **Filters:** `page`, `size`, `fullName` (partial match), `isOnGoodreads` (`true`|`false`)
    - **Notes:** Paging is driven by `AuthorSearchCriteria` and results are sorted by `fullName` by default unless otherwise specified.
    - **Response:** paginated list `{ content: [...], page: ..., totalPages: ... }`

- `GET /authors/{id}`
    - **Description:** Get author details by ID. Includes basic author fields and read-only metadata about associated books (via `BookAuthor` relationships).

- `POST /authors` *(MANAGER role or STAFF with permission ADD_INFORMATION required)*
    - **Description:** Create a new author.
    - **Content-Type:** `application/json`
    - **Required fields:** `fullName`
    - **Validation notes:**
        - `fullName` must be unique (DB constraint). The API returns a `409 Conflict` if uniqueness is violated.
    - **Example request body:**
      ```json
      {
        "fullName": "J. R. R. Tolkien",
        "isOnGoodreads": true
      }
      ```

- `PUT /authors/{id}` *(MANAGER or STAFF role required)*
    - **Description:** Update author fields (e.g., rename, toggle `isOnGoodreads`). Partial updates allowed if `PATCH` is implemented.
    - **Body:** same shape as create DTO; `fullName` remains required if present.

- `DELETE /authors/{id}` *(MANAGER role or STAFF with REMOVE_INFORMATION permission required)*
    - **Description:** Remove an author (note, it is implemented as a hard delete).

### Book Instances

- `GET /book-instances/{id}` *(Any role required)*
    - **Description:** Return a single book instance by its numeric DB `id`.
    - **Authorization:** `USER` or `STAFF` (`@PreAuthorize("hasAnyRole('USER','STAFF')")`).
    - **Success (200 OK)** returns a `BookInstanceDTO`:
      ```json
      {
        "id": 42,
        "instanceNumber": 3,
        "isRentable": true,
        "isSellable": true,
        "maxRentCount": 5,
        "price": 9.99,
        "createdAt": "2025-09-14T12:34:56",
        "updatedAt": "2025-09-14T12:40:00",
        "status": "AVAILABLE",
        "bookId": 123
      }
      ```
    
- `GET /book-instances` *(STAFF or MANAGER role required)*
    - **Description:** List book instances (paged) using `BookInstanceSearchCriteria`.
    - **Authorization:** `STAFF` (`@PreAuthorize("hasRole('STAFF')")`).
    - **Query params (from `BookInstanceSearchCriteria`):**
        - `page`, `size` (paging)
        - `instanceNumber`
        - `isRentable` (`true|false`)
        - `isSellable` (`true|false`)
        - `maxRentCount`
        - `currentRentCount`
        - `maxInitialPrice`
        - `maxCurrentPrice`
        - `bookInstanceStatus` (enum)
        - `bookId`
        - `createdAt`, `updatedAt` (ISO datetime)
        - `sortBy` (default `"book"`, results sorted descending by this field)
    - **Response:** paginated `PageResponseDTO<BookInstanceDTO>` `{ content:[...], page:..., totalPages:... }`.

- `GET /book-instances/{id}/rental-cost` *(STAFF role required)*
    - **Description:** Compute rental cost for the specified instance. Optional date range may be supplied.
    - **Query params:**
        - `startDate` (optional, ISO `YYYY-MM-DD`)
        - `endDate` (optional, ISO `YYYY-MM-DD`)
    - **Authorization:** `USER` or `STAFF`.

- `GET /book-instances/{id}/sale-cost`
    - **Description:** Compute sale price for the specified instance, optionally considering a coupon code.
    - **Request params:**
        - `couponCode` (optional)
    - **Authorization:** `USER` or `STAFF`.

- `POST /book-instances` *(MANAGER role or STAFF with permission ADD_INFORMATION required)*
    - **Description:** Create a new book instance.
    - **Content-Type:** `application/json`
    - **Request DTO:** `BookInstanceRequestDTO` (validated with `@Valid`). Fields:
        - `copyNumber` (Integer, `@NotBlank`)
        - `isRentable` (Boolean, default `false`)
        - `isSellable` (Boolean, default `true`)
        - `maxRentCount` (Integer, default `0`)
        - `status` (enum `BookInstanceStatus`, default `AVAILABLE`)
        - `initialPrice` (BigDecimal, `@NotBlank`)
        - `bookId` (Long, `@NotBlank`)
    - **Success (201 Created):** returns created `BookInstanceDTO`.
    - **Example request body:**
      ```json
      {
        "copyNumber": 1,
        "isRentable": true,
        "isSellable": true,
        "maxRentCount": 3,
        "status": "AVAILABLE",
        "initialPrice": 9.99,
        "bookId": 123
      }
      ```


- `PUT /book-instances/{id}` *(MANAGER or STAFF role required)*
    - **Description:** Update an existing book instance by `id`. Accepts `BookInstanceRequestDTO` .
    - **Content-Type:** `application/json`
    
- `DELETE /book-instances/{id}` *(MANAGER role or STAFF with REMOVE_INFORMATION permission required)*
    - **Description:** Delete a book instance by `id` (note, it is implemented as a hard delete).

    
### File Uploads (covers)

- `GET /books/{id}/cover-image`
    - **Description:** Retrieve the stored cover image path (filesystem path) for a given book and requested `size`.
    - **Query params:** `size` — enum `PictureSize` (e.g. `THUMBNAIL`, `MEDIUM`, `ORIGINAL` where supported).
    - **Authorization:** Public (`@PermitAll` on controller method).
    - **Response:** `String` — full file path returned from repository (`FileInformation.filePath`).
  
- Image processing (service behavior — `CoverImageService`)
    - **Entry points:** `saveImages(List<BookCoverImage>, int from, int upTo)` and scheduled `downloadImages()` (`@Scheduled` uses `scheduler.daily.cron` and `scheduler.daily.zone` properties).
    - **Config / properties referenced in code:**
        - `app.image.root.path` — base filesystem path where images are stored.
        - `app.image.medium.size` — integer size used for medium thumbnails.
        - `app.image.thumbnail.size` — integer size used for small thumbnails.
        - `cover.images.count.per.process` — used by repository query that selects pending images (`findPendingTop`) and by the scheduled job.
        - `app.image.processing.enabled` — flag controlling whether CSV upload triggers image processing.
    - **Processing steps (from code):**
        - For each `BookCoverImage` entry, the service:
            - Derives a directory under `IMAGES_ROOT` based on the first (and second) letter(s) of the book title (lowercased) to organize files.
            - Normalizes `bookId` to `cleanBookId` (non-alphanumeric characters replaced with `-`) and writes the original file as `cleanBookId.jpg`.
            - Attempts to download the original image from `FileInformation.fileUrl` and sets `FileInformation.status` to `DOWNLOADING` / `FAILED` as appropriate.
            - Uses `net.coobird.thumbnailator.Thumbnails` to create:
                - Medium file: `cleanBookId-Medium.jpg` (size = `MEDIUM_SIZE`)
                - Thumbnail file: `cleanBookId-Thumbnail.jpg` (size = `THUMBNAIL_SIZE`)
            - Calls `completeFileInformation(...)` to set `fileName`, `filePath`, `fileFormat` and mark `status = COMPLETED`.
            - Saves `FileInformation` and `BookCoverImage` records via `fileInformationRepository.saveAll(...)` and `bookCoverImageRepository.saveAll(...)`.
        - The service logs progress to stdout (System.out.println) and handles exceptions by marking `FileInformation.status = FAILED` and storing `errorMessage`.
    - **Scheduling & async:**
        - `downloadImages()` selects pending cover image jobs using `bookCoverImageRepository.findPendingTop(COUNT_PER_PROCESS)` and calls `saveImages(...)`.
        - CSV upload may invoke `saveImages(...)` asynchronously via `CompletableFuture.runAsync(...)` to start processing without blocking request completion.

    - **Note:** cover images can be provided via the [`POST /books/csv` bulk upload](#bulk-csv-upload-pipeline) — the CSV `coverImg` column is collected by the upload pipeline. 
        - When `app.image.processing.enabled` is `true`, the pipeline creates `FileInformation` records and asynchronously downloads + resizes images (thumbnail/medium/original) into the configured `app.image.root.path`, linking them to books via `BookCoverImage`.

- Repository & DB notes (exact from code)
    - `BookCoverImageRepository.getImage(bookId, pictureSize)` — JPA query returning `f.filePath` for the requested book & picture size.
    - `BookCoverImageRepository.findPendingTop(countPerProcess)` — native query selecting pending `book_cover_image` rows (joined to `file_information`) ordered by `file_information.id` and limited by `:countPerProcess`.

- Status tracking & error handling
    - `FileInformation.status` is used to track lifecycle: `PENDING` (initial), `DOWNLOADING`, `COMPLETED`, or `FAILED`. On failure the `errorMessage` field is set.
    - The CSV pipeline and image service write diagnostic messages to stdout for troubleshooting; exceptions during image compile/process are converted into `FAILED` status rather than crashing the whole batch.

#### Scheduled image processing (cron job)

- **Method:** `CoverImageService.downloadImages()` — annotated with `@Scheduled(cron = "${scheduler.daily.cron}", zone = "${scheduler.daily.zone}")`.
- **Purpose:** Periodically pick up any pending `BookCoverImage` / `FileInformation` entries and ensure images are downloaded, resized (medium + thumbnail), status-updated and persisted even if immediate async processing did not complete.
- **Selection:** each run calls `bookCoverImageRepository.findPendingTop(:count)` where `:count` is configured by `cover.images.count.per.process`.
- **Config / properties referenced in code:** `scheduler.daily.cron`, `scheduler.daily.zone`, `cover.images.count.per.process`, `app.image.root.path`, `app.image.medium.size`, `app.image.thumbnail.size`, `app.image.processing.enabled`.

### Publishers

- `GET /publishers/{id}/books`
    - **Description:** Return a list of books published by the specified publisher.
    - **Authorization:** `STAFF` or `USER` (`@PreAuthorize("hasAnyRole('STAFF','USER')")`).
    - **Success (200 OK):** returns `List<BookSearchResponseDTO>`.
    - **Error:** `404 Not Found` if publisher does not exist.

- `GET /publishers/{id}`
    - **Description:** Retrieve publisher details by id.
    - **Authorization:** Public (`@PermitAll`).
    - **Success (200 OK):** returns `PublisherDTO`.
    - **Error:** `404 Not Found` if publisher does not exist.

- `POST /publishers`
    - **Description:** Create a new publisher.
    - **Content-Type:** `application/json`
    - **Request DTO:** `PublisherDTO` (`name` is required / `@NotBlank`).
    - **Validation notes:**
        - `name` must be non-empty and unique (service checks `existsByName(name)`).
    - **Authorization:** `MANAGER` OR `ROLE_STAFF` with `ADD_INFORMATION` permission.
    - **Success (201 Created):** returns created `PublisherDTO` (controller sets `@ResponseStatus(HttpStatus.CREATED)`).
    - **Error (duplicate):** `409 Conflict` when a publisher with the same name already exists.

- `PUT /publishers/{id}`
    - **Description:** Update publisher data (name).
    - **Content-Type:** `application/json`
    - **Request DTO:** `PublisherDTO`
    - **Authorization:** `STAFF` (`@PreAuthorize("hasRole('STAFF')")`).
    - **Success (200 OK):** returns updated `PublisherDTO`.
    - **Error:** `404 Not Found` if publisher does not exist.

- `DELETE /publishers/{id}`
    - **Description:** Delete a publisher by id.
    - **Authorization:** `MANAGER` OR `ROLE_STAFF` with `REMOVE_INFORMATION` permission.
    - **Behavior:** The service checks for dependent published books via `publisherRepository.publishedBookIds(id)`; if any dependent book IDs are found, the service throws an `EntityDeletionException` and the publisher is **not** deleted.
    - **Success (204 No Content):** when deletion succeeds.
    - **Error (has dependents):** return a conflict-style error indicating dependent book ids (implementation throws `EntityDeletionException` — map to HTTP 409 Conflict or an appropriate 4xx response).
    - **Error (not found):** `404 Not Found` if publisher does not exist.

    
### Payments

- `POST /payments`
    - **Description:** Create / process a payment for a user. The current implementation is a **demo/mock** that auto-approves payments and stores a placeholder transaction reference; it exists to demonstrate the payment flow without external dependencies.
    - **Content-Type:** `application/json`
    - **Required fields:**
        - `userId` — numeric id of the paying user (the controller enforces `hasRole('USER') AND #paymentRequestDTO.userId.equals(authentication.principal.userId)`).
        - `amount` — numeric/decimal amount to pay.
        - `currency` — payment currency (enum `PaymentCurrency` in code).
    - **Optional / implementation note:** `paymentInformation` would normally contain payment method details; in this demo those details are *not* processed by a real gateway (see Implementation notes below).
    - **Validation & behavior:**
        - The service loads the `User` by `userId`; `EntityNotFoundException` is thrown if the user does not exist.
        - A `Payment` entity is created with status `PENDING`, then the demo sets a placeholder transaction reference (`"PlaceHolder" + payment.getId()`) and marks the payment `COMPLETED`.
        - On failure (in a real integration), the service would mark payment `FAILED` and throw `PaymentFailedException` (commented-out example in code).
    - **Example request body:**
      ```json
      {
        "userId": 42,
        "amount": 49.99,
        "currency": "USD",
        "paymentInformation": { "placeholder": "not-handled-in-demo" }
      }
      ```
    - **Authorization:** `USER` and the `userId` must match the authenticated principal.
    - **Success response:** `200 OK` with a `PaymentDTO` (payment id, amount, currency, status, transactionReference, createdAt).
    - **Error responses:** `404 Not Found` (user missing), `400 Bad Request` (validation), `500` on unexpected failures.

- `GET /payments/{id}`
    - **Description:** Retrieve payment details by payment `id`.
    - **Authorization:** allowed if the authenticated user owns the payment (`authentication.principal.userId.equals(@paymentService.getUserByPaymentId(#id).id)`) **or** the caller has `STAFF` role.
    - **Success response:** `200 OK` with `PaymentDTO`.
    - **Error responses:** `404 Not Found` (payment missing), `403 Forbidden` (not owner / no staff role).

#### Implementation notes
- The `PaymentService.pay(...)` method:
    - Loads the `User` from `UserRepository`.
    - Creates and saves a `Payment` entity with status `PENDING`.
    - Generates a placeholder transaction reference and marks the payment `COMPLETED` (demo behavior).
    - Returns `PaymentDTO` mapped from the saved entity.
- The comments in `PaymentService` explicitly state this is a **simplified mock** for demonstration and list the production differences (use real gateway, generate real references, never handle raw card data).


### Standard Error Format
Errors return example:
```json
{
  "message": "Book with id [145455] was not found",
  "status": 404
}
```

## Report Generation

- `GET /reports/sales` *(Any role with permission 'GENERATE_REPORT' required)*
    - **Description:** Generate a sales report (PDF) for the provided date range and email it to the specified address. The endpoint builds a JasperReports design, fills it from `SaleRepository` projections, exports a PDF named like `sales_report_<timestamp>.pdf` into the configured report directory, and emails the PDF as an attachment. If the queried period contains no rows, a plain informational email is sent instead.
    - **Content-Type:** `application/x-www-form-urlencoded`
    - **Parameters (request):**
        - `startDate` — `LocalDate` (ISO `YYYY-MM-DD`)
        - `endDate` — `LocalDate` (ISO `YYYY-MM-DD`)
        - `emailTo` — destination email address for the report
    - **Behavior & notes:**
        - Report is generated synchronously (the controller calls `ReportService.getSalesReport(...)` which compiles, fills and exports the PDF via JasperReports, then sends an email).
        - PDF files are saved under the configured path held in `app.report.root.path` (in code: `reportDirectoryPath`).
        - Filename pattern: `sales_report_<System.currentTimeMillis()>.pdf`.
        - If no data is found for the date range, an informative email (no attachment) is sent to `emailTo`.
        - Errors during compilation, filling, export, directory creation or emailing throw a `ReportException` 

## Testing

This project is demonstrated via a curated Postman workspace — tidy folders with one-click requests for reviewers.

- **[Postman workspace](https://web.postman.co/workspace/3af5d935-55b6-4e3a-84a9-20af0be97205)**

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

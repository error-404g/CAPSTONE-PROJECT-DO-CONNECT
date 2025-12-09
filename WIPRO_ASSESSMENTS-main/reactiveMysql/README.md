# Reactive MySQL CRUD (WebFlux + R2DBC)

A minimal Spring Boot 3.3 WebFlux application using R2DBC MySQL with full CRUD for `/employees`.

## Prerequisites
- JDK 17
- Maven 3.8+
- MySQL running at `localhost:3306`
- Database created: `wiprotraining`
  ```sql
  CREATE DATABASE IF NOT EXISTS wiprotraining;
  ```

## Timezone issue note
Some Windows MySQL installs report the server time zone as `India Standard Time` (Windows ID), which Java cannot parse.
This project **forces the session time zone** to `Asia/Kolkata` using the R2DBC URL parameter:
```
sessionVariables=time_zone='Asia/Kolkata'
```
so you don't need to change your MySQL global config.

If you still prefer to change MySQL globally:
```sql
SET GLOBAL time_zone = 'Asia/Kolkata';
```

## Run
```bash
mvn spring-boot:run
```

## Endpoints
- `GET  /employees`
- `GET  /employees/{id}`
- `POST /employees`  (JSON: `{ "name": "Charlie", "role": "Manager" }`)
- `PUT  /employees/{id}`
- `DELETE /employees/{id}`

On startup, the app runs `schema.sql` and `data.sql` to create the table and seed data.

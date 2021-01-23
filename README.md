# jpa-validation

Simple project that demonstrates Spring Boot JSON serialization/deserialization and validation using a simple entity model of `Product` and `ProductType` objects.

Features:
* Spring Boot
* Simple JSON rest endpoints
* Custom JSON serialization/deserialization
* Caching
* JPA + Spring Data Repositories
* Flyway database migration
* Mock MVC testing using jsonPath()
* Jacoco code coverage (with custom exclusions)

Example method with request validation.
```java
    @PostMapping("/product")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.ok(service.save(product));
    }
```

## How to use it
The code can be tested using:
```bash
$ ./gradlew clean check
```

To start the application:
```bash
$ ./gradlew bootRun
```

The application is available on http://localhost:8080 (You can view the application health on http://localhost:8080/actuator/health)

### Add a product
POST http://localhost:8080/product
```json
{
	"name": "Product1",
	"type": "X1",
	"category": "insurance",
	"subCategory": "life insurance",
	"roleStart": "2021-01-03T11:00:01",
	"roleEnd": null
}
```

### Add a product (that will fail validation)
POST http://localhost:8080/product
```json
{
	"name": null,
	"type": "foo",
	"category": null,
	"subCategory": null,
	"roleStart": null,
	"roleEnd": null
}
```

### Get a product
GET http://localhost:8080/product/{id}

### Get product types
GET http://localhost:8080/productTypes

### Update a product
PATCH http://localhost:8080/product/{id}
```json
{
  "name": "Product2",
  "type": "R3",
  "category": "insurance",
  "subCategory": "life insurance",
  "roleStart": "2021-02-03T11:00:01",
  "roleEnd": null
}
```

### To flush the cache and force Spring to load entities from the DB
POST http://localhost:8080/cache
```json
RESET
```
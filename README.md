# SE331 Lab - Spring Boot Events & Organizers API

A simple Spring Boot application exposing in-memory APIs for Events and Organizers with pagination and fetch-by-id endpoints. The project mirrors a typical layered architecture (Controller -> Service -> DAO) and uses Lombok for boilerplate reduction.

## Prerequisites
- Java 17+
- Maven 3.9+

## Build & Run
```bash
# From project root
mvn spring-boot:run
``` 
or
```bash
./mvnw clean spring-boot:run
```
The app starts on `http://localhost:8080` by default.

## API Endpoints
### Events
- List (with pagination): `GET /events?_limit={n}&_page={p}`
- Get by ID: `GET /events/{id}`

Headers on list responses:
- `X-Total-Count`: total number of events
- `x-total-count`: same value (for compatibility)

### Organizers
- List (with pagination): `GET /organizers?_limit={n}&_page={p}`
- Get by ID: `GET /organizers/{id}`

Headers on list responses:
- `X-Total-Count`: total number of organizers
- `x-total-count`: same value (for compatibility)

## Example Requests
```bash
# List first 3 events
curl -i "http://localhost:8080/events?_limit=3&_page=1"

# Get event by id
curl -i "http://localhost:8080/events/123"

# List first 2 organizers
curl -i "http://localhost:8080/organizers?_limit=2&_page=1"

# Get organizer by id
curl -i "http://localhost:8080/organizers/1"
```

## Project Structure (key files)
- `src/main/java/se331/lab/Application.java`: Spring Boot entrypoint
- `src/main/java/se331/lab/Event.java`: Event entity (POJO with Lombok)
- `src/main/java/se331/lab/Organizer.java`: Organizer entity (POJO with Lombok)
- `src/main/java/se331/lab/controller/EventController.java`: Event endpoints
- `src/main/java/se331/lab/controller/OrganizerController.java`: Organizer endpoints
- `src/main/java/se331/lab/service/`: Service interfaces/implementations
- `src/main/java/se331/lab/dao/`: In-memory DAOs with seeded data

## Notes
- Data is in-memory; it resets at startup (see DAO `@PostConstruct`).
- CORS is open (`@CrossOrigin("*")`) on controllers for easy testing.
- Pagination uses `_limit` and `_page` query parameters. If omitted, all items are returned.

## Development
- Build: `mvn clean package`
- Run tests: `mvn test`

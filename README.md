# WheelyWise — Dublin Bike Availability & Weather

WheelyWise is a full-stack Dublin mobility demonstration that combines live bike-station availability, station history, weather, forecasts, route planning, and an interactive Google Map. The browser frontend is served by the same Spring Boot application that provides the REST API.

## Engineering overview

- Java 8 and Spring Boot 2.7 REST backend
- MyBatis persistence backed by MySQL 8
- Scheduled JCDecaux station ingestion every five minutes
- Scheduled OpenWeather current-weather and forecast ingestion every hour
- Idempotent station snapshots with seven-day retention and historical availability queries
- Static HTML, CSS, and JavaScript frontend with Google Maps integration
- Environment-based database and API configuration

The backend follows a small layered structure: controllers expose `/api` endpoints, services own application operations and transaction boundaries, MyBatis repositories map domain records to MySQL, external API clients translate provider responses, and scheduled tasks coordinate ingestion and cleanup. Persisted data remains available when an external provider is temporarily unavailable.

## Local setup

Requirements: Java 8+, Maven, MySQL 8, and API keys for JCDecaux, OpenWeather, and Google Maps.

### 1. Initialize MySQL

> **Warning:** `init-database.sql` drops and recreates the application tables. Running it again deletes existing station and weather history. Back up any data you need first.

```bash
mysql -u root -p < init-database.sql
```

### 2. Configure the environment

Use `.env.example` as the variable list. Export the values in the shell that starts Maven:

```bash
export DB_URL='jdbc:mysql://localhost:3306/dublin_service?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true'
export DB_USERNAME='root'
export DB_PASSWORD='your-local-password'
export JCDECAUX_API_KEY='your-jcdecaux-key'
export OPENWEATHER_API_KEY='your-openweather-key'
export GOOGLE_MAPS_API_KEY='your-browser-key'
```

`DB_URL` and `DB_USERNAME` may be omitted when the defaults in `application.yml` match the local database. Keep real credentials out of source control.

For Google Maps, enable the Maps JavaScript API and Places API for the browser key. Restrict the key to HTTP referrers and allow the local origins used for development, such as `http://localhost:8080/*` and, if needed, `http://127.0.0.1:8080/*`.

### 3. Build and run

```bash
mvn test
mvn package
mvn spring-boot:run
```

Open [http://localhost:8080/](http://localhost:8080/). Swagger UI is available at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

## Main API endpoints

- `GET /api/bike/stations`
- `GET /api/bike/stations/{number}`
- `GET /api/bike/stations/{number}/history?limit=24`
- `GET /api/weather/current`
- `GET /api/weather/forecast`
- `GET /api/config`

## Project Origin & Attribution

This application originated from a university group project. The WheelyWise frontend was created as part of that group project, and the initial Java backend implementation originated from another contributor's work in the repository history.

The current repository represents subsequent work to integrate the frontend and Java backend into one Spring Boot application, align the browser with the Java APIs, add station-history support, externalize configuration, audit credentials and generated files, improve ingestion integrity and failure isolation, add focused tests, and prepare the project for portfolio presentation.

These origins are stated explicitly so the repository does not imply sole authorship of components originally contributed by others.

## Scope

Prediction and the former demonstration account/payment UI were intentionally removed. Authentication, deployment, and production-scale infrastructure are outside the current project scope.

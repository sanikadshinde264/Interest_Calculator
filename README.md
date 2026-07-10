# 💰 Interest Calculator
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 279cbedf77ec1e7147c6df3fbf41fd244e4494b3
A full-stack simple interest calculator with day-wise and month-wise calculations, saved history, and a home-screen installable app (PWA) — built with Java, Spring Boot, MySQL, HTML, CSS, and vanilla JavaScript.

## Features

### Calculator
- Day-wise simple interest — enter principal, rate, and a start/end date; actual days in the period are calculated automatically
- Month-wise simple interest — enter principal, rate, and number of months
- Clean results card showing principal, rate, period, interest amount, and total payable
- Client-side + server-side validation with inline field errors

### History
- Every calculation is saved and listed, most recent first
- Click any history row to reload that calculation's full breakdown into the results card
- Delete individual history entries

### Installable app (PWA)
- Ships with a manifest, icons, and a service worker
- Installable straight from the browser to a phone's home screen — opens full-screen, no browser bars, no app store needed

## Tech stack
- **Frontend:** HTML, CSS, vanilla JavaScript
- **Backend:** Java 17, Spring Boot 3
- **Database:** MySQL (H2 fallback available for local testing without installing MySQL)
- **Build tool:** Maven

## Folder structure
```text
interest-calculator/
├── src/main/java/com/yourcompany/interestcalc/
│   ├── InterestCalculatorApplication.java   Entry point
│   ├── controller/InterestController.java   REST endpoints
│   ├── service/InterestService.java         Interest calculation logic
│   ├── model/
│   │   ├── DaywiseInterest.java
│   │   └── MonthwiseInterest.java
│   ├── repository/
│   │   ├── DaywiseInterestRepository.java
│   │   └── MonthwiseInterestRepository.java
│   ├── dto/
│   │   ├── DaywiseRequest.java
│   │   ├── MonthwiseRequest.java
│   │   └── InterestResponse.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       └── InvalidDateRangeException.java
├── src/main/resources/
│   ├── application.properties               DB & server config
│   └── static/
│       ├── index.html                       Main UI
│       ├── manifest.json                    PWA manifest
│       ├── service-worker.js                Offline app shell caching
│       ├── css/style.css
│       ├── js/app.js
│       └── icons/
├── src/test/java/.../InterestCalculatorApplicationTests.java
└── pom.xml
```

## Setup

### Requirements
- **Java 17+**
- **Maven 3.8+**
- **MySQL 8+** (or skip this and use the H2 in-memory fallback — see step 2)

### 1. Get the project
```bash
git clone https://github.com/sanikadshinde264/Interest_Calculator.git
cd Interest_Calculator
```

### 2. Set up the database
Create the database (or let `createDatabaseIfNotExist=true` do it for you):
```sql
CREATE DATABASE interest_calc;
```
Then set your credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/interest_calc?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

**No MySQL installed?** Comment out the MySQL block in `application.properties` and uncomment the H2 block instead — it runs entirely in memory, no install required (history won't persist between restarts).

### 3. Run it

**Windows (PowerShell / cmd):**
```powershell
mvn spring-boot:run
```

**macOS / Linux:**
```bash
mvn spring-boot:run
```

Wait for this line in the console output:
```
Tomcat started on port(s): 8080
Started InterestCalculatorApplication in X seconds
```

**Or build a standalone jar and run that instead:**
```bash
mvn clean package
java -jar target/interest-calculator-1.0.0.jar
```

### 4. Open the app
```
http://localhost:8080
```
The calculator UI loads directly — no separate frontend setup needed.

### 5. Install it on your phone (optional)
Once it's running and reachable from your phone (same Wi-Fi, or deployed publicly):
- **Android (Chrome):** open the site → tap **⋮** → **Add to Home screen**
- **iPhone (Safari):** open the site → tap **Share** → **Add to Home Screen**

## API Endpoints

| Method | Endpoint | Body | Purpose |
|---|---|---|---|
| POST | `/api/interest/daywise` | `{ "amount": 10000, "interestRate": 8.5, "startDate": "2026-01-01", "endDate": "2026-04-01" }` | Calculate + save day-wise interest |
| GET | `/api/interest/daywise` | — | Get day-wise history |
| DELETE | `/api/interest/daywise/{id}` | — | Delete a day-wise record |
| POST | `/api/interest/monthwise` | `{ "amount": 10000, "interestRate": 8.5, "months": 6 }` | Calculate + save month-wise interest |
| GET | `/api/interest/monthwise` | — | Get month-wise history |
| DELETE | `/api/interest/monthwise/{id}` | — | Delete a month-wise record |

### Example request
```bash
curl -X POST http://localhost:8080/api/interest/daywise \
  -H "Content-Type: application/json" \
  -d '{"amount":10000,"interestRate":8.5,"startDate":"2026-01-01","endDate":"2026-04-01"}'
```

### Example response
```json
{
  "id": 1,
  "amount": 10000,
  "interestRate": 8.5,
  "startDate": "2026-01-01",
  "endDate": "2026-04-01",
  "actualDays": 90,
  "interestAmount": 209.59,
  "totalAmount": 10209.59,
  "createdAt": "2026-07-10T14:22:01.123"
}
```

## Validation rules enforced
- `amount` — required, must be > 0
- `interestRate` — required, must be > 0, at most 2 decimal places
- `endDate` — must be strictly after `startDate` (day-wise only)
- `months` — required, must be a positive whole number (month-wise only)

Validation failures return `400 Bad Request` with a `fieldErrors` map naming each invalid field. See `exception/GlobalExceptionHandler.java`.

## Deploying (e.g. Railway)
`application.properties` reads its config from environment variables, so the same jar works locally and in production without editing the file:

| Variable | Purpose | Example |
|---|---|---|
| `PORT` | Port the app listens on | `8080` |
| `DB_URL` | Full JDBC URL | `jdbc:mysql://<host>:<port>/<database>` |
| `DB_USERNAME` | Database username | — |
| `DB_PASSWORD` | Database password | — |

## License
This project is for educational purposes.
<<<<<<< HEAD
=======
=======

A simple interest calculator (day-wise & month-wise) with saved history — built with Java, Spring Boot, MySQL, HTML, CSS, and JavaScript. Installable as a mobile app.

## Features
- Day-wise interest calculation (auto-calculates days between two dates)
- Month-wise interest calculation
- Results breakdown: principal, rate, period, interest, total payable
- Saved calculation history — click a row to view it again
- Delete history entries
- Installable on phone as a home-screen app (PWA)

## Tech Stack
- **Frontend:** HTML, CSS, JavaScript
- **Backend:** Java, Spring Boot
- **Database:** MySQL

## Setup

1. Clone the repo:
   ```bash
   git clone https://github.com/sanikadshinde264/Interest_Calculator.git
   cd Interest_Calculator
   ```

2. Create the database:
   ```sql
   CREATE DATABASE interest_calc;
   ```

3. Set your MySQL username/password in `src/main/resources/application.properties`.

4. Run the app:
   ```bash
   mvn spring-boot:run
   ```

5. Open in browser:
   ```
   http://localhost:8080
   ```

## License
For educational purposes.
>>>>>>> 9b16c822bcf010c0222e5048cb15526ee935fe17
>>>>>>> 279cbedf77ec1e7147c6df3fbf41fd244e4494b3

# 💰 Interest Calculator
A web app for quickly calculating simple interest — either day-wise (based on an exact date range) or month-wise (based on number of months). Every calculation is saved to a history you can revisit or delete, and the app can be installed on your phone's home screen like a native app.

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
- **Database:** MySQL 
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
- **MySQL 8+** 

### 1. Get the project
```bash
git clone https://github.com/sanikadshinde264/Interest_Calculator.git
cd Interest_Calculator
```

### 2. Set up the database
Create the database:
```sql
CREATE DATABASE interest_calc;
```
Then set your credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/interest_calc?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

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

## License
This project is for educational purposes.

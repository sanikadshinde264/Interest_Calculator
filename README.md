# 💰 Interest Calculator

A web app for calculating simple interest (day-wise or month-wise) and recurring deposit (RD) maturity projections — with saved calculation history and installable as a PWA.

---

## 🔎 Overview
Interest Calculator is a Spring Boot + MySQL web app with a clean, installable frontend for common interest calculations. It supports **day-wise simple interest** (based on an exact date range), **month-wise simple interest** (based on number of months), and **recurring deposit (RD) projections** (based on monthly deposit, rate, tenure, and start date). Every calculation is saved to a history you can revisit or delete, and the app can be installed on your phone's home screen like a native app.

---

## ❓ Problem Statement
Quickly and accurately working out interest — whether for a loan, a fixed deposit, or a recurring deposit — usually means doing manual math or hunting for a reliable calculator online. This project solves that by providing:
- A single tool for the three most common interest calculations (day-wise, month-wise, RD)
- Automatic, accurate date-based day counting instead of manual calendar math
- A saved history of past calculations for quick reference, without needing to redo the math
- A fast, installable, offline-friendly experience via PWA support

---

## ✨ Features

### 🧮 Calculator
- **Day-wise simple interest** — enter principal, rate, and a start/end date; actual days in the period are calculated automatically
- **Month-wise simple interest** — enter principal, rate, and number of months
- **RD Projection** — enter monthly deposit, interest rate, number of months, and start date to project maturity value
- Clean results card showing principal/deposit, rate, period, interest amount, and total payable / maturity value
- Client-side + server-side validation with inline field errors

### 🕘 History
- Every calculation (day-wise, month-wise, and RD) is saved and listed, most recent first
- Click any history row to reload that calculation's full breakdown into the results card
- Delete individual history entries, or clear all history for a given calculator type

### 📲 Installable App (PWA)
- Ships with a manifest, icons, and a service worker
- Installable straight from the browser to a phone's home screen — opens full-screen, no browser bars, no app store needed

---

## 🗂️ Dataset
This project doesn't use a static dataset — all data is generated and stored live in MySQL as the app is used:
- **Day-wise interest calculations** — principal, rate, date range, computed interest
- **Month-wise interest calculations** — principal, rate, number of months, computed interest
- **Recurring deposit calculations** — monthly deposit, rate, tenure, start date, projected maturity value

---

## 🛠️ Tools & Technologies
- **Frontend:** HTML, CSS, vanilla JavaScript
- **Backend:** Java 17, Spring Boot 3
- **Database:** MySQL 8+
- **Build tool:** Maven

---

## ⚙️ Methods / Methodology
1. **API design:** Built a REST API under `/api/interest` with dedicated endpoints for day-wise, month-wise, and recurring-deposit calculations (`POST` to calculate & save, `GET` for history, `DELETE` for individual or bulk removal).
2. **Calculation logic:** Implemented interest formulas in `InterestService`, including exact day-count logic for the day-wise calculator and compounding projection logic for RD maturity.
3. **Persistence:** Modeled each calculation type (`DaywiseInterest`, `MonthwiseInterest`, `RecurringDepositInterest`) as its own JPA entity/repository, backed by MySQL.
4. **Validation:** Applied request validation (`@Valid` DTOs) on the backend and mirrored it with inline error messages on the frontend for instant feedback.
5. **Frontend:** Built a single-page, tabbed UI (Day-wise / Month-wise / RD Projection) in vanilla JS/HTML/CSS that calls the REST API and renders results and history.
6. **PWA support:** Added a web app manifest, icons, and a service worker so the app can be installed and used like a native app.

---

## 📁 Project Directory Structure
```text
interest-calculator/
├── src/main/java/com/yourcompany/interestcalc/
│   ├── InterestCalculatorApplication.java   Entry point
│   ├── controller/InterestController.java   REST endpoints (daywise, monthwise, rd)
│   ├── service/InterestService.java         Interest calculation logic
│   ├── model/
│   │   ├── DaywiseInterest.java
│   │   ├── MonthwiseInterest.java
│   │   └── RecurringDepositInterest.java
│   ├── repository/
│   │   ├── DaywiseInterestRepository.java
│   │   ├── MonthwiseInterestRepository.java
│   │   └── RecurringDepositRepository.java
│   ├── dto/
│   │   ├── DaywiseRequest.java
│   │   ├── MonthwiseRequest.java
│   │   ├── InterestResponse.java
│   │   ├── RecurringDepositRequest.java
│   │   ├── RecurringDepositResponse.java
│   │   └── RecurringDepositRow.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       └── InvalidDateRangeException.java
├── src/main/resources/
│   ├── application.properties               DB & server config
│   └── static/
│       ├── index.html                       Main UI (Day-wise / Month-wise / RD tabs)
│       ├── manifest.json                    PWA manifest
│       ├── service-worker.js                Offline app shell caching
│       ├── css/style.css
│       ├── js/app.js
│       └── icons/
├── src/test/java/.../InterestCalculatorApplicationTests.java
└── pom.xml
```

---

## 🖥️ Dashboard / Model / Output
The app's UI has three tabs, all served from `http://localhost:8080`:
- **Day-wise** — enter principal, rate, and a date range; see days elapsed, interest, and total payable
- **Month-wise** — enter principal, rate, and number of months; see interest and total payable
- **RD Projection** — enter monthly deposit, rate, tenure, and start date; see projected maturity value
- Each tab has its own **history list** below the results card, with click-to-reload and delete actions

---

## 💡 Key Insights
- Splitting day-wise, month-wise, and RD calculations into their own entities/endpoints keeps each calculation type's validation and persistence logic independent and easy to extend.
- Automating exact day-count for the day-wise calculator removes a common source of manual calculation error.
- Adding PWA support (manifest + service worker) turns a simple calculator into an app-like, installable tool with minimal extra code.

---

## ✅ Results & Conclusion
Interest Calculator gives users a fast, reliable way to compute simple interest and RD maturity values without manual math, while keeping a searchable history of every past calculation. Its installable, offline-friendly PWA setup makes it convenient to use on mobile as a lightweight, native-feeling app.

---

## ▶️ How to Run the Project

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
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

### 3. Run it

**Windows (PowerShell / cmd):**
```powershell
mvn spring-boot:run
```

**macOS / Linux:**
```bash
./mvnw spring-boot:run
```

### 4. Open the app
```
http://localhost:8080
```
The calculator UI loads directly — no separate frontend setup needed.

---

## ☁️ Live Deployment
🔗 **[interestcalculator-production-05d8.up.railway.app](https://interestcalculator-production-05d8.up.railway.app/)** — deployed on [Railway](https://railway.app/)

Railway builds the Spring Boot app (via Maven/the included `pom.xml`) and runs it alongside a provisioned MySQL instance, with the datasource credentials supplied through Railway environment variables (overriding the local `application.properties` defaults).

---

## 🚀 Future Work
- Add compound interest calculations alongside simple interest
- Add user accounts so history is private per user instead of shared/global
- Export calculation history to CSV/PDF
- Add charts to visualize interest growth over time
- Add dark mode to the UI

---

## 👤 Author & Contact
**Sanika Shinde** <br>
📧 [sanikadshinde264@gmail.com] | 🔗 [www.linkedin.com/in/sanikadshinde264] 

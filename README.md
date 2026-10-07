# 📱 Practical-7: SQLite & REST JSON API Integration

<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white" alt="SQLite" />
  <img src="https://img.shields.io/badge/Material_Design-757575?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material Design" />
  <img src="https://img.shields.io/badge/Coroutines-000000?style=for-the-badge&logo=kotlin&logoColor=white" alt="Coroutines" />
</p>

---

## 🎯 Aim
> **To develop an Android application that retrieves person data in JSON format from an internet API over HTTP connection and stores the retrieved data in an SQLite database for offline-first persistence.**

---

## 👤 Developer Information

| Field | Student Details |
| :--- | :--- |
| **Name** | **Maharsh Patel** |
| **Enrollment Number** | **24012011102** |
| **Course** | Mobile Application Development (MAD) |
| **Semester** | 5th Semester |
| **Institute** | UVPCE |
| **Practical Number** | Practical 7 |
| **GitHub Repository** | [https://github.com/maharsh-patel/MAD_24012011102_Practical_7](https://github.com/maharsh-patel/MAD_24012011102_Practical_7) |

---

## 📋 Table of Contents

- [🎯 Aim](#-aim)
- [👤 Developer Information](#-developer-information)
- [ℹ️ Project Information](#️-project-information)
- [✨ Key Features](#-key-features)
- [⚠️ Important Implementation Details](#️-important-implementation-details)
- [📁 Project Directory Structure](#-project-directory-structure)
- [⚙️ Code Architecture & Components](#️-code-architecture--components)
- [📄 API Response Schema & Mapping](#-api-response-schema--mapping)
- [💾 Database Schema & Contract](#-database-schema--contract)
- [💡 Key Code Snippets](#-key-code-snippets)
- [📸 Screenshots](#-screenshots)
- [🛠 Tech Stack & Dependencies](#-tech-stack--dependencies)
- [🚀 How to Build & Run](#-how-to-build--run)

---

## ℹ️ Project Information

This Android application demonstrates end-to-end integration of **remote REST API communication**, **nested JSON parsing**, **asynchronous background processing**, and **local data persistence using SQLite** in Kotlin.

### How it Works:
1. **Launch Phase:** The app queries the local SQLite database (`persons_db`).
2. **Offline First:** If records exist, contacts are instantly displayed in a `RecyclerView` without network delay.
3. **API Fetching:** If the database is empty or the user taps the refresh **Floating Action Button (FAB)**, the app executes an HTTP GET request to `https://api.json-generator.com/templates/5rDXHcbgpo93/data` with Bearer token authentication.
4. **JSON Parsing & DB Insertion:** The received nested JSON string is parsed into `Person` model objects and inserted/updated into SQLite using `CONFLICT_REPLACE`.
5. **Real-Time UI Updates:** The `RecyclerView` updates smoothly using Kotlin Coroutines on `Dispatchers.Main`. Users can also delete individual contact records, updating both the SQLite database and the UI list simultaneously.

---

## ✨ Key Features

- 🌐 **RESTful API Fetching:** Fetches contact data over HTTP via `HttpURLConnection`.
- 🔑 **Bearer Token Authorization:** Attaches `Authorization: Bearer <token>` and `Content-Type: application/json` headers to API requests.
- ⚡ **Kotlin Coroutines:** Asynchronous execution using `CoroutineScope` on `Dispatchers.IO` for network/database work and `Dispatchers.Main` for UI rendering.
- 📦 **Nested JSON Parsing:** Uses native `org.json` package to extract fields (`profile.name`, `profile.address`, `profile.location.lat/long`).
- 💾 **SQLite Persistence:** Custom `SQLiteOpenHelper` handling table creation, version upgrades, and CRUD operations.
- 📱 **Material UI Design:** Uses `MaterialCardView`, `RecyclerView`, `FloatingActionButton`, and `ProgressBar`.
- 🗑️ **Interactive Operations:** Supports deleting contacts from the database and manually refreshing data from the server.

---

## ⚠️ Important Implementation Details

> [!IMPORTANT]
> 1. **INTERNET Permission:** Declared in `AndroidManifest.xml` via `<uses-permission android:name="android.permission.INTERNET" />`.
> 2. **API Authentication Token:** The API requires a Bearer Token (`d7wrtfqywyhu7y2bcbsz3cgjpbfisuhnmbibvgvf`) passed in HTTP headers.
> 3. **Non-Blocking Threads:** All networking and database transactions run off the main UI thread via `Dispatchers.IO` to prevent ANR errors.
> 4. **Conflict Resolution Policy:** Uses `SQLiteDatabase.CONFLICT_REPLACE` on primary key `id` to handle updates seamlessly without duplicate entry errors.
> 5. **ViewBinding:** Enabled in `build.gradle.kts` for type-safe layout access in `activity_main.xml` and `item_person.xml`.

---

## 📁 Project Directory Structure

```
MAD_24012011102_Practical_7/
├── .gitignore
├── README.md
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── Screenshots/
│   └── SS_7_1.png
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
└── app/
    ├── build.gradle.kts
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/example/mad_24012011102_practical_7/
        │   │   ├── MainActivity.kt         # Main Activity & UI Controller
        │   │   ├── HttpRequest.kt          # HTTP Client & Bearer Auth
        │   │   ├── DatabaseHelper.kt       # SQLite Helper (CRUD Operations)
        │   │   ├── PersonDbTableData.kt    # SQLite Table Schema Contract
        │   │   ├── Person.kt               # Serializable Data Class
        │   │   └── PersonAdapter.kt        # RecyclerView Adapter
        │   └── res/
        │       ├── drawable/               # Vector Drawables & Backgrounds
        │       ├── layout/
        │       │   ├── activity_main.xml   # Main Layout (RecyclerView & FAB)
        │       │   └── item_person.xml     # Contact Card Row Item Layout
        │       └── values/
        │           ├── colors.xml
        │           ├── strings.xml
        │           └── themes.xml
        └── test/                           # Unit Tests
```

---

## ⚙️ Code Architecture & Components

| Component | File Path | Functional Description |
| :--- | :--- | :--- |
| **Main Screen** | `MainActivity.kt` | Manages UI state, triggers API requests via Coroutines, parses JSON responses, and binds contacts to RecyclerView. |
| **Network Manager** | `HttpRequest.kt` | Connects to remote URL using `HttpURLConnection`, sets Bearer headers, and returns response string. |
| **Database Manager** | `DatabaseHelper.kt` | Extends `SQLiteOpenHelper`. Handles SQLite table creation and CRUD operations (`insertPerson`, `getPerson`, `allPersons`, `deletePerson`, `updatePerson`). |
| **Database Schema** | `PersonDbTableData.kt` | Defines database constants (table name `persons`, column names) and `CREATE TABLE` SQL syntax. |
| **Data Model** | `Person.kt` | Data structure class implementing `Serializable`, holding contact properties (`id`, `name`, `emailId`, `phoneNo`, `address`, `latitude`, `longitude`). |
| **List Adapter** | `PersonAdapter.kt` | `RecyclerView.Adapter` binding contact cards to layout views and handling individual row delete actions. |

---

## 📄 API Response Schema & Mapping

### Sample API Response (JSON)
```json
[
  {
    "id": "6732f9131e5f",
    "email": "john.doe@example.com",
    "phone": "+1 (800) 555-0199",
    "profile": {
      "name": "John Doe",
      "address": "123 Main St, New York, NY 10001",
      "location": {
        "lat": 40.7128,
        "long": -74.0060
      }
    }
  }
]
```

### JSON Mapping to `Person.kt` Model
| JSON Path | Model Field | Data Type | Database Column |
| :--- | :--- | :--- | :--- |
| `id` | `id` | `String` | `id` (PRIMARY KEY) |
| `profile.name` | `name` | `String` | `person_name` |
| `email` | `emailId` | `String` | `person_email_id` |
| `phone` | `phoneNo` | `String` | `person_phone_no` |
| `profile.address` | `address` | `String` | `person_address` |
| `profile.location.lat` | `latitude` | `Double` | `person_lat` |
| `profile.location.long` | `longitude` | `Double` | `person_long` |

---

## 💾 Database Schema & Contract

**Database Name:** `persons_db` | **Version:** `1` | **Table:** `persons`

```sql
CREATE TABLE persons(
    id TEXT PRIMARY KEY,
    person_name TEXT,
    person_email_id TEXT,
    person_phone_no TEXT,
    person_address TEXT,
    person_lat REAL,
    person_long REAL
);
```

---

## 💡 Key Code Snippets

### 1. HTTP Request with Bearer Auth (`HttpRequest.kt`)
```kotlin
val url = URL(reqUrl)
val conn = url.openConnection() as HttpURLConnection
if (token != null) {
    conn.setRequestProperty("Authorization", "Bearer $token")
    conn.setRequestProperty("Content-Type", "application/json")
}
conn.requestMethod = "GET"
response = convertStreamToString(BufferedInputStream(conn.inputStream))
```

### 2. SQLite Conflict Replace Insertion (`DatabaseHelper.kt`)
```kotlin
fun insertPerson(person: Person): Long {
    val db = writableDatabase
    val id = db.insertWithOnConflict(
        PersonDbTableData.TABLE_NAME,
        null,
        getValues(person),
        SQLiteDatabase.CONFLICT_REPLACE
    )
    db.close()
    return id
}
```

### 3. Asynchronous Execution & UI Switch (`MainActivity.kt`)
```kotlin
CoroutineScope(Dispatchers.IO).launch {
    val json = HttpRequest().makeServiceCall(API_URL, API_TOKEN)
    withContext(Dispatchers.Main) {
        if (json != null) {
            val fetched = parsePersonsFromJson(json)
            fetched.forEach { dbHelper.insertPerson(it) }
            persons.clear()
            persons.addAll(dbHelper.allPersons)
            adapter.notifyDataSetChanged()
        }
    }
}
```

---

## 📸 Screenshots

<p align="center">
  <img src="Screenshots/SS_7_1.png" alt="Contact List Screen" width="300">
</p>

---

## 🛠 Tech Stack & Dependencies

- **Language:** Kotlin 1.9+
- **Database:** SQLite (`SQLiteOpenHelper`)
- **Networking:** `HttpURLConnection` + `org.json`
- **Concurrency:** Kotlin Coroutines (`kotlinx-coroutines-android`)
- **UI Framework:** Material Design (`RecyclerView`, `MaterialCardView`, `FloatingActionButton`)
- **View Binding:** ViewBinding enabled
- **Target SDK:** 34 / 35 | **Min SDK:** 24

---

## 🚀 How to Build & Run

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/maharsh-patel/MAD_24012011102_Practical_7.git
   ```
2. **Open in Android Studio:**
   - Open Android Studio and select **Open**.
   - Navigate to the cloned `MAD_24012011102_Practical_7` folder.
3. **Sync & Run:**
   - Wait for Gradle sync to complete.
   - Run on an Emulator or connected Physical Device (Android 7.0+ / API 24+).

---

<p align="center">
  <sub>👤 <b>Maharsh Patel</b> &nbsp;•&nbsp; 🆔 Enrollment No: <b>24012011102</b> &nbsp;•&nbsp; 🕒 Practical 7</sub>
</p>

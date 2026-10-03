# Practical-7: SQLite & JSON API Integration

---

**Aim:** To develop an Android application that retrieves person data in JSON format from an internet API and stores the retrieved data in an SQLite database.

## 🔗 Project Overview

This Android application demonstrates end-to-end integration of **remote REST API communication**, **JSON parsing**, **asynchronous background processing**, and **local data persistence using SQLite** in Kotlin.

When launched, the application checks the local SQLite database (`persons_db`). If the database is empty, or when the user taps the refresh **Floating Action Button (FAB)**, the app executes an HTTP request over `HttpURLConnection` with Bearer token authentication to fetch JSON person data. The nested JSON payload is parsed into `Person` model objects and inserted into SQLite. The data is then rendered dynamically in a **RecyclerView** as Material card views.

All contact data survives application restarts as it is retrieved directly from the local database. Users can also delete contacts individually, which updates both the local database and the UI in real-time.

---

## 📌 Key Features

- 🌐 **REST API Fetching:** Fetches contact data from JSON-Generator template API via `HttpURLConnection`.
- 🔑 **Bearer Token Authentication:** Adds `Authorization: Bearer <token>` and `Content-Type: application/json` headers to secure network requests.
- ⚡ **Asynchronous Coroutines:** Utilizes Kotlin `CoroutineScope` with `Dispatchers.IO` for network and database operations, switching to `Dispatchers.Main` for UI updates.
- 📦 **JSON Parsing:** Uses native `org.json.JSONArray` and `org.json.JSONObject` to parse complex nested JSON objects (`profile.name`, `profile.address`, `profile.location.lat/long`).
- 💾 **SQLite Persistence:** Uses `SQLiteOpenHelper` to store contact records locally with `CONFLICT_REPLACE` policy.
- 📱 **Interactive Material UI:** Displays contact details (Name, Phone, Email, Address) in a `RecyclerView` using `MaterialCardView` cards with individual delete buttons and a refresh FAB.
- 🔄 **Offline First Access:** Displays previously stored contacts instantly on launch without requiring repeated network calls unless refreshed.

---

## ⚠️ Important Details & Considerations

> [!IMPORTANT]
> 1. **INTERNET Permission:** The app requires network access granted via `<uses-permission android:name="android.permission.INTERNET" />` in `AndroidManifest.xml`.
> 2. **API Authentication:** Network requests to `https://api.json-generator.com/templates/5rDXHcbgpo93/data` require a valid Bearer token attached to the `Authorization` request header.
> 3. **Thread Safety & Background Execution:** `HttpURLConnection` and SQLite database reads/writes must never be executed on the main UI thread. Kotlin Coroutines (`Dispatchers.IO` and `Dispatchers.Main`) ensure smooth, non-blocking UI operations.
> 4. **Conflict Resolution Strategy:** SQLite insertion uses `SQLiteDatabase.CONFLICT_REPLACE` on the primary key `id` to handle updates seamlessly without crashing or creating duplicate entries upon refresh.
> 5. **ViewBinding:** ViewBinding is enabled in `build.gradle.kts` for safe, null-type binding across layouts (`activity_main.xml` and `item_person.xml`).

---

## 📁 Key Components & Code Architecture

| Component | Class / File | Description |
| :--- | :--- | :--- |
| **Main Screen** | `MainActivity.kt` | Manages app lifecycle, initial database check, Coroutines launch, JSON response parsing, and RecyclerView adapter updates. |
| **Network Client** | `HttpRequest.kt` | Connects to remote API using `HttpURLConnection`, sets Bearer auth token, reads stream input, and returns raw JSON string response. |
| **Database Handler** | `DatabaseHelper.kt` | Subclass of `SQLiteOpenHelper`. Handles table creation, database versioning, and CRUD operations (`insertPerson`, `getPerson`, `allPersons`, `deletePerson`, `updatePerson`). |
| **Database Schema** | `PersonDbTableData.kt` | Defines database schema constants (`persons` table, column names for `id`, `name`, `email`, `phone`, `address`, `lat`, `long`) and `CREATE TABLE` SQL syntax. |
| **Data Model** | `Person.kt` | Data structure class implementing `Serializable`, holding contact properties (`id`, `name`, `emailId`, `phoneNo`, `address`, `latitude`, `longitude`). |
| **List Adapter** | `PersonAdapter.kt` | `RecyclerView.Adapter` binding contact data to card layout views and handling row delete click events. |

---

## 🛠 Tech Stack & Tools

- **Language:** Kotlin
- **Database:** SQLite (`SQLiteOpenHelper`)
- **Networking:** `HttpURLConnection` + `org.json`
- **Concurrency:** Kotlin Coroutines (`Dispatchers.IO`, `Dispatchers.Main`)
- **UI Components:** Material Design, `RecyclerView`, `MaterialCardView`, `FloatingActionButton`, `ProgressBar`
- **Architecture/Binding:** ViewBinding

---

## 📸 Screenshots

<p align="center">
  <img src="Screenshots/SS_7_1.png" alt="Contact List (RecyclerView + SQLite)" width="300">
</p>

---

<p align="center">
  <sub>👤 <b>Maharsh Patel</b> &nbsp;•&nbsp; 🆔 Enrollment No: <b>24012011102</b> &nbsp;•&nbsp; 🕒 Practical 7</sub>
</p>

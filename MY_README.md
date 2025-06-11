# Pupil Management Android App - Case Study Submission
**By:** Oke Oluwaseun Michael  
**Tech Stack:** Java, Android (XML layouts, MVVM architecture), Room DB, Retrofit, WorkManager  

---

## Summary

Simple Android app for managing student records. You can add students, view their information, and delete them. Works offline and syncs when you're back online.

---

## Implemented Requirements

1. **List all pupils**  
   - Pupils are displayed in a `RecyclerView` using data from Room DB.
   - Syncs with the remote API when online.

2. **View details of a single pupil**  
   - Tapping a pupil opens a detailed view fragment with full information.

3. **Add new pupil**  
   - A form captures the pupil's details and current GPS location.
   - The entry is stored locally and marked as `isSynced = false` until sync.

4. **Delete a pupil**  
   - Users can delete a pupil from the list; the change is synced later if offline.

5. **Offline support and sync**  
   - The app uses Room DB for offline persistence.
   - `WorkManager` detects network changes and syncs unsynced data automatically.

---

## Design Approach

- **Architecture:** MVVM pattern for separation of concerns.
- **UI Navigation:** Fragment-based navigation within `MainActivity`.
- **Database:** Room handles local data persistence with an `isSynced` field.
- **Networking:** Retrofit manages API communication.
- **Sync Strategy:**  
   - Offline entries are flagged.
   - `WorkManager` schedules background sync when connectivity returns.
   - Sync includes posting new pupils and deleting synced ones.

---

## Assumptions Made

- Latitude and longitude are required at pupil creation for potential location-based analytics.
- The API simulates network latency, failure, and mutations, so sync logic is resilient to retries and failure handling.
- I made the UI simple for clarity, with error messages for user feedback.

---

## Folder Structure

```
├── data/
│   ├── api/             → Retrofit interface & client
│   ├── db/              → Room database, DAO
│   └── model/           → Pupil data class
│
├── repository/          → Handles data ops & sync logic
├── ui/
│   ├── fragments/       → List, Add, Detail fragments
│   └── adapters/        → RecyclerView adapter
│
├── utils/               → NetworkUtils
└── viewmodel/           → PupilViewModel
```

---

## Exclusions

The final `.zip` submission excludes:

- `/build/`, `/app/build/`, `.gradle/`, `.idea/`, `.iml`, and other binary-generated files.
- The original README was mistakenly submitted as PDF, now corrected as plain text.

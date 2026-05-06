# SmartLife - Productivity App

SmartLife is a modern Android productivity application built using Jetpack Compose and MVVM architecture.  
The app helps users organize tasks, maintain journals, and improve productivity with a clean and interactive user experience.

---

# Features

## Todo Management
- Create, edit, update, and delete tasks
- Organize tasks by:
  - Day
  - Week
  - Month
  - Year
- Real-time task updates using Flow and StateFlow
- Search functionality for quick task filtering

## Journal System
- Interactive calendar-based journal
- Save daily journal entries
- Add images to journal pages
- Persistent image storage using URI permissions
- Drag and zoom journal images using gesture controls

## Animated Home Screen
- Custom animated typography
- Stroke path text animation using Canvas and PathMeasure
- Dynamic font transitions

## Local Storage
- Room Database integration
- Offline-first architecture
- Reactive UI updates

---

# Tech Stack

- Kotlin
- Jetpack Compose
- Room Database
- MVVM Architecture
- Coroutines
- Flow / StateFlow
- Navigation Compose
- Canvas APIs
- Gesture Detection APIs

---

# Architecture

```text
UI (Compose Screens)
       ↓
ViewModel
       ↓
DAO
       ↓
Room Database
```

---

# Project Structure

```text
com.example.smartlife
│
├── data
│   ├── local
│   │   ├── dao
│   │   ├── entity
│   │   └── appdatabase
│   │
│   └── repository
│
├── navigation
│
├── ui
│   └── screen
│       ├── home
│       ├── todo
│       ├── journal
│       └── profile
│
└── viewmodel
    ├── todoViewModel
    └── journalviewmodel
```

---

# Screenshots

## Home Screen
<img width="250" alt="Home Screen" src="screenshots/home.png"/>

## Todo Screen
<img width="250" alt="Todo Screen" src="screenshots/todo.png"/>

## Journal Calendar
<img width="250" alt="Journal Calendar" src="screenshots/journal.png"/>

## Journal Editor
<img width="250" alt="Journal Editor" src="screenshots/editor.png"/>

---

# Installation

1. Clone the repository

```bash
git clone https://github.com/iamloki143/smart-life-productivity-app.git
```

2. Open the project in Android Studio

3. Sync Gradle

4. Run the application on an emulator or physical device

---

# Key Concepts Used

- Reactive UI with StateFlow
- Local persistence with Room
- MVVM architecture
- Compose Navigation
- Custom Canvas drawing
- Gesture handling
- Image persistence using Storage Access Framework

---

# Future Improvements

- Firebase Authentication
- Cloud Sync
- Push Notifications
- Dark Mode
- Habit Tracker
- Analytics Dashboard
- Hilt Dependency Injection

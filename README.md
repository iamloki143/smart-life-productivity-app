# SmartLife - Modern Productivity App

SmartLife is a powerful Android productivity application built with **Jetpack Compose**, **MVVM Architecture**, and **Room Database**.  
Designed to improve productivity and daily organization, SmartLife combines task management, journaling, reminders, streak tracking, and interactive UI experiences into a single modern application.

The app focuses on:
- Clean UI/UX
- Offline-first architecture
- Reactive state management
- Smooth animations
- Modern Android development practices

---

# Features

## Smart Todo Management
- Create, edit, update, and delete tasks
- Organize tasks by:
  - Daily
  - Weekly
  - Monthly
  - Yearly
- Search and filter tasks instantly
- Real-time updates using `Flow` and `StateFlow`
- Task completion tracking

---

## Reminder & Notification System
- Exact alarm scheduling using `AlarmManager`
- Local notifications with `NotificationManager`
- Tap notifications to reopen the app
- Android 12+ exact alarm support
- Android 13+ notification permission handling
- Background reminder support

---

## Streak Tracking System
- Daily productivity streak tracking
- Automatic streak increment
- Missed-day streak reset system
- SharedPreferences-based lightweight persistence

---

## Interactive Journal System
- Calendar-based journal interface
- Create daily journal entries
- Add and save images permanently
- Persistent URI permission handling
- Gesture support:
  - Drag
  - Zoom
  - Pan images
- Smooth Compose-based UI interactions

---

## Animated Home Screen
- Custom typography animations
- Canvas and PathMeasure based stroke animations
- Dynamic text transitions
- Smooth Compose animations

---

## Offline-First Storage
- Room Database integration
- Local persistent storage
- Reactive database updates
- Fast and lightweight architecture

---

# Tech Stack

## Languages & Frameworks
- Kotlin
- Jetpack Compose

## Architecture
- MVVM Architecture
- Repository Pattern

## Database & Storage
- Room Database
- SharedPreferences
- Storage Access Framework (SAF)

## Async & Reactive
- Coroutines
- Flow
- StateFlow

## Android APIs
- AlarmManager
- NotificationManager
- Canvas APIs
- Gesture Detection APIs

## Navigation
- Navigation Compose

---

# Core Android Concepts Used

- Reactive UI State Management
- Compose State Handling
- Local Database Persistence
- Alarm Scheduling
- Local Notifications
- Broadcast Receivers
- SharedPreferences
- Runtime Permissions
- URI Permission Persistence
- Custom Canvas Drawing
- Gesture Detection
- Offline-first App Design

---

# Architecture

```text
UI Layer (Jetpack Compose)
            ↓
        ViewModel
            ↓
       Repository
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
├── notification
│   ├── AlarmReceiver
│   ├── AlarmScheduler
│   └── NotificationHelper
│
├── navigation
│
├── ui
│   └── screen
│       ├── home
│       ├── todo
│       ├── journal
│       ├── streak
│       └── profile
│
├── utils
│   └── StreakManager
│
└── viewmodel
    ├── todoViewModel
    ├── journalviewmodel
    └── streakviewmodel
```

---

# Screens Included

- Home Screen
- Todo Screen
- Journal Screen
- Reminder System
- Streak Tracker
- Profile Screen

---

# Installation

## 1️.Clone Repository

```bash
git clone https://github.com/iamloki143/smart-life-productivity-app.git
```

---

## 2️.Open in Android Studio

Open the project folder inside:
- Android Studio Hedgehog or newer

---

## 3️.Sync Gradle

Allow Gradle dependencies to download completely.

---

## 4.Run Application

Run the app on:
- Physical Android device
- Android Emulator

Minimum recommended:
- Android 8.0 (API 26)

---

# Required Permissions

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
```

---

# Journal Image Features

SmartLife uses Android's Storage Access Framework to:
- Persist image access permissions
- Prevent image loss after restart
- Support large image handling safely

---

# Reminder System Flow

```text
User Creates Reminder
          ↓
AlarmScheduler
          ↓
AlarmManager
          ↓
AlarmReceiver
          ↓
NotificationHelper
          ↓
Local Notification Displayed
```

---

# Streak System Flow

```text
Task Completed
       ↓
StreakManager
       ↓
Check Last Completion Date
       ↓
Update Streak
       ↓
Save to SharedPreferences
```

---

# Future Improvements

- Firebase Authentication
- Cloud Backup & Sync
- Push Notifications
- Dark Mode
- Habit Tracker
- AI Productivity Assistant
- Analytics Dashboard
- Hilt Dependency Injection
- Widget Support
- Multi-device Sync
- Wear OS Support

---

# Learning Highlights

This project demonstrates:
- Modern Android Development
- Scalable Architecture
- Real-time Reactive UI
- Local Notification Systems
- Alarm Scheduling
- Gesture Handling
- Canvas Drawing
- Persistent Storage
- State Management in Compose

---

# Contributing

Contributions, improvements, and feature suggestions are welcome.

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Open a Pull Request

---

# License

This project is intended for learning and educational purposes.

---

# Developer

Developed by Loki  
Passionate about:
- Android Development
- Cybersecurity
- UI/UX Design
- Modern App Architecture

---

# Support

If you like this project:
- Star the repository
- Share feedback
- Contribute improvements

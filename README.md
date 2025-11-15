# Noted - Modern Android Notes App

A beautiful, feature-rich notes application built with the latest Android development technologies and following clean architecture principles.

## Features

- Create, edit, and delete notes
- Pin important notes to the top
- Search through your notes
- Choose from 8 different note colors
- Smooth animations and transitions
- Material Design 3 UI
- Dark mode support
- Offline-first with local Room database

## Tech Stack

### Architecture
- **Clean Architecture** with three layers:
  - **Data Layer**: Room database, repositories
  - **Domain Layer**: Use cases, business logic
  - **Presentation Layer**: ViewModels, UI states

### Technologies
- **Kotlin**
- **Jetpack Compose** - Modern declarative UI
- **Material Design 3** - Latest Material Design components
- **Room** - Local database
- **Hilt** - Dependency injection
- **Coroutines** & **Flow** - Asynchronous programming
- **Navigation Compose** - Type-safe navigation
- **ViewModel** - State management
- **Gradle Version Catalog** - Dependency management

## Project Structure

```
com.noted.app/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── entity/
│   │   └── NotesDatabase.kt
│   ├── mapper/
│   └── repository/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
├── presentation/
│   ├── notes/
│   ├── note_detail/
│   ├── navigation/
│   ├── theme/
│   └── MainActivity.kt
└── di/
```

## Dependencies

All dependencies are managed through Gradle Version Catalog (`gradle/libs.versions.toml`).

## Author

Ibtehaaj Shahid

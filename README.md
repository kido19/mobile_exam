# Daily Notes & Quote Android App

This Android application was developed for the Android Application Development Lab Exam. It allows users to manage personal notes and get daily motivational quotes.

## Features
- **Daily Motivational Quote**: Fetches and displays a random motivational quote from an online API every time the app loads.
- **Create Notes**: Add new personal notes with a title and content.
- **View Notes**: Scroll through a list of all saved notes directly on the home screen.
- **Edit & Delete Notes**: Tap on any existing note to modify its contents or delete it permanently.
- **Configuration Change Handling**: Rotating the device will not cause the quote to reload or data to be lost, thanks to proper Android Architecture patterns.

## Tech Stack & Architecture
- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose (Modern Declarative UI)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Local Database**: Android Room (SQLite) for storing notes persistently.
- **Networking**: Retrofit & Gson for handling API requests and parsing JSON.
- **Concurrency**: Kotlin Coroutines for safe background processing.

## Project Structure
- `data/local`: Contains Room Database entities and DAO.
- `data/remote`: Contains Retrofit API interface.
- `data/repository`: Abstracts data operations from the UI.
- `ui/screens`: Contains Jetpack Compose layouts (`HomeScreen`, `NoteDetailScreen`).
- `ui/viewmodel`: Manages state and survives configuration changes.

## How to Run
1. Open the project folder in **Android Studio**.
2. Allow Gradle a few moments to sync and download the dependencies (Room, Retrofit, Compose).
3. Connect a physical Android device or start an Android Emulator.
4. Click the green **Run** button to compile and launch the app.

# 4683839Assignment2

NIT3213 Final Assignment Android application.

## Overview
The application contains the three required screens:
1. Login
2. Dashboard
3. Details

It authenticates against the NIT3213 Footscray API, receives a `keypass`, retrieves dashboard data, displays entities in a RecyclerView, and opens a Details screen when an entity is selected.

## API
Base URL: `https://nit3213apinew.onrender.com/`

- `POST /footscray/auth`
- `GET /dashboard/{keypass}`

The application does not hard-code the returned keypass. The keypass received from a successful login is passed to the Dashboard screen and used for the dashboard request.

## Technologies and Architecture
- Kotlin
- XML layouts
- Material 3
- Retrofit 2 + Gson
- OkHttp logging interceptor
- Kotlin Coroutines
- Hilt dependency injection
- MVVM with ViewModels and StateFlow
- RecyclerView
- View Binding
- JUnit and Kotlin Coroutines Test

## Requirements
- Android Studio
- JDK 17
- Android SDK 35 installed
- Android device or emulator running API 24 or higher
- Internet connection

Project SDK configuration:
- `minSdk = 24`
- `targetSdk = 35`
- `compileSdk = 35`
- Android Gradle Plugin `8.7.3`
- Gradle `8.9`

## Project Structure
- `data/api` - Retrofit API interface
- `data/model` - request and response models
- `data/repository` - repository abstraction and network implementation
- `di` - Hilt dependency-injection modules
- `ui/login` - Login Activity and ViewModel
- `ui/dashboard` - Dashboard Activity, ViewModel and RecyclerView adapter
- `ui/details` - Details Activity

## Build and Run
1. Clone or download this repository.
2. Open the project folder in Android Studio.
3. Allow Gradle Sync to complete and download dependencies.
4. Ensure JDK 17 is selected for Gradle in Android Studio.
5. Start an Android emulator (API 24+) or connect a physical Android device.
6. Click **Run > Run 'app'**.
7. Enter your student ID as the username and your first name as the password.

Example login request format:

```json
{
  "username": "4683839",
  "password": "John"
}
```

The password is case-sensitive because the API expects the student's first name exactly as registered.

## Application Flow
### Login
The Login screen validates that both fields are present and that the student ID contains digits only. It then sends the credentials to `/footscray/auth`.

### Dashboard
After a successful login, the returned keypass is passed to the Dashboard. The Dashboard calls `/dashboard/{keypass}` and displays the returned entities in a RecyclerView. The description field is intentionally excluded from the Dashboard summary.

### Details
Tapping an entity opens the Details screen, which displays all fields for that entity, including its description.

## Error Handling
The application handles:
- Missing login fields
- Invalid student ID format
- Invalid credentials
- HTTP/server errors
- Network connection errors
- Missing keypass
- Dashboard loading failures

## Unit Tests
Unit tests are included for critical ViewModel behaviour, including:
- Successful login
- Login validation
- Successful dashboard loading
- Missing dashboard keypass validation

Run tests from Android Studio by right-clicking the test directory and selecting **Run Tests**, or run:

```bash
./gradlew test
```

## Internet Permission
The app requires the following permission, already declared in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Author
Student ID: **4683839**  
Unit: **NIT3213 - Android Application Development**

# Appium Automation Tests

This module contains the automated tests for the Oinkoin application, built using Appium, Java, and Cucumber.

## Prerequisites

Before running the tests, ensure you have the following set up:

1.  **Android Emulator**: An emulator should be running (default configuration expects `emulator-5554`).
2.  **Appium Server**: Must be running on `http://127.0.0.1:4723`, usually set by default.
3.  **Application APK**: The target APK must be built and available at the expected path relative to the project root.

## Setup & Execution

### 1. Build the Application

The tests look for the APK in the main project build folder. From the root of the `oinkoin` repository, build the debug APK (Recommended Pro flavor):

```bash
flutter build apk --debug --flavor pro
```

### 2. Start Appium

Start the Appium server in a separate terminal:

```bash
appium
```

### 3. Run Tests

Navigate to the `appium` directory and execute the tests using Gradle:

```bash
cd appium
./gradlew clean test
```

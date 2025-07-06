# Face Contours App

An Android application for real-time face contour detection and puting on sunglasses mask

## Features
*Real-time face contour detection*
- Onboarding screen on first launch
- Draw points and contours over detected faces
- Built using modern Android technologies:
  - Jetpack Compose
  - CameraX
  - Hilt
  - Kotlin Coroutines
  - DataStore
  - Ml Google Face Detection

## Build Instructions

### 1. Clone the repository
- git clone https://github.com/ChetireBanana/face-contours.git
- [download link] (https://drive.google.com/file/d/1kZCiloKiRaleanHnmWE0UkXhE5E2W1Ce/view?usp=sharing)

### 2. Download the APK
You can download the latest APK from this link:


### 3. Install the APK
Enable installation from unknown sources in your device settings.
Open the downloaded APK file to install the app.

## Usage
After installing the app, follow these steps to use it:

### Grant Camera Permission:
The app requires access to the device camera for real-time face contour detection. You will be prompted to allow camera access on first launch. Please allow it to ensure proper functionality.

### Onboarding Screen:
On the very first launch, the app will display an onboarding screen explaining its features.

### Face Contour Detection:
Point your camera at faces. The app will detect face and show a green border round the screen. Push the button "Show contours" to draw contour on face in real time.

### Sunglasses Mask:
Push the button "Show sunglasses" to show a sunglasses mask on detected face.

## Running Tests
This project includes unit tests for key components.

### To run tests:
    Use Android Studio’s Run Tests feature
    Or run tests from the command line with: ./gradlew test

## Technologies Used
 - Kotlin
 - Jetpack Compose
 - CameraX
 - Hilt (Dependency Injection)w
 - Kotlin Coroutines
 - DataStore (Preferences)
 - [Google ML Kit Face Detection] (https://developers.google.com/ml-kit/vision/face-detection?hl=ru)

## Contact

Developed by Sergey Veretennikov.

GitHub: [https://github.com/ChetireBanana](https://github.com/ChetireBanana)  
Email: mr.veretennikov.s@gmail.com

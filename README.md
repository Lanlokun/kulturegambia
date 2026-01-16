# Kultur Gambia Android App


![App Screens](screenshots/kulturemockup.png)

Kultur Gambia is an open-source Android application built with Jetpack Compose that showcases Gambian culture, heritage, events, and places in a modern, mobile-first experience. The app is designed to document and preserve cultural stories while making them easily accessible to both locals and a global audience.

The project emphasizes simplicity, accessibility, and community contribution, serving both as a cultural platform and a practical example of modern Android development.

## Tech Stack

Kultur Gambia is built entirely using modern Android tools and best practices. The application is written in Kotlin and uses Jetpack Compose for declarative UI development. Material 3 is used for theming and UI components to ensure a consistent and modern design language across the app. Navigation Compose handles in-app navigation, enabling a single-activity architecture with multiple composable screens.

The build system is powered by Gradle using Kotlin DSL, and Coil is used for efficient image loading and rendering within Compose.

## Features

The app allows users to explore curated cultural content across categories such as music, dance, food, clothing, festivals, history, and language. Each cultural item includes descriptive content and imagery to provide context and depth.

Users can save their favorite cultural items for quick access later through a dedicated Saved screen. The app also includes screens for cultural places and events, helping users discover locations and activities connected to Gambian heritage.

Community participation is supported through a story submission feature, allowing users to contribute their own cultural stories directly from the app.

## Architecture

Kultur Gambia follows a single-activity architecture with all UI rendered using Jetpack Compose. Application state is managed using Compose state utilities such as `remember` and `rememberSaveable`, while side effects and asynchronous tasks are handled using Kotlin coroutines.

Data is currently sourced from local JSON assets and accessed through repository classes. This design makes it easy to later introduce remote data sources or persistent storage without major architectural changes.

## Usage

When the app is launched, users are greeted with a simple splash screen before being taken to the Home screen. From the Home screen, users can browse cultural stories and explore featured content.

The bottom navigation allows users to switch between Home, Explore, Map, Events, and Saved screens. The Explore section enables browsing by categories, while the Map and Events sections provide location-based and event-related cultural information.

Users can tap on any cultural item to view detailed information. Items they find interesting can be saved and later accessed from the Saved screen. The app also allows users to submit new cultural stories using the Submit Story feature, where they can add text content and upload an image.


## Installation

To set up and run the Kultur Gambia Android app locally, follow the steps below.

First, ensure that you have **Android Studio** installed on your machine. It is recommended to use the latest stable version with full Jetpack Compose support enabled. You will also need an Android device or emulator running **Android 8.0 (API level 26) or higher**.

Clone the repository to your local machine using Git:

```bash
git clone https://github.com/Lanlokun/kulturegambia.git
cd kulturegambia
```

Open the project in Android Studio. Once opened, allow Android Studio to complete the Gradle sync process. This may take a few minutes on the first run as dependencies are downloaded.

After the project has synced successfully, select a target device. You can either start an Android emulator from Android Studio’s Device Manager or connect a physical Android device with USB debugging enabled.

Finally, click the Run button in Android Studio or use the Shift + F10 shortcut to build and launch the app on the selected device.

No additional configuration is required, as the app currently uses local JSON data sources and does not depend on external services.


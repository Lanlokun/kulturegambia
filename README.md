# Kultur Gambia Android App

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

## Getting Started

To run the project locally, clone the repository and open it in Android Studio. Ensure that you are using a recent version of Android Studio with Jetpack Compose support enabled. Sync the Gradle project and run the app on an emulator or physical Android device running Android 8.0 or higher.

No additional setup is required at this stage, as the app uses local data sources.

## Contribution

Contributions are welcome. You can contribute by improving the UI, adding new features, fixing bugs, enhancing documentation, or contributing cultural content ideas. To contribute, fork the repository, create a feature branch, and submit a pull request with a clear description of your changes.

All contributions should follow clean code practices and align with the existing project structure and design principles.

## Status

Work in progress. The project is actively being developed, with plans to expand features, improve user-submitted content handling, and introduce more advanced search, filtering, and data persistence capabilities.

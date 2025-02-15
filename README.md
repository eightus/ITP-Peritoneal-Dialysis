﻿# PDBuddy Application Documentation

## Table of Contents
1. [Introduction](#introduction)
2. [Project Structure](#project-structure)
3. [Setting Up a New Screen](#setting-up-a-new-screen)
4. [Navigation](#navigation)
5. [ViewModel and Repository Interaction](#viewmodel-and-repository-interaction)
6. [Dependency Injection with Hilt](#dependency-injection-with-hilt)
7. [Running the App](#running-the-app)

## Introduction
PDBuddy is an Android application designed to assist peritoneal dialysis patients. This documentation provides an overview of the project structure, guides on creating new screens, details on navigation, and explains how ViewModel and Repository interact. It also covers setting up dependency injection using Hilt.

## Project Structure
The project is organized into the following directories:
```
├───app
│   ├───src
│   │   ├───main
│   │   │   ├───java/com/itp/pdbuddy
│   │   │   │   ├───data/remote       # Remote data source
│   │   │   │   ├───data/repository   # Repository pattern
│   │   │   │   ├───navigation        # Navigation configuration
│   │   │   │   ├───ui/screen         # UI screens
│   │   │   │   ├───ui/theme          # UI theming
│   │   │   │   ├───ui/viewmodel      # ViewModels
│   │   │   │   └───utils             # Utility classes
│   │   │   └───res                   # Resource files (layouts, strings, etc.)
```

## Setting Up a New Screen
To create a new screen in the PDBuddy app, follow these steps:

1. **Create the Screen Composable**:
    - Navigate to `src/main/java/com/itp/pdbuddy/ui/screen`.
    - Create a new Kotlin file for your screen, e.g., `NewScreen.kt`.
    - Define your screen composable function:

```kotlin
@Composable
fun NewScreen(navController: NavHostController) {
    // UI components here
}
```

2. **Add Navigation**:
    - Update the navigation configuration in `src/main/java/com/itp/pdbuddy/navigation/NavigationConfig.kt` to include your new screen:

```kotlin
val navItems = listOf(
    // Existing screens
    NavItem(
        route = "new_screen",
        title = "New Screen",
        icon = Icons.Default.YourIcon
    ) { navController -> NewScreen(navController) }
)
```

## Navigation
Navigation in PDBuddy is handled using Jetpack Compose's Navigation component. Here's how it's set up:

1. **Navigation Configuration**:
    - The navigation items are configured in `NavigationConfig.kt`:

```kotlin
object NavigationConfig {
    val navItems = listOf(
        NavItem(
            route = "home",
            title = "Home",
            icon = Icons.Default.Home
        ) { navController -> HomeScreen(navController) },
        // Add other screens similarly
    )
}
```

2. **Setting Up NavHost**:
    - The `AppNavigation` composable in `AppNavigation.kt` sets up the `NavHost`:

```kotlin
@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        NavigationConfig.navItems.forEach { navItem ->
            composable(navItem.route) { navItem.screen(navController) }
        }
    }
}
```

## ViewModel and Repository Interaction
The app follows the MVVM architecture pattern. Here's how the components interact:

1. **ViewModel**:
    - Located in `src/main/java/com/itp/pdbuddy/ui/viewmodel`.
    - Manages UI-related data and interacts with the repository.

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    // ViewModel logic
}
```

2. **Repository**:
    - Located in `src/main/java/com/itp/pdbuddy/data/repository`.
    - Acts as a single source of truth for data. Manages data operations and provides data to ViewModel.

```kotlin
class UserRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    // Repository methods
}
```

## Dependency Injection with Hilt
Hilt is used for dependency injection throughout the app:

1. **Setup Hilt**:
    - Ensure `Hilt` is added in the `build.gradle` file.
    - Annotate `Application` class with `@HiltAndroidApp`.

2. **Inject Dependencies**:
    - Use `@Inject` annotation in constructors of ViewModel and Repository.
    - Annotate ViewModels with `@HiltViewModel`.

## Running the App
Running The Backend:
1. `cd python-graph`
2. `python -m venv venv`
3. `python ./venv/scripts/activate`
4. `pip install -r requirements.txt`
5. Drop the `credentials.json` in the same folder as the `main.py`
6. `python main.py`

To run the app:
1. Follow [Add Firebase to Android Project](https://firebase.google.com/docs/android/setup) tutorial to configure Firebase with Android Studio
2. **Open Android Studio**.
3. Configure `app/build.gradle.kts` to point to your backend ip address
```
buildTypes {
    debug {
      buildConfigField("String", "BASE_URL", "\"http://<IP_ADDRESS>:8000/\"")
    }
    release {
      buildConfigField("String", "BASE_URL", "\"http://192.168.18.20:8000/\"")
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
  }
} 
```
4. **Build the Project**: `Build > Make Project`.
5. **Run the App**: `Run > Run 'app'`.

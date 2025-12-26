rootProject.name = "trainer"

pluginManagement {
    repositories {
        maven { url = uri("${System.getenv("ANDROID_HOME")}/extras/android/m2repository") }
        maven { url = uri("${System.getenv("ANDROID_HOME")}/extras/google/m2repository") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven { url = uri("${System.getenv("ANDROID_HOME")}/extras/android/m2repository") }
        maven { url = uri("${System.getenv("ANDROID_HOME")}/extras/google/m2repository") }
        google()
        mavenCentral()
    }
}

include(":shared")
include(":androidApp")

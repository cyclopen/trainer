rootProject.name = "trainer"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":shared")
// Note: androidApp requires Android Gradle Plugin which needs network access to dl.google.com
// Uncomment when building with Android support:
// include(":androidApp")

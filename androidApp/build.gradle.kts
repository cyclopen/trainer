plugins {
    kotlin("android")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.cyclopen.trainer.android"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.cyclopen.trainer.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    
    buildFeatures {
        compose = true
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("io.insert-koin:koin-android:3.5.6")
}

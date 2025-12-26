plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }
    
    macosArm64()
    macosX64()
    mingwX64()
    linuxX64()
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                
                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
                
                // Koin for DI
                implementation("io.insert-koin:koin-core:3.5.6")
                implementation("io.insert-koin:koin-compose:1.1.5")
            }
        }
        
        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.9.2")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
                
                // Kable for Bluetooth
                implementation("com.juul.kable:core:0.34.0")
            }
        }
        
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                // Kable for Bluetooth
                implementation("com.juul.kable:core:0.34.0")
            }
        }
        
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        
        val macosMain by creating {
            dependsOn(commonMain)
            dependencies {
                // Kable for Bluetooth
                implementation("com.juul.kable:core:0.34.0")
            }
        }
        
        val macosArm64Main by getting {
            dependsOn(macosMain)
        }
        
        val macosX64Main by getting {
            dependsOn(macosMain)
        }
        
        val desktopMain by getting {
            dependsOn(commonMain)
        }
        
        val mingwMain by creating {
            dependsOn(commonMain)
        }
        
        val mingwX64Main by getting {
            dependsOn(mingwMain)
        }
        
        val linuxMain by creating {
            dependsOn(commonMain)
        }
        
        val linuxX64Main by getting {
            dependsOn(linuxMain)
        }
    }
}

android {
    namespace = "com.cyclopen.trainer"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 24
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
}

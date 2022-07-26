plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.sk.shotsapp"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.sk.shotsapp"
}

dependencies {

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["compose_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0")
    implementation("androidx.activity:activity-compose:1.5.0")

    //firebase-auth
    implementation("com.google.firebase:firebase-auth:21.0.6")
    implementation("com.google.android.gms:play-services-auth:20.2.0")
    implementation("com.google.firebase:firebase-auth-ktx:21.0.6")
    implementation(platform("com.google.firebase:firebase-bom:30.3.0"))
    implementation("com.google.firebase:firebase-firestore-ktx:24.2.1")


    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")

    //navigation
    implementation("androidx.navigation:navigation-compose:2.5.0")

    implementation("com.google.dagger:hilt-android:2.43")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0") // Inside Nav graph DI to work

    implementation("com.google.maps.android:maps-compose:2.5.2")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("androidx.compose.foundation:foundation:1.3.0-alpha01")

    implementation("com.google.accompanist:accompanist-permissions:0.24.13-rc")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.17.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.13-rc")
}
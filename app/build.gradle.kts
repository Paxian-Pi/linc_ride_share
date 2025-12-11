plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.ride_sharing.linc"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ride_sharing.linc"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.google.maps.compose)
    implementation(libs.google.maps.sdk)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.compose.material3) // Add Coil dependency for Compose
    implementation("androidx.compose.material:material:1.5.0") // Add Material library for swipeable
    implementation("androidx.compose.material:material-icons-core:1.5.0") // Add Material icons core
    implementation("androidx.compose.material:material-icons-extended:1.5.0") // Add Material icons extended
    implementation(libs.compose.material)
    implementation(libs.androidx.compose.material)
    implementation("androidx.compose.foundation:foundation:1.6.0")
    implementation(libs.androidx.compose.foundation) // Add Compose Foundation library
    implementation("com.google.maps.android:android-maps-utils:2.3.0") // Add Google Maps Android library
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
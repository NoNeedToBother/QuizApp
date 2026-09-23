plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.kpfu.itis.quiz.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "ru.kpfu.itis.quiz.android"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.1"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.coil)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.material.iconsExtended)
    implementation(libs.androidx.animation.graphics.android)
    implementation(libs.androidx.compose.navigation)

    implementation(libs.bundles.paging.multiplatform)

    implementation(libs.kotlinx.datetime)

    implementation(libs.mvi.orbit.compose)

    implementation(libs.bundles.koin.common)
    implementation(libs.koin.android)

    debugImplementation(libs.compose.ui.tooling)
}
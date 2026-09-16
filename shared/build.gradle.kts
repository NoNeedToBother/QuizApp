import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    id("io.github.skeptick.libres")
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_21)
                }
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    cocoapods {
        summary = "QuizKMP app shared module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.ktorClientCommon)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            implementation(libs.cryptography.core)
            implementation(libs.room.runtime)
            implementation(libs.bundles.sqlite)
            implementation(libs.bundles.paging.multiplatform)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.serialization)

            implementation(libs.bundles.koin.common)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.mvi.orbit.core)
            implementation(libs.mvi.orbit.viewmodel)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.okhttp3.logging.interceptor)
            implementation(libs.cryptography.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
            implementation(libs.cryptography.ios)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "ru.kpfu.itis.quiz"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}

tasks {
    configureEach {
        if (this.name.contains("kspDebugKotlinAndroid")) {
            this.dependsOn("libresGenerateStrings")
            this.dependsOn("libresGenerateResources")
        }
    }
}

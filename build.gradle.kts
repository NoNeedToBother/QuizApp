plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.kotlinCocoapods).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.room).apply(false)
}

buildscript {
    dependencies {
        classpath("io.github.skeptick.libres:gradle-plugin:1.2.4")
    }
}

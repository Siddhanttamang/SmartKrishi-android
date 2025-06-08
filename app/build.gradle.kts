plugins {
    alias(libs.plugins.android.application)
//    kotlin("kapt")
}

android {
    namespace = "com.example.smartkrishi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.smartkrishi"
        minSdk = 26
        targetSdk = 35
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
}

dependencies {
    implementation(libs.onnxruntime)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.sessions)
    testImplementation(libs.junit)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.lottie)
    implementation(libs.glide)
//    kapt(libs.compiler)


    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}


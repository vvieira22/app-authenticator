import java.util.Properties

plugins {
    id("com.google.gms.google-services")//firebase
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.vvieira.appauthenticator"
    compileSdk = 34
    val localProperties = Properties()
    localProperties.load(project.rootProject.file("local.properties").inputStream())

    defaultConfig {
        buildConfigField("String", "FACEBOOK_APP_ID", "\"${localProperties.getProperty("FACEBOOK_APP_ID")}\"")
        buildConfigField("String", "FB_LOGIN_PROTOCOL_SCHEME", "\"${localProperties.getProperty("FB_LOGIN_PROTOCOL_SCHEME")}\"")
        buildConfigField("String", "FACEBOOK_CLIENT_TOKEN", "\"${localProperties.getProperty("FACEBOOK_CLIENT_TOKEN")}\"")

        applicationId = "com.vvieira.appauthenticator"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildFeatures {
            buildConfig = true
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            resValue("string", "FACEBOOK_APP_ID", "\"${localProperties["FACEBOOK_APP_ID"]}\"")
            resValue("string", "FB_LOGIN_PROTOCOL_SCHEME", "\"${localProperties["FB_LOGIN_PROTOCOL_SCHEME"]}\"")
            resValue("string", "FACEBOOK_CLIENT_TOKEN", "\"${localProperties["FACEBOOK_CLIENT_TOKEN"]}\"")
        }
        release {
            buildConfigField("String", "FACEBOOK_APP_ID", "\"${localProperties["FACEBOOK_APP_ID"]}\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
//    debugImplementation(libs.squareup.leakcanary.android)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    //firebase important
    implementation(libs.firebase.bom)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
//    implementation("com.facebook.android:facebook-login:latest.release")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation(libs.converter.gson)
    implementation(libs.gson)
//    implementation(libs.firebase.crashlytics)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
kapt {
    javacOptions {
        // These options are normally set automatically via the Hilt Gradle plugin, but we
        // set them manually to workaround a bug in the Kotlin 1.5.20
        option("-Adagger.fastInit=ENABLED")
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}
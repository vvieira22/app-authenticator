import java.util.Properties

plugins {
    id("com.google.gms.google-services")//firebase
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
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
    //firebase important
    implementation(libs.firebase.bom)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation("com.facebook.android:facebook-login:latest.release")

//    implementation(libs.firebase.crashlytics)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
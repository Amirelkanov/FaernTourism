plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.amel.faerntourism"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.amel.faerntourism"
        minSdk = 26
        targetSdk = 34
        versionCode = 8
        versionName = "1.2.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.android.maps.utils)
    implementation(libs.material3)

    implementation(libs.firebase.firestore.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.jsoup.jsoup)
    implementation(libs.ramani.maplibre)

    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.messaging)

    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.converter.scalars)

    api(libs.moko.permissions)
    api(libs.moko.permissions.compose)

    implementation(platform(libs.rustore.bom))
    implementation(libs.rustore.appupdate)
    implementation(libs.rustore.review)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.googleid)
    implementation(libs.firebase.vertexai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
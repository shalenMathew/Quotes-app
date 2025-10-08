plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
}

android {
    namespace = "com.shalenmathew.quotesapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.shalenmathew.quotesapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 31
        versionName = "3.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }

        buildTypes {
            release {
                buildConfigField("Boolean", "ENABLE_ANALYTICS", "true")
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }

            debug {
                buildConfigField("Boolean", "ENABLE_ANALYTICS", "false")
                isDebuggable = true
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures{
        buildConfig = true
    }

    kapt {
        correctErrorTypes = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.animation.core)
    testImplementation(libs.junit)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Coil
    implementation ("io.coil-kt:coil-compose:2.7.0")

    implementation ("io.github.aghajari:LazySwipeCards:1.0.1")

    //Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.56.2")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0") // hilt for nav
    implementation("androidx.hilt:hilt-work:1.2.0")

//    ksp ("com.google.dagger:hilt-android-compiler:2.54")
//    kapt("androidx.hilt:hilt-compiler:1.2.0")

    kapt("androidx.hilt:hilt-compiler:1.2.0")
    kapt("com.google.dagger:hilt-compiler:2.56.2")

//    kapt ("com.google.dagger:hilt-android-compiler:2.54")
//    kapt ("com.google.dagger:hilt-compiler:2.54")


    //Room Database
    implementation ("androidx.room:room-runtime:2.7.2")
    ksp ("androidx.room:room-compiler:2.7.2")
    implementation ("androidx.room:room-ktx:2.7.2")


    //Retrofit2
    implementation ("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation ("com.squareup.retrofit2:retrofit:3.0.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0")
    implementation ("com.squareup.retrofit2:converter-scalars:3.0.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0")


    // Glance Widget
    implementation ("androidx.glance:glance-appwidget:1.1.1")


    // work manager
    implementation ("androidx.work:work-runtime-ktx:2.10.2")


    implementation ("com.google.android.material:material:1.12.0")


    // test
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.8.3")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.8.3")
    // mockito
    testImplementation("org.mockito:mockito-core:5.18.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.17")
// Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
// this below kotlin imports are necessary for the assertEquals to work
    testImplementation ("org.jetbrains.kotlin:kotlin-test")
    testImplementation ("org.jetbrains.kotlin:kotlin-test-junit")


    // haze effect
    implementation("dev.chrisbanes.haze:haze-jetpack-compose:0.4.1")

    // hypnotic canvas
    implementation("com.mikepenz.hypnoticcanvas:hypnoticcanvas:0.4.1")

    //color picker
    implementation("com.github.skydoves:colorpicker-compose:1.1.2")



}
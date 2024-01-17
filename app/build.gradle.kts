plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.prjctmobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.prjctmobile"
        minSdk = 26
        targetSdk = 33
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.appcompat:appcompat-resources:1.6.1")
    implementation("androidx.emoji2:emoji2-views-helper:1.4.0")
    implementation("androidx.emoji2:emoji2:1.4.0")
    implementation("androidx.lifecycle:lifecycle-process:2.6.2")
    implementation(platform("com.google.firebase:firebase-bom:28.0.3"))

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")


    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.palette:palette:1.0.0")
    implementation ("com.squareup.picasso:picasso:2.8")


    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("com.google.dagger:hilt-android:2.42")

    implementation("com.google.android.material:material:1.5.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation ("androidx.fragment:fragment-ktx:1.4.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    implementation("androidx.recyclerview:recyclerview-selection:1.2.0-alpha01")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
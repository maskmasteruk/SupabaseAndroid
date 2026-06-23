plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
}

android {
    namespace = "com.maskmasteruk.supabaseandroidsdk"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.auth.kt)
    implementation(libs.storage.kt)
    implementation(libs.supabase.kt)
    implementation(libs.postgrest.kt)
    implementation(libs.ktor.client.okhttp)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.maskmasteruk"
                artifactId = "supabaseandroid"
                version = "1.0.0"

                from(components["release"])
            }
        }
    }
}

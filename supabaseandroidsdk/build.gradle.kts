plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
}

android {
    namespace = "com.maskmasteruk.supabaseandroidsdk"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}

android {
    namespace = "com.maskmasteruk.supabasesdk"

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }
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
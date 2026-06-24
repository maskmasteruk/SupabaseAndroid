plugins {
    alias(libs.plugins.android.library)
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.vanniktech.maven.publish") version "0.37.0"
    signing
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
}

mavenPublishing {
    coordinates("io.github.maskmasteruk", "supabaseandroid", "1.0.0")

    pom {
        name.set("Supabase Android SDK")
        description.set("Android SDK for Supabase integration")
        url.set("https://github.com/maskmasteruk/SupabaseAndroid")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("maskmasteruk")
                name.set("Udhayakrishna K G")
                email.set("k.g.u2006@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:github.com/maskmasteruk/SupabaseAndroid.git")
            developerConnection.set("scm:git:ssh://github.com/maskmasteruk/SupabaseAndroid.git")
            url.set("https://github.com/maskmasteruk/SupabaseAndroid/tree/main")
        }
    }
    
    signAllPublications()
    publishToMavenCentral()
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

plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
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

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
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
                groupId = "com.maskmasteruk.supabaseandroid"
                artifactId = "supabaseandroid"
                version = "1.0.0"

                from(components["release"])

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
                            name.set("Mask Master UK")
                            email.set("info@maskmaster.uk")
                        }
                    }
                    scm {
                        connection.set("scm:git:github.com/maskmasteruk/SupabaseAndroid.git")
                        developerConnection.set("scm:git:ssh://github.com/maskmasteruk/SupabaseAndroid.git")
                        url.set("https://github.com/maskmasteruk/SupabaseAndroid/tree/main")
                    }
                }
            }
        }
        repositories {
            maven {
                name = "OSSRH"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.findProperty("ossrhUsername")?.toString() ?: ""
                    password = project.findProperty("ossrhPassword")?.toString() ?: ""
                }
            }
        }
    }

    signing {
        sign(publishing.publications["release"])
    }
}

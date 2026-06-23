plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm") version "2.2.10"
    `java-gradle-plugin`
    `maven-publish`
    signing
}

group = "com.maskmasteruk.supabaseandroid"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("Supabase Android Gradle Plugin")
                description.set("Gradle plugin for Supabase Android integration")
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
    sign(publishing.publications["maven"])
}

dependencies {
    implementation("org.json:json:20260522")

    compileOnly("com.android.tools.build:gradle:9.2.1")

}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
gradlePlugin {
    plugins {
        create("supabaseAndroid") {
            id = "com.maskmasteruk.supabaseandroid"
            implementationClass =
                "com.maskmasteruk.supabase_plugin.SupabasePlugin"
        }
    }
}


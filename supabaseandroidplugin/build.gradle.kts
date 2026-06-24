plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm") version "2.2.10"
    id("com.vanniktech.maven.publish") version "0.37.0"
    `java-gradle-plugin`
    signing
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

mavenPublishing {
    coordinates("io.github.maskmasteruk", "supabaseandroidplugin", "1.0.0")

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
            id = "io.github.maskmasteruk.supabaseandroid"
            implementationClass = "com.maskmasteruk.supabase_plugin.SupabasePlugin"
        }
    }
}

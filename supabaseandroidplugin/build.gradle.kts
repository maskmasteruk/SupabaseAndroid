plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm") version "2.4.0"
    `java-gradle-plugin`
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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


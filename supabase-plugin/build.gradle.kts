plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `java-gradle-plugin`
    `maven-publish`
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.json)
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
                "com.maskmasteruk.plugin.SupabasePlugin"
        }
    }
}


package com.maskmasteruk.supabase_plugin

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.json.JSONObject

/**
 * A Gradle plugin that automates the configuration of Supabase credentials for Android applications.
 *
 * This plugin reads a `supabase-config.json` file from the application module's root directory
 * and injects the `supabase_url` and `supabase_anon_key` into the project's `BuildConfig`.
 */
class SupabasePlugin : Plugin<Project> {

    /**
     * Applies the plugin to the given project.
     *
     * @param project The Gradle project to which the plugin is applied.
     * @throws RuntimeException if `supabase-config.json` is missing or invalid.
     */
    override fun apply(project: Project) {

        project.plugins.withId("com.android.application") {

            val configFile = project.file("supabase-config.json")

            if (!configFile.exists()) {
                throw RuntimeException(
                    """
                    supabase-config.json not found.

                    Place the file in:

                    app/supabase-config.json

                    Example:

                    {
                      "supabase_url": "https://your-project.supabase.co",
                      "supabase_anon_key": "your-anon-key"
                    }
                    """.trimIndent()
                )
            }

            val json = JSONObject(configFile.readText())

            val supabaseUrl =
                json.optString("supabase_url")

            val supabaseAnonKey =
                json.optString("supabase_anon_key")

            require(supabaseUrl.isNotBlank()) {
                "supabase_url is missing in supabase-config.json"
            }

            require(supabaseAnonKey.isNotBlank()) {
                "supabase_anon_key is missing in supabase-config.json"
            }

            val android =
                project.extensions.getByType(
                    ApplicationExtension::class.java
                )

            // Ensure BuildConfig generation is enabled
            android.buildFeatures.buildConfig = true

            android.defaultConfig {

                // Inject Supabase URL into BuildConfig
                buildConfigField(
                    "String",
                    "SUPABASE_URL",
                    "\"$supabaseUrl\""
                )

                // Inject Supabase Anon Key into BuildConfig
                buildConfigField(
                    "String",
                    "SUPABASE_ANON_KEY",
                    "\"$supabaseAnonKey\""
                )
            }

            project.logger.lifecycle(
                "Supabase config loaded successfully: Injected SUPABASE_URL and SUPABASE_ANON_KEY into BuildConfig"
            )
        }
    }
}
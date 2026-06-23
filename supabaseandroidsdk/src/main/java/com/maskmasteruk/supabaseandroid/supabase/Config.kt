package com.maskmasteruk.supabaseandroid.supabase

/**
 * Configuration object to store Supabase credentials.
 * These should be initialized via [AuthManager.initialize].
 */
object Config {
    /**
     * The Supabase project URL.
     */
    lateinit var SUPABASE_URL: String

    /**
     * The Supabase anonymous API key.
     */
    lateinit var SUPABASE_ANON_KEY: String
}
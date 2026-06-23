package com.maskmasteruk.supabaseandroid.supabase

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SettingsSessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage

/**
 * Singleton manager for handling Supabase client initialization and authentication operations.
 */
object AuthManager {
    private var supabase: SupabaseClient? = null
    private lateinit var appContext: Context

    /**
     * Initializes the Supabase client with the provided credentials and context.
     * @param context Application context.
     * @param supabaseUrl Your Supabase project URL.
     * @param supabaseAnonKey Your Supabase anonymous API key.
     */
    @JvmStatic
    fun initialize(context: Context, supabaseUrl: String, supabaseAnonKey: String) {
        appContext = context.applicationContext
        Config.SUPABASE_URL = supabaseUrl
        Config.SUPABASE_ANON_KEY = supabaseAnonKey
        getClient()
    }

    /**
     * Lazily creates or returns the [SupabaseClient].
     * @return The initialized [SupabaseClient].
     */
    fun getClient(): SupabaseClient {
        if (supabase == null) {
            supabase = createSupabaseClient(
                supabaseUrl = Config.SUPABASE_URL,
                supabaseKey = Config.SUPABASE_ANON_KEY
            ) {
                install(Postgrest)
                install(Storage)
                install(Auth) {
                    sessionManager = SettingsSessionManager(
                        SharedPreferencesSettings(
                            appContext.applicationContext.getSharedPreferences(
                                "supabase-auth",
                                Context.MODE_PRIVATE
                            )
                        )
                    )
                    alwaysAutoRefresh = true
                    autoLoadFromStorage = true
                }
            }
        }
        return supabase!!
    }

    /**
     * Checks if a valid session exists.
     * @param context Android context.
     * @return True if a session is valid, false otherwise.
     */
    suspend fun validateSession(context: Context): Boolean {
        return try {
            getClient().auth.awaitInitialization()
            val session = getClient().auth.currentSessionOrNull()
            session != null
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Checks if a valid session exists.
     * @return True if a session is valid, false otherwise.
     */
    suspend fun validateSession(): Boolean {
        return try {
            getClient().auth.awaitInitialization()
            val session = getClient().auth.currentSessionOrNull()
            session != null
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Core sign-up logic.
     * @param email User email.
     * @param password User password.
     */
    suspend fun signUp(email: String, password: String) {
        getClient().auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    /**
     * Core sign-in logic.
     * @param email User email.
     * @param password User password.
     */
    suspend fun signIn(email: String, password: String) {
        getClient().auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    /**
     * Gets the currently logged-in user's information.
     * @return [UserInfo] if logged in, null otherwise.
     */
    @JvmStatic
    fun getCurrentUser(): UserInfo? {
        return getClient().auth.currentUserOrNull()
    }

    /**
     * Core sign-out logic.
     * @param context Android context.
     */
    suspend fun signOut(context: Context) {
        try {
            getClient().auth.signOut()
        } catch (e: Exception) {
        }
    }

    /**
     * Core sign-out logic.
     */
    suspend fun signOut() {
        try {
            getClient().auth.signOut()
        } catch (e: Exception) {
        }
    }

    /**
     * Deletes the user account by calling a stored procedure and clearing the session.
     */
    suspend fun deleteUser() {
        getClient().postgrest.rpc(function = "delete_user_account")
        getClient().auth.clearSession()
    }
}
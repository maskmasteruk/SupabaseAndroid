package com.maskmasteruk.supabaseandroidsdk.supabase

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SettingsSessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage

object AuthManager {
    private var supabase: io.github.jan.supabase.SupabaseClient? = null
    private lateinit var appContext: Context

    @JvmStatic
    fun initialize(context: Context, supabaseUrl: String, supabaseAnonKey: String) {
        appContext = context.applicationContext
        Config.SUPABASE_URL = supabaseUrl
        Config.SUPABASE_ANON_KEY = supabaseAnonKey
        getClient()
    }

    fun getClient(): io.github.jan.supabase.SupabaseClient {
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

    suspend fun validateSession(context: Context): Boolean {
        return try {
            getClient().auth.awaitInitialization()
            val session = getClient().auth.currentSessionOrNull()
            session != null
        } catch (e: Exception) {
            false
        }
    }

    suspend fun validateSession(): Boolean {
        return try {
            getClient().auth.awaitInitialization()
            val session = getClient().auth.currentSessionOrNull()
            session != null
        } catch (e: Exception) {
            false
        }
    }

    suspend fun signUp(email: String, password: String) {
        getClient().auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signIn(email: String, password: String) {
        getClient().auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    @JvmStatic
    fun getCurrentUser(): UserInfo? {
        return getClient().auth.currentUserOrNull()
    }

    suspend fun signOut(context: Context) {
        try {
            getClient().auth.signOut()
        } catch (e: Exception) {
        }
    }

    suspend fun signOut() {
        try {
            getClient().auth.signOut()
        } catch (e: Exception) {
        }
    }

    suspend fun deleteUser() {
        getClient().postgrest.rpc(function = "delete_user_account")
        getClient().auth.clearSession()
    }
}
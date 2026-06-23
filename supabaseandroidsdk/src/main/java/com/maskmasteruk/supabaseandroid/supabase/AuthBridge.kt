package com.maskmasteruk.supabaseandroid.supabase

import android.content.Context
import com.maskmasteruk.supabaseandroid.CONSTANTS.AUTH_ERROR_MESSAGES
import com.maskmasteruk.supabaseandroid.objects.Error
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A bridge between the asynchronous Supabase Auth operations and the UI layer.
 * Provides callbacks for Java/Android interoperability.
 */
object AuthBridge {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    /**
     * Callback interface for authentication operations.
     */
    interface AuthCallback {
        /**
         * Called when the authentication operation succeeds.
         * @param user Information about the authenticated user, or null if signed out.
         */
        fun onSuccess(user: UserInfo?)

        /**
         * Called when the authentication operation fails.
         * @param error The error that occurred.
         */
        fun onError(error: Error)
    }

    private fun launchAuth(
        authCallback: AuthCallback, block: suspend () -> UserInfo?
    ) {
        scope.launch {
            try {
                val user = block()
                withContext(Dispatchers.Main) {
                    authCallback.onSuccess(user)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    authCallback.onError(Error(e))
                }
            }
        }
    }

    private fun launchSilent(block: suspend () -> Unit) {
        scope.launch {
            try {
                block()
            } catch (_: Exception) {
            }
        }
    }

    /**
     * Signs up a new user with email and password.
     * @param email The user's email address.
     * @param password The user's password.
     * @param authCallback Callback for success or error.
     */
    @JvmStatic
    fun signUp(email: String, password: String, authCallback: AuthCallback) =
        launchAuth(authCallback) {
            try {

                AuthManager.signUp(email, password)
                AuthManager.getCurrentUser()

            } catch (e: AuthRestException) {

                val code: String? = e.errorCode?.toString()

                val message = AUTH_ERROR_MESSAGES[code] ?: "Authentication failed."


                throw Exception(message)

            } catch (e: Exception) {

                throw Exception(
                    "Network error. Please try again.", e
                )
            }
        }

    /**
     * Validates the current session and refreshes it if necessary.
     * @param context Android context for shared preferences.
     */
    @JvmStatic
    fun validateSession(context: Context) = launchSilent {
        AuthManager.validateSession(context)
    }

    /**
     * Validates the current session and refreshes it if necessary.
     */
    @JvmStatic
    fun validateSession() = launchSilent {
        AuthManager.validateSession()
    }

    /**
     * Signs in an existing user with email and password.
     * @param email The user's email address.
     * @param password The user's password.
     * @param authCallback Callback for success or error.
     */
    @JvmStatic
    fun signIn(email: String, password: String, authCallback: AuthCallback) =
        launchAuth(authCallback) {
            try {
                AuthManager.signIn(email, password)
                AuthManager.getCurrentUser()
            } catch (e: AuthRestException) {

                val code: String? = e.errorCode?.toString()

                val message = AUTH_ERROR_MESSAGES[code] ?: "Authentication failed."
                throw Exception(message)

            } catch (e: Exception) {
                throw Exception(
                    "Network error. Please try again.", e
                )
            }
        }

    /**
     * Fetches the currently authenticated user's information.
     * @param authCallback Callback for success or error.
     */
    @JvmStatic
    fun getCurrentUser(authCallback: AuthCallback) = launchAuth(authCallback) {
        AuthManager.getCurrentUser()
    }

    /**
     * Signs out the current user.
     * @param context Android context for shared preferences.
     * @param authCallback Callback for success or error.
     */
    @JvmStatic
    fun signOut(context: Context, authCallback: AuthCallback) = launchAuth(authCallback) {
        AuthManager.signOut(context)
        AuthManager.getCurrentUser()
    }

    /**
     * Signs out the current user silently (no callback).
     * @param context Android context for shared preferences.
     */
    @JvmStatic
    fun signOut(context: Context) = launchSilent { AuthManager.signOut(context) }

    /**
     * Signs out the current user silently (no callback).
     */
    @JvmStatic
    fun signOut() = launchSilent { AuthManager.signOut() }

    /**
     * Deletes the current user's account.
     * @param authCallback Callback for success or error.
     */
    @JvmStatic
    fun deleteUser(authCallback: AuthCallback) = launchAuth(authCallback) {
        AuthManager.deleteUser()
        AuthManager.getCurrentUser()
    }

    /**
     * Deletes the current user's account silently (no callback).
     */
    @JvmStatic
    fun deleteUser() = launchSilent { AuthManager.deleteUser() }
}
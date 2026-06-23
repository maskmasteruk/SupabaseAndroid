package com.maskmasteruk.supabaseandroid.supabase

import android.content.Context
import com.maskmasteruk.supabaseandroid.objects.Error
import com.maskmasteruk.supabaseandroid.CONSTANTS.AUTH_ERROR_MESSAGES
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object AuthBridge {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    interface AuthCallback {
        fun onSuccess(user: UserInfo?)
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

    @JvmStatic
    fun validateSession(context: Context) = launchSilent {
        AuthManager.validateSession(context)
    }

    @JvmStatic
    fun validateSession() = launchSilent {
        AuthManager.validateSession()
    }

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

    @JvmStatic
    fun getCurrentUser(authCallback: AuthCallback) = launchAuth(authCallback) {
        AuthManager.getCurrentUser()
    }

    @JvmStatic
    fun signOut(context: Context, authCallback: AuthCallback) = launchAuth(authCallback) {
        AuthManager.signOut(context)
        AuthManager.getCurrentUser()
    }

    @JvmStatic
    fun signOut(context: Context) = launchSilent { AuthManager.signOut(context) }

    @JvmStatic
    fun signOut() = launchSilent { AuthManager.signOut() }

    @JvmStatic
    fun deleteUser(authCallback: AuthCallback) = launchAuth(authCallback) {
        AuthManager.deleteUser()
        AuthManager.getCurrentUser()
    }

    @JvmStatic
    fun deleteUser() = launchSilent { AuthManager.deleteUser() }
}
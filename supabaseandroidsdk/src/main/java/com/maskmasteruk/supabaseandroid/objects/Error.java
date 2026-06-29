package com.maskmasteruk.supabaseandroid.objects;

import androidx.annotation.NonNull;

import com.maskmasteruk.supabaseandroid.CONSTANTS;

/**
 * A wrapper class for exceptions and error messages to be passed back to the UI.
 * This class helps categorize errors and provides appropriate messages for display.
 */
public class Error {
    /**
     * The underlying exception that occurred.
     */
    private Exception exception;

    /**
     * Indicates whether the error message is safe/appropriate to show directly to the user.
     */
    private boolean isUserFriendly = false;

    /**
     * Constructs an Error from an Exception.
     * The stack trace is printed to the console for debugging.
     *
     * @param exception The exception to wrap.
     */
    public Error(Exception exception) {
        if (CONSTANTS.AUTH_ERROR_MESSAGES.containsValue(exception.getMessage())) {
            setUserFriendly(true);
        }
        this.exception = exception;
    }

    /**
     * Constructs an Error from a user-friendly message string.
     * This creates a new Exception with the provided message and marks it as user-friendly.
     *
     * @param s The user-facing error message.
     */
    public Error(String s) {
        this.exception = new Exception(s);
        setUserFriendly(true);
    }

    /**
     * Gets the wrapped exception.
     *
     * @return The Exception object.
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Sets the wrapped exception.
     *
     * @param exception The exception to wrap.
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * Returns a message suitable for displaying in UI components like Toasts or Snackbars.
     * If the error was explicitly marked as user-friendly, returns the exception's localized message.
     * Otherwise, returns a generic error message to avoid exposing technical details.
     *
     * @return A localized error message or a generic fallback.
     */
    public String getToastMessage() {
        return isUserFriendly ? exception.getLocalizedMessage() : "An unforeseen issue prevented the process from completing successfully.";
    }

    /**
     * Gets the raw message from the wrapped exception.
     *
     * @return The exception's message string.
     */
    public String getErrorMessage() {
        return exception.getMessage();
    }

    /**
     * Checks if the error message is user-friendly.
     *
     * @return true if the message is suitable for display to the user, false otherwise.
     */
    public boolean isUserFriendly() {
        return isUserFriendly;
    }

    /**
     * Sets whether the error message should be considered user-friendly.
     *
     * @param userFriendly true if the message is suitable for display to the user.
     * @return This Error instance for chaining.
     */
    public Error setUserFriendly(boolean userFriendly) {
        isUserFriendly = userFriendly;
        return this;
    }

    /**
     * Returns a string representation of this Error object.
     *
     * @return A string containing the error message.
     */
    @NonNull
    @Override
    public String toString() {
        return "Error{" +
                "message='" + exception.getMessage() + '\'' +
                '}';
    }
}

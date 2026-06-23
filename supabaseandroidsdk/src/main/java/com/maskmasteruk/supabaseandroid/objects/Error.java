package com.maskmasteruk.supabaseandroid.objects;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * A wrapper class for exceptions and error messages to be passed back to the UI.
 */
public class Error {
    private Exception exception;

    /**
     * Constructs an Error from an Exception.
     * @param exception The exception to wrap.
     */
    public Error(Exception exception) {
        exception.printStackTrace();
        this.exception = exception;
    }

    /**
     * Constructs an Error from a message string.
     * @param s The error message.
     */
    public Error(String s) {
        this.exception = new Exception(s);
    }

    /**
     * Gets the wrapped exception.
     * @return The exception.
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Sets the wrapped exception.
     * @param exception The exception to set.
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * Returns a localized message suitable for displaying in a Toast or Snackbar.
     * @return The localized error message.
     */
    public String getToastMessage() {
        return exception.getLocalizedMessage();
    }

    @NonNull
    @Override
    public String toString() {
        return "Data{" +
                exception.getMessage() +
                "}";
    }
}

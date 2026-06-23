package com.maskmasteruk.supabaseandroid.objects;

import android.util.Log;

import androidx.annotation.NonNull;

public class Error {
    private Exception exception;

    public Error(Exception exception) {
        Log.i("TAG", "Error: " + exception);
        exception.printStackTrace();
        this.exception = exception;
    }

    public Error(String s) {
        Log.i("TAG", "Error: " + s);
        this.exception = new Exception(s);
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

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

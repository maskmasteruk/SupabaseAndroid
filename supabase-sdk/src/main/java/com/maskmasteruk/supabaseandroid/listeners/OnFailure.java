package com.maskmasteruk.supabaseandroid.listeners;

import com.maskmasteruk.supabaseandroid.objects.Error;

public interface OnFailure {
    void onError(Error error);
}

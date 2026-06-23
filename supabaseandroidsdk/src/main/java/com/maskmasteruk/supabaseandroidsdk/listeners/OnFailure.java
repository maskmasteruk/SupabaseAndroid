package com.maskmasteruk.supabaseandroidsdk.listeners;

import com.maskmasteruk.supabaseandroidsdk.objects.Error;

public interface OnFailure {
    void onError(Error error);
}

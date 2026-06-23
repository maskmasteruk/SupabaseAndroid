package com.maskmasteruk.supabaseandroidsdk.listeners;

import com.maskmasteruk.supabaseandroidsdk.objects.Data;
import com.maskmasteruk.supabaseandroidsdk.objects.Error;

public interface OnComplete {
    void onData(Data data);
    void onError(Error error);

}

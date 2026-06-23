package com.maskmasteruk.supabaseandroid.listeners;

import com.maskmasteruk.supabaseandroid.objects.Data;
import com.maskmasteruk.supabaseandroid.objects.Error;

public interface OnComplete {
    void onData(Data data);
    void onError(Error error);

}

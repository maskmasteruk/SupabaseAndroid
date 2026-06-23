package com.maskmasteruk.supabaseandroid.objects;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class Data {
    public enum InputType {
        HASHMAP_STRING_OBJECT(1),
        BOOLEAN(2),
        URI(3),
        STRING(4),
        OBJECT(5);
        private final int value;

        InputType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public HashMap<String, Object> hashMap;
    public Boolean aBoolean;
    public Uri uri;
    public String string;
    public Object object;

    private final InputType inputType;


    public Data(Object object) {
        this.object = object;
        inputType = InputType.OBJECT;
    }

    public Data(String string) {
        this.string = string;
        inputType = InputType.STRING;
    }

    public Data(Boolean aBoolean) {
        this.aBoolean = aBoolean;
        inputType = InputType.BOOLEAN;
    }

    public Data(Uri uri) {
        this.uri = uri;
        inputType = InputType.URI;
    }


    public Data(HashMap<String, Object> hashMap) {
        this.hashMap = hashMap;
        inputType = InputType.HASHMAP_STRING_OBJECT;
    }

    public String getString() {
        return string;
    }



    public Object getData() {
        switch (inputType) {
            case HASHMAP_STRING_OBJECT:
                return hashMap;
            case BOOLEAN:
                return aBoolean;
            case URI:
                return uri;
            case STRING:
                return string;
            case OBJECT:
                return object;
        }

        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Data{" +
                "hashMap=" + hashMap +
                ", boolean=" + aBoolean +
                ", uri=" + uri +
                ", string='" + string + '\'' +
                ", inputType=" + inputType +
                '}';
    }
}

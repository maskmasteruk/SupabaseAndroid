# Supabase Android SDK Documentation

This library provides a simplified bridge for using Supabase (Auth, Postgres, and Storage) in Android applications. It is designed to work seamlessly with both Java and Kotlin.

## 0. Installation

### Add Plugin and Dependency

Add the following to your app-level `build.gradle.kts`:

```kotlin
plugins {
    id("com.android.application")
    id("io.github.maskmasteruk.supabaseandroid") version "1.0.0"
}

dependencies {
    implementation("io.github.maskmasteruk:supabaseandroid:1.0.0")
}
```

Ensure `mavenCentral()` is present in your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

### Configuration

Create a `supabase-config.json` file in your `app/` folder to store your credentials:

```json
{
  "supabase_url": "https://your-project.supabase.co",
  "supabase_anon_key": "your-anon-key"
}
```

The `supabaseandroid` plugin will automatically read this file and generate `BuildConfig.SUPABASE_URL` and `BuildConfig.SUPABASE_ANON_KEY` for you.

## 1. Initialization

Before using any Supabase features, you must initialize the `AuthManager`. This is typically done in your `MainActivity` or a custom `Application` class.

Since the `SupabasePlugin` automatically injects your credentials into `BuildConfig`, you can initialize it as follows:

```java
import com.maskmasteruk.supabaseandroid.supabase.AuthManager;
import com.maskmasteruk.supabaseandroiddemo.BuildConfig; // Ensure this is your app's BuildConfig

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize Supabase with credentials from BuildConfig
        AuthManager.initialize(
            this, 
            BuildConfig.SUPABASE_URL, 
            BuildConfig.SUPABASE_ANON_KEY
        );
    }
}
```

---

## 2. Authentication (AuthBridge)

`AuthBridge` handles user authentication using callbacks.

### Sign In
```java
AuthBridge.signIn("user@example.com", "password123", new AuthBridge.AuthCallback() {
    @Override
    public void onSuccess(UserInfo user) {
        // Handle successful sign in
        Log.d("Auth", "User logged in: " + user.getEmail());
    }

    @Override
    public void onError(SupabaseError supabaseError) {
        // Show supabaseError message
        Toast.makeText(context, supabaseError.getToastMessage(), Toast.LENGTH_SHORT).show();
    }
});
```

### Sign Up
```java
AuthBridge.signUp("newuser@example.com", "securePassword", new AuthBridge.AuthCallback() {
    @Override
    public void onSuccess(UserInfo user) {
        // User created (check email for verification if enabled)
    }

    @Override
    public void onError(SupabaseError supabaseError) {
        // Handle supabaseError
    }
});
```

### Get Current User
```java
AuthBridge.getCurrentUser(new AuthBridge.AuthCallback() {
    @Override
    public void onSuccess(UserInfo user) {
        if (user != null) {
            // User is signed in
        } else {
            // No active session
        }
    }

    @Override
    public void onError(SupabaseError supabaseError) { }
});
```

---

## 3. Database Operations (PostgresBridge)

`PostgresBridge` allows you to perform CRUD operations on your Supabase tables. Use `JsonUtils` to prepare data.

### Select Data
```java
PostgresBridge.select("profiles", null, "created_at", false, new PostgresBridge.CrudCallback() {
    @Override
    public void onSuccess(List<JsonObject> result) {
        for (JsonObject row : result) {
            Log.d("DB", "Row: " + row.toString());
        }
    }

    @Override
    public void onError(SupabaseError supabaseError) {
        Log.e("DB", supabaseError.getToastMessage());
    }
});
```

### Insert Data
```java
import kotlinx.serialization.json.JsonObject;
import com.maskmasteruk.supabaseandroid.utils.JsonUtils;

JsonObject data = JsonUtils.jsonObjectOf(
    "username", "john_doe",
    "full_name", "John Doe",
    "age", 25
);

PostgresBridge.insert("profiles", data, new PostgresBridge.InsertCallback() {
    @Override
    public void onSuccess(JsonObject result) {
        Log.d("DB", "Inserted: " + result.toString());
    }

    @Override
    public void onError(SupabaseError supabaseError) { }
});
```

---

## 4. Storage Operations (StorageBridge)

`StorageBridge` handles file uploads and deletions in Supabase Buckets.

### Upload File
```java
File file = new File(filePath);

StorageBridge.uploadFile("avatars", "folder/photo.jpg", file, new StorageBridge.UploadCallback() {
    @Override
    public void onSuccess(String url) {
        Log.d("Storage", "File uploaded! Public URL: " + url);
    }

    @Override
    public void onError(SupabaseError supabaseError) {
        Log.e("Storage", "Upload failed: " + supabaseError.getToastMessage());
    }
});
```

### Delete File
```java
StorageBridge.deleteFile("avatars", "folder/photo.jpg", new StorageBridge.DeleteCallback() {
    @Override
    public void onSuccess(boolean deleted) {
        // File deleted successfully
    }

    @Override
    public void onError(SupabaseError supabaseError) { }
});
```

---

## 5. Utilities (JsonUtils)

`JsonUtils` helps bridge Java objects to the `JsonObject` type used by the underlying Kotlin library.

```java
// Create a JsonObject fluently
JsonObject json = new JsonUtils.JsonParamBuilder()
    .add("id", 123)
    .add("status", "active")
    .add("is_premium", true)
    .build();

// Convert a Map to JsonObject
Map<String, Object> map = new HashMap<>();
map.put("key", "value");
JsonObject fromMap = JsonUtils.mapToJsonObject(map);
```

---

## 6. Error Handling

All bridge methods return a `com.maskmasteruk.supabaseandroid.objects.SupabaseError` object on failure.

*   `supabaseError.getException()`: Get the raw exception.
*   `supabaseError.getToastMessage()`: Get a user-friendly supabaseError message (translated from Supabase codes where possible).
*   `supabaseError.toString()`: Get a debug string representation.

---

## Credits & Dependencies

This library is built upon the following amazing projects:

*   **[Supabase Kotlin SDK](https://github.com/supabase-community/supabase-kt)** (v3.6.0) - The official community-driven Kotlin SDK for Supabase.
*   **[Ktor Client OkHttp](https://mvnrepository.com/artifact/io.ktor/ktor-client-okhttp)** (v3.4.3) - Asynchronous HTTP client for Kotlin.

---
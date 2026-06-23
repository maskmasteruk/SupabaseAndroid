package com.maskmasteruk.supabaseandroid;

import java.util.Map;

public class CONSTANTS {

    public static final Map<String, String> AUTH_ERROR_MESSAGES = Map.ofEntries(
            Map.entry("UserAlreadyExists", "An account with this email already exists."),
            Map.entry("EmailAddressInvalid", "Please enter a valid email address."),
            Map.entry("WeakPassword", "Password is too weak."),
            Map.entry("EmailNotConfirmed", "Please verify your email."),
            Map.entry("InvalidCredentials", "Incorrect email or password."),
            Map.entry("UserNotFound", "Account not found."),
            Map.entry("OtpExpired", "Verification code expired."),
            Map.entry("OverRequestRateLimit", "Too many attempts. Please try again later."),
            Map.entry("SignupDisabled", "Registration is disabled."),
            Map.entry("SessionExpired", "Session expired. Please sign in again.")
    );
}

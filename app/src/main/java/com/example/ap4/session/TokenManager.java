package com.example.ap4.session;


import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_ROLE = "user_role";

    private static SharedPreferences getPrefs(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveToken(Context context, String token) {
        getPrefs(context)
                .edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public static void saveUser(Context context, int id, int role) {
        getPrefs(context)
                .edit()
                .putInt(KEY_USER_ID, id)
                .putInt(KEY_USER_ROLE, role)
                .apply();
    }

    public static String getToken(Context context) {
        return getPrefs(context).getString(KEY_TOKEN, null);
    }

    public static int getUserId(Context context) {
        return getPrefs(context).getInt(KEY_USER_ID, -1);
    }

    public static int getUserRole(Context context) {
        return getPrefs(context).getInt(KEY_USER_ROLE, -1);
    }

    public static void clear(Context context) {
        getPrefs(context)
                .edit()
                .clear()
                .apply();
    }
}

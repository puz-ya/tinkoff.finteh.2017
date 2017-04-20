package tinkoff.androidcourse.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Sergey Boishtyan
 */
public final class PrefManager {

    private static final String PREF_FILE = "custom_name";
    private static final String PREF_LOGIN = "pref_login";

    private PrefManager() {
        //no instance
    }

    public static String login(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_LOGIN, null);
    }

    public static void saveLogin(Context context, String login) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(PREF_LOGIN, login);
        edit.commit();
    }
}

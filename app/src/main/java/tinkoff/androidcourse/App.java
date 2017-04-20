package tinkoff.androidcourse;

import android.app.Application;

import tinkoff.androidcourse.model.PrefManager;
import tinkoff.androidcourse.model.db.ChatDbHelper;

/**
 * @author Sergey Boishtyan
 */
public class App extends Application {

    private static ChatDbHelper chatDbHelper;

    public static ChatDbHelper getDbhelper() {
        return chatDbHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PrefManager.newInstance(this);
        chatDbHelper = new ChatDbHelper(this);
    }
}

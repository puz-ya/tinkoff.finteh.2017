package tinkoff.androidcourse;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * @author Sergey Boishtyan
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

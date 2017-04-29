package tinkoff.androidcourse;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

import tinkoff.androidcourse.model.PrefManager;

/**
 * @author Sergey Boishtyan
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PrefManager.newInstance(this);
        FlowManager.init(this); //init DBFlow (raizlabs)
    }
}

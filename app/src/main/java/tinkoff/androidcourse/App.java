package tinkoff.androidcourse;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

import tinkoff.androidcourse.model.PrefManager;

/**
 * @author Sergey Boishtyan
 */
public class App extends Application {

    public static final String ARG_TITLE = "fragments_argument_title";
    public static final String ARG_MENU_ID = "fragments_argument_menu_id";

    @Override
    public void onCreate() {
        super.onCreate();
        PrefManager.newInstance(this);
        FlowManager.init(this); //init DBFlow (raizlabs)
    }
}

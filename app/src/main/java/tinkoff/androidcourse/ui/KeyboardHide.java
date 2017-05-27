package tinkoff.androidcourse.ui;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created on 27.05.2017
 *
 * @author Puzino Yury
 */

public class KeyboardHide {

    /** hide keyboard when we want
     * @param activity - current activity
     *  */
    public static void hideSoftKeyboard(Activity activity) {
        if(activity != null){
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            View view = activity.getCurrentFocus();
            if(view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

}

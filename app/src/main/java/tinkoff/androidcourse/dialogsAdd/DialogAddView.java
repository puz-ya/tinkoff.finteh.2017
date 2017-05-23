package tinkoff.androidcourse.dialogsAdd;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created on 23.05.2017
 *
 * @author Puzino Yury
 */

public interface DialogAddView extends MvpView {

    // if OK -> return to NavigationActivity (DialogFragment)
    void redirectToNavigationWithExtra(String title, String descr);
    // if FAIL -> show error dialog (toast \ snack \ alert)
    void showFailedPopup(int code);
}

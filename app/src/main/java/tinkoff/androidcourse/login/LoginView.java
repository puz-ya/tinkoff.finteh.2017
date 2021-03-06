package tinkoff.androidcourse.login;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created on 21.05.2017
 *
 * @author Puzino Yury
 */

public interface LoginView extends MvpView{

    // if OK -> showing NavigationActivity
    void redirectToNavigation();
    // if FAIL -> show error dialog (toast \ snack \ alert)
    void showFailedAuth();
}

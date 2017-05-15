package tinkoff.androidcourse.login;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * @author Sergey Boishtyan
 */
public interface LoginView extends MvpView {

    void goToNavigationScreen();

    void showUnSuccessAuthorization();
}

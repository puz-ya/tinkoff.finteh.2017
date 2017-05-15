package tinkoff.androidcourse.login;

import android.support.annotation.VisibleForTesting;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

/**
 * @author Sergey Boishtyan
 */
public class LoginPresenter extends MvpBasePresenter<LoginView> {

    @VisibleForTesting
    Boolean authorizationResult;

    @Override
    public void attachView(LoginView view) {
        super.attachView(view);
        if (authorizationResult != null) {
            onAuthorizationResult(authorizationResult);
            authorizationResult = null;
        }
    }

    public void setAuthorizationResult(Boolean authorizationResult) {
        if (isViewAttached()) {
            onAuthorizationResult(authorizationResult);
        } else {
            this.authorizationResult = authorizationResult;
        }
    }

    public void onLoginButtonClick(String login, String password) {
        LoginTask loginTask = new LoginTask(this);
        loginTask.execute(new String[]{login, password});
    }

    @VisibleForTesting
    void onAuthorizationResult(Boolean authorizationDetails) {
        if (authorizationDetails) {
            getView().goToNavigationScreen();
        } else {
            getView().showUnSuccessAuthorization();
        }
    }
}

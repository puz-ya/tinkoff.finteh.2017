package tinkoff.androidcourse.login;

import android.support.annotation.VisibleForTesting;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

/**
 * Created on 21.05.2017
 *
 * @author Puzino Yury
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {

    @VisibleForTesting
    Boolean authResult;

    @Override
    public void attachView(LoginView loginView){
        super.attachView(loginView);
        if(authResult != null){
            onAuthResult(authResult);
            authResult = null;
        }
    }

    /* return method from LoginTask
    * */
    public void setAuthResult(Boolean authResult){
        if(isViewAttached()){
            onAuthResult(authResult);
        }else{
            this.authResult = authResult;
        }
    }

    /* create Task to check input data
    * */
    public void onLoginButtonClick(String login, String pass){
        LoginTask loginTask = new LoginTask(this);
        loginTask.execute(new String[]{login, pass});
    }

    /* based on Boolean result, make a decision
    * */
    @VisibleForTesting
    void onAuthResult(Boolean authResult){
        if(authResult){
            getView().redirectToNavigation();
        }else{
            getView().showFailedAuth();
        }
    }
}

package tinkoff.androidcourse.login;

import android.os.AsyncTask;

import tinkoff.androidcourse.model.PrefManager;

/**
 * @author Sergey Boishtyan
 */
class LoginTask extends AsyncTask<String[], Void, Boolean> {

    private LoginPresenter loginPresenter;

    public LoginTask(LoginPresenter loginFragment) {
        this.loginPresenter = loginFragment;
    }

    @Override
    protected Boolean doInBackground(String[]... credentials) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PrefManager.getInstance().saveLogin(credentials[0][0]);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        loginPresenter.setAuthorizationResult(success);
    }
}


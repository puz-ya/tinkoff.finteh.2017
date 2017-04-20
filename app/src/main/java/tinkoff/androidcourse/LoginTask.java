package tinkoff.androidcourse;

import android.os.AsyncTask;

import tinkoff.androidcourse.model.PrefManager;

/**
 * @author Sergey Boishtyan
 */
class LoginTask extends AsyncTask<String[], Void, Boolean> {

    private LoginFragment loginFragment;

    public LoginTask(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
    }

    @Override
    protected Boolean doInBackground(String[]... credentials) {
        PrefManager.saveLogin(loginFragment.getContext(), credentials[0][0]);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        loginFragment.setSuccess(success);
    }
}


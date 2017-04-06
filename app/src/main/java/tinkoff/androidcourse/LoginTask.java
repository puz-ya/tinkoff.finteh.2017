package tinkoff.androidcourse;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * @author Sergey Boishtyan
 */
class LoginTask extends AsyncTask<String[], Void, Boolean> {

    private WeakReference<LoginActivity> loginActivity;

    public LoginTask(LoginActivity loginActivity) {
        this.loginActivity = new WeakReference<>(loginActivity);
    }

    @Override
    protected Boolean doInBackground(String[]... credentials) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        loginActivity.get().showProgress();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        LoginActivity loginActivity = this.loginActivity.get();
        if (loginActivity != null) {
            Log.i("LoginTask", "onPostExecute " + loginActivity.toString());
            loginActivity.hideProgress();
            if (success) {
                loginActivity.startNextScreen();
            } else {
                new LoginActivity.MyDialogFragment().show(loginActivity.getSupportFragmentManager(), null);
            }
        }
    }
}

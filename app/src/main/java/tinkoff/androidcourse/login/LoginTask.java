package tinkoff.androidcourse.login;

import android.os.AsyncTask;

import tinkoff.androidcourse.model.PrefManager;

/**
 * @author Sergey Boishtyan
 */
class LoginTask extends AsyncTask<String[], Void, Boolean> {

    //private LoginFragment loginFragment;
    private LoginPresenter loginPresenter;
    private static final int DELAY = 10000;

    public LoginTask(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    protected Boolean doInBackground(String[]... credentials) {
        //emulating slow internet (rotate device NOW)
        try {
            Thread.sleep(DELAY);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }

        //check if Login & Password not empty
        if(credentials[0][0].isEmpty() || credentials[0][1].isEmpty()){
            return false;
        }

        PrefManager.getInstance().saveLogin(credentials[0][0]);

        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        loginPresenter.setAuthResult(success);
    }
}


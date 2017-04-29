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
        //emulating slow internet (rotate device NOW)
        try {
            Thread.sleep(5000);
        }catch (InterruptedException ex){

        }

        //check if not empty
        if(credentials[0][0].isEmpty() || credentials[0][1].isEmpty()){
            return false;
        }

        PrefManager.getInstance().saveLogin(credentials[0][0]);


        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        loginFragment.setSuccess(success);
    }
}


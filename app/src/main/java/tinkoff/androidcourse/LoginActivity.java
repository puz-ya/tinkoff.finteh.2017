package tinkoff.androidcourse;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import tinkoff.androidcourse.model.PrefManager;

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.LoginListener {

    // NavigationActivity extras
    public static final String EXTRA_LOGIN = "extra_login_set";

    /* moved to fragment
    private EditText login;
    private EditText password;
    private ProgressButton button;
    */

    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LoginActivity", "onCreate " + toString());
        setContentView(R.layout.activity_login);

        //we skip Login check if we already logged
        //TODO: needed more secure check
        if(PrefManager.getInstance().loggedIn() && !PrefManager.getInstance().login().isEmpty()){
            startNextScreen();
        }else{

            //get fragment, replace it
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (savedInstanceState != null) {
                loginFragment = (LoginFragment) supportFragmentManager.findFragmentByTag(LoginFragment.TAG);
                if (loginFragment == null) {
                    createLoginFragment(supportFragmentManager);
                }
            } else {
                createLoginFragment(supportFragmentManager);
            }

            /* Attempt to save state of Progress button in retain fragment - fail by 2017.04.30

            login = (EditText) findViewById(R.id.edit_text_login);
            password = (EditText) findViewById(R.id.edit_text_password);

            login.setText(PrefManager.getInstance().login());

            button = (ProgressButton) findViewById(R.id.btn_enter);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress();
                    new LoginTask(loginFragment).execute(new String[]{login.getText().toString(),password.getText().toString()});
                }
            });
            */
        }
    }

    /* moved to fragment
    public void showProgress() {
        button.showProgress();
    }

    public void hideProgress() {
        button.hideProgress();
    }
    */

    @Override
    public void onResult(Boolean success) {
        if (success) {
            PrefManager.getInstance().saveLoggedIn(true);
            startNextScreen();
        } else {
            loginFragment.reset();
            new LoginActivity.MyDialogFragment().show(getSupportFragmentManager(), null);
        }
    }

    void startNextScreen() {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(EXTRA_LOGIN, PrefManager.getInstance().login());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void createLoginFragment(FragmentManager supportFragmentManager) {
        loginFragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction = fragmentTransaction.replace(R.id.content_login, loginFragment, LoginFragment.TAG);
        fragmentTransaction.commit();
    }

    public static class MyDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.setTitle(getString(R.string.login_problem));
            return dialog;
        }
    }
}


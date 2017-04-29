package tinkoff.androidcourse;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import tinkoff.androidcourse.model.PrefManager;
import tinkoff.androidcourse.ui.widgets.ProgressButton;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginListener {

    // Service extras
    public static final String PENDING_INTENT = "pi";
    public static final String EXTRA_SUCCESS = "extra_success";

    // NavigationActivity extras
    public static final String EXTRA_LOGIN = "extra_login_set";

    private EditText login;
    private EditText password;
    private ProgressButton button;

    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LoginActivity", "onCreate " + toString());
        setContentView(R.layout.activity_login);

        //we skip Login check if we already logged
        //needed more secure check
        if(PrefManager.getInstance().loggedIn() && !PrefManager.getInstance().login().isEmpty()){
            startNextScreen();
        }else{

            login = (EditText) findViewById(R.id.edit_text_login);
            password = (EditText) findViewById(R.id.edit_text_password);

            login.setText(PrefManager.getInstance().login());

            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (savedInstanceState != null) {
                loginFragment = (LoginFragment) supportFragmentManager.findFragmentByTag(LoginFragment.TAG);
                if (loginFragment == null) {
                    createLoginFragment(supportFragmentManager);
                }
            } else {
                createLoginFragment(supportFragmentManager);
            }

            button = (ProgressButton) findViewById(R.id.btn_enter);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress();
                    new LoginTask(loginFragment).execute(new String[]{login.getText().toString(),password.getText().toString()});
                }
            });
        }
    }

    public void showProgress() {
        button.showProgress();
    }

    public void hideProgress() {
        button.hideProgress();
    }

    @Override
    public void onResult(Boolean success) {
        if (success) {
            PrefManager.getInstance().saveLoggedIn(true);
            startNextScreen();
        } else {
            hideProgress();
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
        supportFragmentManager.beginTransaction().add(loginFragment, LoginFragment.TAG).commit();
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


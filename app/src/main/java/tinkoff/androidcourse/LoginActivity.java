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

    public static final String PENDING_INTENT = "pi";
    public static final String EXTRA_SUCCESS = "extra_success";
    public static final String CREDENTIALS = "credentials";
    public static final int LOGIN_REQUEST_CODE = 1;

    private EditText login;
    private EditText password;
    private ProgressButton button;

    private LoginFragment loginFragment;

    @Override
    public void onResult(Boolean success) {
        if (success) {
            startNextScreen();
        } else {
            hideProgress();
            new LoginActivity.MyDialogFragment().show(getSupportFragmentManager(), null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LoginActivity", "onCreate " + toString());
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.edit_text_login);
        password = (EditText) findViewById(R.id.edit_text_password);
        button = (ProgressButton) findViewById(R.id.btn_enter);
        login.setText(PrefManager.getInstance().login());
        if (savedInstanceState != null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            loginFragment = (LoginFragment) supportFragmentManager.findFragmentByTag(LoginFragment.TAG);
            if (loginFragment != null) {

            } else {
                createLoginFragment(supportFragmentManager);
            }
        } else {
            createLoginFragment(getSupportFragmentManager());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                new LoginTask(loginFragment).execute(new String[]{login.getText().toString()});
            }
        });
    }

    public void showProgress() {
        button.showProgress();
    }

    public void hideProgress() {
        button.hideProgress();
    }

    void startNextScreen() {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("LOGIN", login.getText().toString());
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
            dialog.setTitle("У тебя проблемы");
            return dialog;
        }
    }
}


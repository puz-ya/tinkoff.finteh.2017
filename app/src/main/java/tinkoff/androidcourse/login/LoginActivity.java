package tinkoff.androidcourse.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import tinkoff.androidcourse.MainActivity;
import tinkoff.androidcourse.R;
import tinkoff.androidcourse.model.PrefManager;
import tinkoff.androidcourse.ui.widgets.ProgressButton;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView {

    public static final String PENDING_INTENT = "pi";
    public static final String EXTRA_SUCCESS = "extra_success";

    private EditText login;
    private EditText password;
    private ProgressButton button;

    @Override
    public void goToNavigationScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("LOGIN", login.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showUnSuccessAuthorization() {
        hideProgress();
        new LoginActivity.MyDialogFragment().show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (EditText) findViewById(R.id.edit_text_login);
        password = (EditText) findViewById(R.id.edit_text_password);
        button = (ProgressButton) findViewById(R.id.btn_enter);
        login.setText(PrefManager.getInstance().login());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                presenter.onLoginButtonClick(login.getText().toString(), password.getText().toString());
            }
        });
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    public void showProgress() {
        button.showProgress();
    }

    public void hideProgress() {
        button.hideProgress();
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


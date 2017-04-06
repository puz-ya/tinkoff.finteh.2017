package tinkoff.androidcourse;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import tinkoff.androidcourse.ui.widgets.ProgressButton;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private ProgressButton button;

    private LoginTask task = new LoginTask(this);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LoginActivity", "onDestroy " + toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LoginActivity", "onCreate " + toString());
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.edit_text_login);
        password = (EditText) findViewById(R.id.edit_text_password);
        button = (ProgressButton) findViewById(R.id.btn_enter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginTask(LoginActivity.this).execute();
            }
        });
    }

    public void showProgress() {
        button.showProgress();
    }

    public void hideProgress() {
        button.hideProgress();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LoginActivity", "onStop " + toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LoginActivity", "onStart " + toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LoginActivity", "onPause" + toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LoginActivity", "onResume" + toString());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("LoginActivity", "onSaveInstanceState" + toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("LoginActivity", "onRestoreInstanceState" + toString());
    }

    void startNextScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("LOGIN", login.getText().toString());
        startActivity(intent);
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


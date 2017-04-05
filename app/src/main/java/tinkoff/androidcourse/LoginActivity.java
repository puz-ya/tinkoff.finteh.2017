package tinkoff.androidcourse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import tinkoff.androidcourse.ui.widgets.ProgressButton;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private ProgressButton button;

    private SharedPreferences mSharedPreferences;
    public static final String PREFERENCES_FILENAME = "preferences_p.y.";

    protected static final String EXTRA_LOGIN = "LOGIN";
    protected static final String EXTRA_PASSW = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.edit_text_login);
        password = (EditText) findViewById(R.id.edit_text_password);
        button = (ProgressButton) findViewById(R.id.btn_enter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginTask().execute();
            }
        });

        mSharedPreferences = getApplicationContext()
                .getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        //check, if we are already logged in
        if(!mSharedPreferences.getString(EXTRA_LOGIN, "").isEmpty() && !mSharedPreferences.getString(EXTRA_PASSW, "").isEmpty()){
            //just start new Activity
            alreadyLoggedIn();
        }
    }

    private void startNextScreen() {

        String log = login.getText().toString(),
                pas = password.getText().toString();

        //check if not empty
        if(log.isEmpty() || pas.isEmpty()){
            Toast.makeText(this, "You need to enter something...", Toast.LENGTH_LONG).show();
        }else {
            //save login+pass pair in settings and send them to NavigationActivity too (for now)
            mSharedPreferences
                    .edit()
                    .putString(EXTRA_LOGIN, log)
                    .putString(EXTRA_PASSW, pas)
                    .apply();

            Intent intent = new Intent(this, NavigationActivity.class);
            intent.putExtra(EXTRA_LOGIN, log);
            intent.putExtra(EXTRA_PASSW, pas);
            startActivity(intent);
            finish();
        }
    }

    /** start new activity (we are logged in) */
    private void alreadyLoggedIn() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    private class LoginTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            button.showProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            button.hideProgress();
            startNextScreen();
        }
    }
}


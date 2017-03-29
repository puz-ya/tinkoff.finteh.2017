package tinkoff.androidcourse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button button;

    private SharedPreferences mSharedPreferences;
    public static final String PREFERENCES_FILENAME = "preferences_p.y.";

    protected static final String EXTRA_LOGIN = "LOGIN";
    protected static final String EXTRA_PASSW = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = (EditText) findViewById(R.id.edit_text_login);
        passwordEditText = (EditText) findViewById(R.id.edit_text_password);

        button = (Button) findViewById(R.id.btn_enter);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startNextScreen();
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

    /** save log in data and start new activity*/
    private void startNextScreen() {

        String log = loginEditText.getText().toString(),
               pas = passwordEditText.getText().toString();

        //check if not empty
        if(log.isEmpty() || pas.isEmpty()){
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.loginCoordinatorLayout);
            Snackbar.make(coordinatorLayout, "You need to enter something...", Snackbar.LENGTH_LONG).show();
        }else{

        //save login+pass pair in settings and send them to NavigationActivity too (for now)
        mSharedPreferences
                .edit()
                .putString(EXTRA_LOGIN, log)
                .putString(EXTRA_PASSW, pas)
                .apply();

        //start new activity
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
}

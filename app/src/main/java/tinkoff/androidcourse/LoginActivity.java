package tinkoff.androidcourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private Button button;

    private static final String EXTRA_LOGIN = "LOGIN";
    private static final String EXTRA_PASSW = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.edit_text_login);
        password = (EditText) findViewById(R.id.edit_text_password);

        button = (Button) findViewById(R.id.btn_enter);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startNextScreen();
            }
        });
    }

    private void startNextScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_LOGIN, login.getText().toString());
        intent.putExtra(EXTRA_PASSW, password.getText().toString());
        startActivity(intent);
    }
}

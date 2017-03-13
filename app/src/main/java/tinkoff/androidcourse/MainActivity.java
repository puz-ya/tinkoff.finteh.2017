package tinkoff.androidcourse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new GsonBuilder().create();
        Response response = gson.fromJson("{ value:\"bla bla bla\" }", Response.class);
        Toast.makeText(this, "Response: " + response.value, Toast.LENGTH_LONG).show();
    }
}

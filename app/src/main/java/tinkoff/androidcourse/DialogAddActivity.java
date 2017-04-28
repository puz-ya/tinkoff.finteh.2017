package tinkoff.androidcourse;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DialogAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("DIALOG_TITLE", "NewTitle");
        returnIntent.putExtra("DIALOG_DESCRIPT", "NewDescript");

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}

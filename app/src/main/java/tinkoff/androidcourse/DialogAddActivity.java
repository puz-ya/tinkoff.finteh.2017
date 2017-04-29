package tinkoff.androidcourse;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import tinkoff.androidcourse.ui.widgets.DialogAddCompoundView;

/**
 * Created on 28.04.2017
 * @author Puzino Yury
 *
 */

public class DialogAddActivity extends AppCompatActivity {

    private DialogAddCompoundView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add);

        view = (DialogAddCompoundView) findViewById(R.id.dialog_add_compound_view);

        view.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra(DialogFragment.EXTRA_DIALOG_TITLE, view.getTitleText());
                returnIntent.putExtra(DialogFragment.EXTRA_DIALOG_DESCR, view.getDescriptionText());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}

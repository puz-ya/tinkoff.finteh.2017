package tinkoff.androidcourse.dialogsAdd;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import tinkoff.androidcourse.DialogFragment;
import tinkoff.androidcourse.R;
import tinkoff.androidcourse.ui.widgets.DialogAddCompoundView;

/**
 * Created on 28.04.2017
 * @author Puzino Yury
 *
 */

public class DialogAddActivity extends MvpActivity<DialogAddView, DialogAddPresenter>
    implements DialogAddView {

    private DialogAddCompoundView compoundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add);

        compoundView = (DialogAddCompoundView) findViewById(R.id.dialog_add_compound_view);

        compoundView.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.onCheckInput(compoundView.getTitleText(), compoundView.getDescriptionText());

            }
        });

    }

    @NonNull
    @Override
    public DialogAddPresenter createPresenter(){
        return new DialogAddPresenter();
    }

    // vvvvv implement methods from DialogAddView (View part of MVP) vvvvv

    /* if input text (Name & Descr) is OK -> return intent with data
    * */
    @Override
    public void redirectToNavigationWithExtra(String title, String descr){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(DialogFragment.EXTRA_DIALOG_TITLE, title);
        returnIntent.putExtra(DialogFragment.EXTRA_DIALOG_DESCR, descr);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /* if input text (Name & Descr) NOT OK -> show red error
    * */
    @Override
    public void showFailedPopup(int code){
        if(compoundView != null) {
            compoundView.clearTexts();
            compoundView.setRedEdits(code);
        }
    }
}

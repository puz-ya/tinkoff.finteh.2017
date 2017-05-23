package tinkoff.androidcourse.ui.widgets;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import tinkoff.androidcourse.R;

/**
 * Created on 28.04.2017
 * @author Puzino Yury
 *
 * Compound View with for info about new dialog (title, text, send button)
 */

public class DialogAddCompoundView extends LinearLayout {

    private EditText editTextTitle;
    private EditText editTextDescript;
    private Button button;

    public DialogAddCompoundView(Context context) {
        super(context);
        init(context);
    }

    public DialogAddCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogAddCompoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_dialog_add, this);

        editTextTitle = (EditText) this.findViewById(R.id.dialog_add_title);
        editTextDescript = (EditText) this.findViewById(R.id.dialog_add_descr);

        button = (Button) this.findViewById(R.id.dialog_add_button);
        button.setEnabled(false);

        //check both edit text for none-emptiness

        editTextTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0 && !editTextDescript.getText().toString().equals("")) {
                    button.setEnabled(true);
                }else{
                    button.setEnabled(false);
                }
                resetRedEdits();
            }
        });

        editTextDescript.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0 && !editTextTitle.getText().toString().equals("")) {
                    button.setEnabled(true);
                }else{
                    button.setEnabled(false);
                }
                resetRedEdits();
            }
        });
    }

    public String getTitleText(){
        return editTextTitle.getText().toString();
    }

    public String getDescriptionText(){
        return editTextDescript.getText().toString();
    }

    public void setEditTextTitle(String editTextTitle) {
        this.editTextTitle.setText(editTextTitle);
    }

    public void setEditTextDescript(String editTextDescript) {
        this.editTextDescript.setText(editTextDescript);
    }

    public Button getButton(){
        return button;
    }

    public void clearTexts(){
        setEditTextTitle("");
        setEditTextDescript("");
    }

    /* set background color of EditTexts to bright one (red) - error
    * */
    public void setRedEdits(int code){
        editTextTitle.setBackgroundColor(Color.parseColor("#ff0000"));
        editTextDescript.setBackgroundColor(Color.parseColor("#ff0000"));
    }

    /* reset background color of EditTexts to default
    * */
    public void resetRedEdits(){
        editTextTitle.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.grey_100, null));
        editTextDescript.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.grey_100, null));
    }
}

package tinkoff.androidcourse.ui.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import tinkoff.androidcourse.R;
import tinkoff.androidcourse.ui.KeyboardHide;

/**
 * Created on 28.04.2017
 *
 * @author Puzino Yury
 */

public class SendMessageCompoundView extends LinearLayout {

    private EditText editText;
    private Button button;

    public SendMessageCompoundView(Context context) {
        super(context);
        init(context);
    }

    public SendMessageCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SendMessageCompoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_send_message, this);

        editText = (EditText) this.findViewById(R.id.chat_edittext);
        button = (Button) this.findViewById(R.id.chat_button_send);
        button.setEnabled(false);

        //create listener to check for non-empty EditText
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    button.setEnabled(true);
                }else{
                    button.setEnabled(false);
                }
            }
        });
    }

    public Button getButton(){
        return button;
    }

    public String getMessage(){
        return editText.getText().toString();
    }

    /** return to initial state - clear text & inactive button
     * */
    public void setSendState(){
        editText.setText("");   //clear previous text
        editText.clearFocus();
    }

}

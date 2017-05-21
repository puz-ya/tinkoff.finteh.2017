package tinkoff.androidcourse.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import tinkoff.androidcourse.R;

public class ProgressButton extends FrameLayout implements View.OnTouchListener {

    private View background;
    private TextView textView;
    private ProgressBar progressBar;

    public ProgressButton(Context context) {
        super(context);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_progress_button, this);
        background = findViewById(R.id.custom_PB_background);
        textView = (TextView) findViewById(R.id.custom_PB_textView);
        progressBar = (ProgressBar) findViewById(R.id.custom_PB_progressbar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.grey), PorterDuff.Mode.SRC_ATOP);

        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressButton);
        if (typedArray != null) {
            if (typedArray.getString(R.styleable.ProgressButton_text) != null) {
                setText(typedArray.getString(R.styleable.ProgressButton_text));
            }
            setEnabled(typedArray.getBoolean(R.styleable.ProgressButton_enabled, true));
            typedArray.recycle();
        }

        setOnTouchListener(this);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        background.setEnabled(enabled);
        textView.setEnabled(enabled);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void showProgress() {
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        this.setClickable(false);
    }

    public void hideProgress() {
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        this.setClickable(true);
    }

    /*
    HOWTO set & get CustomView state
    http://stackoverflow.com/questions/3542333/how-to-prevent-custom-views-from-losing-state-across-screen-orientation-changes
     */
    @Override
    public Parcelable onSaveInstanceState(){
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);

        savedState.clickable = isClickable();
        savedState.textViewVisibility = textView.getVisibility();
        savedState.progressBarVisibility = progressBar.getVisibility();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state){
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        setClickable(savedState.clickable);
        textView.setVisibility(savedState.textViewVisibility);
        progressBar.setVisibility(savedState.progressBarVisibility);
    }

    static class SavedState extends BaseSavedState{
        boolean clickable;
        int textViewVisibility;
        int progressBarVisibility;

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
            this.clickable = (in.readInt() == 1);
            this.textViewVisibility = in.readInt();
            this.progressBarVisibility = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags){
            super.writeToParcel(out, flags);
            out.writeInt(clickable ? 1 : 0);
            out.writeInt(textViewVisibility);
            out.writeInt(progressBarVisibility);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

    }
}

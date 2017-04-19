package tinkoff.androidcourse.ui.activities;

import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tinkoff.androidcourse.R;

public class TransitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        final ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.container);
        final TextView textView = (TextView) findViewById(R.id.textView);
        final View image = findViewById(R.id.imageView);
        final Button buttonExpand = (Button) findViewById(R.id.buttonExpand);

        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(sceneRoot);

                ViewGroup.LayoutParams params = image.getLayoutParams();
                params.width *= 2;
                params.height *= 2;
                image.setLayoutParams(params);

                textView.setVisibility(View.VISIBLE);
            }
        });
    }
}


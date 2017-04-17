package tinkoff.androidcourse.ui.activities;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import tinkoff.androidcourse.R;

public class AnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final Drawable drawable = imageView.getDrawable();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((Animatable) drawable).start();
                } else {
                    Toast.makeText(AnimationActivity.this, "NO ANIMATION :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


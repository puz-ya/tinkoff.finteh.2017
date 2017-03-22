package tinkoff.androidcourse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView tv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tv_about = (TextView) findViewById(R.id.tv_about);
        String about = " Puzino Yury \n puzino.y.a@gmail.com";

        //making email as URL
        //final SpannableString sResult = new SpannableString(about);
        Linkify.addLinks(tv_about, Linkify.EMAIL_ADDRESSES);

        tv_about.setText(about);
        //tv_about.setMovementMethod(LinkMovementMethod.getInstance());
    }
}

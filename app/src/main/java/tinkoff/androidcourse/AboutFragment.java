package tinkoff.androidcourse;

import android.support.transition.Scene;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AboutFragment extends Fragment {

    TextView tv_about;
    View secondView;
    Boolean aBoolean;

    public AboutFragment(){

    }

    public static AboutFragment newInstance(String title) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //title = getArguments().getString(ARG_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        tv_about = (TextView) view.findViewById(R.id.tv_about);
        final String about = " Puzino Yury \n puzino.y.a@gmail.com";

        //making email as URL
        Linkify.addLinks(tv_about, Linkify.EMAIL_ADDRESSES);

        tv_about.setText(about);
        aBoolean = tv_about.getVisibility() == View.VISIBLE;

        final ViewGroup sceneRoot = (ViewGroup) view.findViewById(R.id.about_scene);
        final View image = view.findViewById(R.id.about_logo);

        Button goButton = (Button)view.findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(sceneRoot);

                ViewGroup.LayoutParams params = image.getLayoutParams();

                aBoolean = !aBoolean;
                if(aBoolean) {
                    params.width /= 4;
                    params.height /= 4;
                    tv_about.setVisibility(View.VISIBLE);
                }else{
                    params.width *= 4;
                    params.height *= 4;
                    tv_about.setVisibility(View.GONE);
                }

                image.setLayoutParams(params);
            }
        });

        return view;
    }
}

package tinkoff.androidcourse;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {

    TextView tv_about;

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
        String about = " Puzino Yury \n puzino.y.a@gmail.com";

        //making email as URL
        Linkify.addLinks(tv_about, Linkify.EMAIL_ADDRESSES);

        tv_about.setText(about);

        return view;
    }
}

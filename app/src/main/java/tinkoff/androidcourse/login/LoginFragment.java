package tinkoff.androidcourse.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import tinkoff.androidcourse.login.LoginTask;
import tinkoff.androidcourse.R;
import tinkoff.androidcourse.model.PrefManager;
import tinkoff.androidcourse.ui.widgets.ProgressButton;

/**
 * @author Sergey Boishtyan
 * @author Puzino Yury
 *
 * Attempt to save state of Progress button in retain fragment - fail by 2017.04.30
 */
public class LoginFragment extends Fragment {

    public static final String TAG = "LoginFragment";
    private static final String BUNDLE_KEY = "myProgressButton";
    private Boolean success;
    private LoginListener loginListener;

    private EditText login;
    private EditText password;
    private ProgressButton button;

    //id of view, that we want to save (ProgressButton) -> getIt return -1 always :(
    private int viewId;
    //TODO: hack to round button
    private int start;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            loginListener = (LoginListener) context;
            if (success != null) {
                loginListener.onResult(success);
                success = null;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        viewId = -1;
        start = 0;
        // restore the view id
        if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY)){
            viewId = savedInstanceState.getInt(BUNDLE_KEY);
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the view id
        if(viewId > 0) {
            outState.putInt(BUNDLE_KEY, viewId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        if(viewId == -1 && view != null) {
            setActivityView(view);
        }

        login = (EditText) view.findViewById(R.id.edit_text_login);
        password = (EditText) view.findViewById(R.id.edit_text_password);

        login.setText(PrefManager.getInstance().login());

        button = (ProgressButton) view.findViewById(R.id.btn_enter);
        final LoginFragment fragment = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                start = 1;
                login.setEnabled(false);
                password.setEnabled(false);
                new LoginTask(fragment).execute(new String[]{login.getText().toString(),password.getText().toString()});
            }
        });

        if(start == 1){
            login.setEnabled(false);
            password.setEnabled(false);
            showProgress();
        }

        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getActivity().findViewById(viewId);
        if(view !=null) {
            button = (ProgressButton) view.findViewById(R.id.btn_enter);
            if (button != null) {
                button.showProgress();
            }
        }
    }

    public void setActivityView(View view) {
        viewId = view.getId();
    }

    public void showProgress() {
        button.showProgress();
    }

    public void hideProgress() {
        button.hideProgress();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        loginListener = null;
    }

    public void setSuccess(Boolean success) {
        if (loginListener != null) {
            loginListener.onResult(success);
        } else {
            this.success = success;
        }
    }

    public interface LoginListener {

        void onResult(Boolean success);
    }

    public void reset(){
        login.setEnabled(true);
        password.setEnabled(true);
        hideProgress();
    }
}

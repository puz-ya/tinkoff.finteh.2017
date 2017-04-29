package tinkoff.androidcourse;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @author Sergey Boishtyan
 */
public class LoginFragment extends Fragment {

    public static final String TAG = "LoginFragment";
    private Boolean success;
    private LoginListener loginListener;

    //id of view, that we want to save (ProgressButton)
    private int viewId = -1;

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

        // restore the view id
        if(savedInstanceState != null && savedInstanceState.containsKey("myProgressButton")){
            viewId = savedInstanceState.getInt("myProgressButton");
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the view id
        if(viewId > 0) {
            outState.putInt("myProgressButton", viewId);
        }
    }

    public void setActivityView(View view) {
        viewId = view.getId();
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
}

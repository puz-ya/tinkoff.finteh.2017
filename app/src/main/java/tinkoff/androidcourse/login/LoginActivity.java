package tinkoff.androidcourse.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import tinkoff.androidcourse.NavigationActivity;
import tinkoff.androidcourse.R;
import tinkoff.androidcourse.model.PrefManager;
import tinkoff.androidcourse.model.db.User;
import tinkoff.androidcourse.ui.widgets.ProgressButton;


public class LoginActivity extends MvpActivity<LoginView, LoginPresenter>
        implements LoginView, GoogleApiClient.OnConnectionFailedListener {

    // NavigationActivity extras
    public static final String EXTRA_LOGIN = "extra_login_set";

    //google auth + firebase auth
    private static final int G00GLE_SIGN_IN = 6006;
    private GoogleSignInOptions signInOptions;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleApiClient client;
    private SignInButton signInButton;

    private static final String ARG_DIALOG_FRAG = "title";

    private DatabaseReference mDatabase;

    /*
    // was in fragment (before mvp)
    private EditText login;
    private EditText password;
    private ProgressButton button;
    //*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LoginActivity", "onCreate " + toString());
        setContentView(R.layout.activity_login);
        //*/

        // google auth part
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        signInButton = (SignInButton) findViewById(R.id.google_signIN_button);
        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                googleSignIn();
            }

        });
        //*/

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //we skip Login check if we already logged
        //TODO: needed more secure check
        //button = (ProgressButton) findViewById(R.id.btn_enter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(PrefManager.getInstance().loggedIn() && !PrefManager.getInstance().login().isEmpty() && auth.getCurrentUser() != null){
            //too fast to show something
            redirectToNavigation();
        }else{

            //use raw auth process OR firebase.ui lib
            googleSignIn();
        }
        //*/

    }

    @NonNull
    @Override
    public LoginPresenter createPresenter(){
        return new LoginPresenter();
    }

    /** show default error */
    @Override
    public void showFailedAuth() {

        DialogFragment newFragment = MyDialogFragment.newInstance(
                getString(R.string.login_problem)
        );
        newFragment.show(getSupportFragmentManager(), null);
    }

    /** show error with user's text
     * @param failText - text to insert into FragmentDialog
     * */
    @Override
    public void showFailedAuth(String failText) {

        DialogFragment newFragment = MyDialogFragment.newInstance(
                failText
        );
        newFragment.show(getSupportFragmentManager(), null);
    }

    /** show error with provided id (usually from Presenter)
     * @param r_id - integer id from R.string.*
     * */
    @Override
    public void showFailedAuth(int r_id) {

        DialogFragment newFragment = MyDialogFragment.newInstance(
                getString(r_id)
        );
        newFragment.show(getSupportFragmentManager(), null);
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showFailedAuth(getString(R.string.google_login_problem));
    }

    public Activity getLoginActivity(){
        return this;
    }

    @Override
    public void redirectToNavigation(){
        PrefManager.getInstance().saveLoggedIn(true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String username = usernameFromEmail(user.getEmail());

            // write new user
            writeNewUser(user.getUid(), username, user.getEmail());

            Intent intent = new Intent(this, NavigationActivity.class);
            intent.putExtra(EXTRA_LOGIN, username);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            // didn't find user - log in once again
            googleSignIn();
        }
    }

    @Override
    public void redirectToNavigation(FirebaseUser user) {
        PrefManager.getInstance().saveLoggedIn(true);

        String username = usernameFromEmail(user.getEmail());

        // write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(EXTRA_LOGIN, username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0]; //left part of email
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    /*
    * google sign in - intent
    * */
    private void googleSignIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(client);
        startActivityForResult(signInIntent, G00GLE_SIGN_IN);

        /* we can make auth process with lib firebase.ui
        * and customize it */
        /*
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(),
                G00GLE_SIGN_IN);
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == G00GLE_SIGN_IN) {
            switch (resultCode){
                case ResultCodes.OK:
                    presenter.onLoginIntoGoogle(data);
                    break;
                case ResultCodes.CANCELED:
                    showFailedAuth(getString(R.string.google_signin_cancelled));
                    break;
                default:
                    showFailedAuth(getString(R.string.google_signin_intent_failed));
            }
        }
    }

    /*
    * dialog to show in ALL cases of ERRORs here in Login
    * */
    public static class MyDialogFragment extends DialogFragment {

        public static MyDialogFragment newInstance(String title) {
            MyDialogFragment frag = new MyDialogFragment();
            Bundle args = new Bundle();
            args.putString(ARG_DIALOG_FRAG, title);
            frag.setArguments(args);
            return frag;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString(ARG_DIALOG_FRAG);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            return builder
                    .setMessage(title)
                    .create();
        }

    }
}


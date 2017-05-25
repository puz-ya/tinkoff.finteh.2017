package tinkoff.androidcourse.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import tinkoff.androidcourse.NavigationActivity;
import tinkoff.androidcourse.R;
import tinkoff.androidcourse.model.PrefManager;
import tinkoff.androidcourse.ui.widgets.ProgressButton;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter>
        implements LoginView, GoogleApiClient.OnConnectionFailedListener {

    // NavigationActivity extras
    public static final String EXTRA_LOGIN = "extra_login_set";
    public static final String AUTH_ERROR_TEXT = "dialogErrorsBundle";

    //google auth + firebase auth
    private static final int G00GLE_SIGN_IN = 6006;
    private GoogleSignInOptions signInOptions;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleApiClient client;
    private SignInButton signInButton;

    // was in fragment (before mvp)
    private EditText login;
    private EditText password;
    private ProgressButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LoginActivity", "onCreate " + toString());
        setContentView(R.layout.activity_login);

        //we skip Login check if we already logged
        //TODO: needed more secure check
        if(PrefManager.getInstance().loggedIn() && !PrefManager.getInstance().login().isEmpty()){
            redirectToNavigation();
        }else{

            login = (EditText) findViewById(R.id.edit_text_login);
            password = (EditText) findViewById(R.id.edit_text_password);

            login.setText(PrefManager.getInstance().login());

            button = (ProgressButton) findViewById(R.id.btn_enter);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress();
                    //new LoginTask(loginFragment).execute(new String[]{login.getText().toString(),password.getText().toString()});
                    presenter.onLoginButtonClick(login.getText().toString(), password.getText().toString());
                }
            });
            //*/
        }

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

    }

    public void showProgress() {
        button.showProgress();
    }

    public void hideProgress() {
        button.hideProgress();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter(){
        return new LoginPresenter();
    }

    @Override
    public void showFailedAuth() {
        hideProgress();
        Bundle bundle = new Bundle();
        bundle.putString(AUTH_ERROR_TEXT, getString(R.string.google_login_problem));

        MyDialogFragment myDialogFragment = new LoginActivity.MyDialogFragment();
        myDialogFragment.setArguments(bundle);
        myDialogFragment.show(getSupportFragmentManager(), null);
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showFailedAuth();
    }

    @Override
    public void redirectToNavigation() {
        PrefManager.getInstance().saveLoggedIn(true);

        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(EXTRA_LOGIN, PrefManager.getInstance().login());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == G00GLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /** GOOGLE AUTH PART */

    /** try firebase auth after success */
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuth(acct);
        }

    }

    private void firebaseAuth(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                AuthResult result = task.getResult();
                FirebaseUser user = result.getUser();

                if (user != null) {
                        PrefManager.getInstance().saveLogin(user.getDisplayName());
                        redirectToNavigation();
                } else {
                    MyDialogFragment myDialogFragment = new LoginActivity.MyDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AUTH_ERROR_TEXT, getString(R.string.google_login_problem));
                    myDialogFragment.setArguments(bundle);
                    myDialogFragment.show(getSupportFragmentManager(), null);
                }
            }

        });
    }

    /*
    * google sign in - intent
    * */
    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(client);
        startActivityForResult(signInIntent, G00GLE_SIGN_IN);

    }

    /*
    * dialog to show in ALL cases of ERRORs
    * */
    public static class MyDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            Bundle bundle=getArguments();

            if(bundle != null) {
                String msg = bundle.getString(AUTH_ERROR_TEXT);
                dialog.setTitle(msg);
            }
            return dialog;
        }

    }
}


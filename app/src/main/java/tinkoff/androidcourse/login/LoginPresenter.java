package tinkoff.androidcourse.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import tinkoff.androidcourse.R;
import tinkoff.androidcourse.model.PrefManager;

import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED;
import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_FAILED;

/**
 * Created on 21.05.2017
 *
 * @author Puzino Yury
 */

public class LoginPresenter extends MvpBasePresenter<LoginView>
        implements GoogleApiClient.OnConnectionFailedListener{

    @VisibleForTesting
    Boolean authResult;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void attachView(LoginView loginView){
        super.attachView(loginView);
        if(authResult != null){
            onAuthResult(authResult);
            authResult = null;
        }
    }

    /* return method from LoginTask
    * */
    public void setAuthResult(Boolean authResult){
        if(isViewAttached()){
            onAuthResult(authResult);
        }else{
            this.authResult = authResult;
        }
    }

    /* create Task to check input data
    * */
    public void onLoginButtonClick(String login, String pass){
        LoginTask loginTask = new LoginTask(this);
        loginTask.execute(new String[]{login, pass});
    }


    /** GOOGLE AUTH PART */

    public void onLoginIntoGoogle(Intent data){
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(result);
    }

    /** try firebase auth after google auth success */
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuth(acct);
        }else{
            switch (result.getStatus().getStatusCode()){
                // sign in was cancelled by the user
                case SIGN_IN_CANCELLED:
                    getView().showFailedAuth(R.string.google_signin_cancelled);
                    break;
                //when seeing this error code, there is nothing user can do to recover from the sign in failure :D
                case SIGN_IN_FAILED:
                    getView().showFailedAuth(R.string.google_signin_failed);
                    break;
                default:
                    getView().showFailedAuth(R.string.google_signin_unknown_error);
                    break;
            }
        }

    }
    //*/


    private void firebaseAuth(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        if(isViewAttached()) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getView().getLoginActivity(), new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // get info if success
                                AuthResult result = task.getResult();
                                FirebaseUser user = result.getUser();

                                if (user != null) {
                                    PrefManager.getInstance().saveLogin(user.getDisplayName());
                                    getView().redirectToNavigation();
                                } else {
                                    getView().showFailedAuth(R.string.firebase_user_notfound);
                                }
                            } else {
                                // If sign in fails
                                getView().showFailedAuth(R.string.firebase_signin_failed);
                                //googleSignIn();
                            }
                        }

                    });
        }
    }
    //*/
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getView().showFailedAuth(R.string.google_login_problem);
    }

    /* based on Boolean result, make a decision
    * */
    @VisibleForTesting
    void onAuthResult(Boolean authResult){
        if(authResult){
            getView().redirectToNavigation();
        }else{
            getView().showFailedAuth();
        }
    }
}

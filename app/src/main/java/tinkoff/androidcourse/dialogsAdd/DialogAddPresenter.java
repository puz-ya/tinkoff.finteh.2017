package tinkoff.androidcourse.dialogsAdd;

import android.support.annotation.VisibleForTesting;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.raizlabs.android.dbflow.sql.language.Operator;

import tinkoff.androidcourse.login.LoginView;

/**
 * Created on 23.05.2017
 *
 * @author Puzino Yury
 */

public class DialogAddPresenter extends MvpBasePresenter<DialogAddView> {

    //for future version - check error and set view accordingly
    @VisibleForTesting
    Integer ERROR_NO_NAME = 1;
    @VisibleForTesting
    Integer ERROR_NO_DESCR = 2;
    @VisibleForTesting
    Integer ERROR_TOO_SHORT_NAME = 3;
    @VisibleForTesting
    Integer ERROR_TOO_SHORT_DESCR = 4;

    @VisibleForTesting
    Boolean result;
    @VisibleForTesting
    Integer error_code;

    @VisibleForTesting
    String title;
    @VisibleForTesting
    String descr;

    /* if we restore view state
    * */
    @Override
    public void attachView(DialogAddView addView){
        super.attachView(addView);
        if(this.result != null){
            onDecision(this.result);
            this.result = null;
        }
    }

    /* if we have view - send result right there,
    * else - just keep it
    * */
    public void setAuthResult(Boolean result){
        if(isViewAttached()){
            onDecision(result);
        }else{
            this.result = result;
        }
    }

    /* main checking function
    * */
    void onCheckInput(String title, String descr){
        result = true;
        if (title.isEmpty()) {
            result = false;
            error_code = ERROR_NO_NAME;
            setAuthResult(result);
            return;
        }else if (title.length() < 3){
            result = false;
            error_code = ERROR_TOO_SHORT_NAME;
            setAuthResult(result);
            return;
        }

        if(descr.isEmpty()){
            result = false;
            error_code = ERROR_NO_DESCR;
            setAuthResult(result);
            return;
        }else if (descr.length() < 3){
            result = false;
            error_code = ERROR_TOO_SHORT_DESCR;
            setAuthResult(result);
            return;
        }

        this.title = title;
        this.descr = descr;

        setAuthResult(result);
    }

    /* based on Boolean result, make a decision
    * */
    @VisibleForTesting
    void onDecision(Boolean decide){
        if(decide){
            getView().redirectToNavigationWithExtra(title, descr);
        }else{
            getView().showFailedPopup(error_code);
        }
    }
}

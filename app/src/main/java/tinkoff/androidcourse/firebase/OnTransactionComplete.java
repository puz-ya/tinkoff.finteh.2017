package tinkoff.androidcourse.firebase;

/**
 * Created on 25.05.2017
 *
 * @author Puzino Yury
 */

public interface OnTransactionComplete<T> {

    void onCommit(T result);
    void onAbort(Exception e);
}

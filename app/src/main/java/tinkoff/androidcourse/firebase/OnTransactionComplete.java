package tinkoff.androidcourse.firebase;

/**
 * @author Sergey Boishtyan
 */
public interface OnTransactionComplete<T> {
    void onCommit(T result);

    void onAbort(Exception e);
}

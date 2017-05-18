package tinkoff.androidcourse.firebase;

import java.util.List;

import tinkoff.androidcourse.model.DialogItem;

/**
 * @author Sergey Boishtyan
 */
public interface DialogItemValueListener {

    void onValue(List<DialogItem> items);
}

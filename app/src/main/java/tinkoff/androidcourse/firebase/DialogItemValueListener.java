package tinkoff.androidcourse.firebase;

import java.util.List;

import tinkoff.androidcourse.model.db.DialogItem;

/**
 * Created on 25.05.2017
 *
 * @author Puzino Yury
 */

public interface DialogItemValueListener {

    void onValue(List<DialogItem> dialogItems);
}

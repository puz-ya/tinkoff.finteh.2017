package tinkoff.androidcourse.firebase;

import java.util.List;
import tinkoff.androidcourse.model.db.MessageItem;

/**
 * Created on 25.05.2017
 *
 * @author Puzino Yury
 */

public interface MessageItemValueListener {

    void onValue(List<MessageItem> dialogItems);
}

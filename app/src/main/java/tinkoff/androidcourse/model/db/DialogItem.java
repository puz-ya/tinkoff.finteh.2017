package tinkoff.androidcourse.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

@Table(database = FintechChatDatabase.class)
public class DialogItem {

    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String title;

    @Column
    String desc;

    public DialogItem() {
    }

    public DialogItem(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
package tinkoff.androidcourse.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@Table(database = FintechChatDatabase.class)
public class DialogItem {

    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String title;

    @Column
    String desc;

    @Column
    String creation_time;   //yes, DBFlow accept Date format, but...

    /** version 2 */
    @Column
    String lastMessage;

    public DialogItem() {
    }

    public DialogItem(String title, String desc) {
        this.title = title;
        this.desc = desc;

        DateFormat df = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss", Locale.getDefault());
        this.creation_time = df.format(Calendar.getInstance().getTime());
    }

    //may be we need specify special TIME
    public DialogItem(String title, String desc, String creation_time) {
        this.title = title;
        this.desc = desc;
        this.creation_time = creation_time;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public long getId() {
        return id;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public String getLastMessage() {
        return lastMessage;
        //return "";    //testing v1 <-> v2 DB migration
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
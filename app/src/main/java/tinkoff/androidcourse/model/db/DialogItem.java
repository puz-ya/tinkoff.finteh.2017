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
            //todo: somehow useless with firebase
    long id;

    @Column
    String title;

    @Column
    String desc;

    @Column
    String creation_time;   //yes, DBFlow accept Date format, but...

    @Column
    String id_author;   //just set author id for this time
    //todo: need to set up "attended users list" in dialogs for each dialog

    /** version 2 */
    @Column
    String lastMessage;

    public DialogItem() {
    }

    /* deprecated since need user id
    public DialogItem(String title, String desc) {
        this.title = title;
        this.desc = desc;

        DateFormat df = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss", Locale.getDefault());
        this.creation_time = df.format(Calendar.getInstance().getTime());
        this.lastMessage = "";
    }
    */

    public DialogItem(String title, String desc, String id_author) {
        this.title = title;
        this.desc = desc;
        this.id_author = id_author;

        DateFormat df = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss", Locale.getDefault());
        this.creation_time = df.format(Calendar.getInstance().getTime());
        this.lastMessage = "";
    }

    //may be we need specify special TIME
    public DialogItem(String title, String desc, String id_author, String creation_time) {
        this.title = title;
        this.desc = desc;
        this.id_author = id_author;
        this.creation_time = creation_time;
        this.lastMessage = "";
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

    public String getId_author() {
        return id_author;
    }

    public void setId_author(String id_author) {
        this.id_author = id_author;
    }
}
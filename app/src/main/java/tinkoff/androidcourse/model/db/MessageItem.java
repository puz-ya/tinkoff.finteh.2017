package tinkoff.androidcourse.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Puzino Yury
 */

@Table(database = FintechChatDatabase.class)
public class MessageItem {

    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String text;

    @Column
    String id_author;

    @Column
    String id_dialog;

    @Column
    String creation_time;

    public MessageItem() {
    }

    public MessageItem(String text, String id_author) {
        this.text = text;
        this.id_author = id_author;
        this.id_dialog = "1"; //common dialog for all "strange" messages

        DateFormat df = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss", Locale.getDefault());
        this.creation_time = df.format(Calendar.getInstance().getTime());
    }

    public MessageItem(String text, String id_author, String id_dialog) {
        this.text = text;
        this.id_author = id_author;
        this.id_dialog = id_dialog;

        DateFormat df = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss", Locale.getDefault());
        this.creation_time = df.format(Calendar.getInstance().getTime());
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getId_author() {
        return id_author;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId_author(String id_author) {
        this.id_author = id_author;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getId_dialog() {
        return id_dialog;
    }

    public void setId_dialog(String id_dialog) {
        this.id_dialog = id_dialog;
    }
}
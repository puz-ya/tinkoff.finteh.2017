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
    long id_author;

    @Column
    String creation_time;

    public MessageItem() {
    }

    public MessageItem(String text, long id_author) {
        this.text = text;
        this.id_author = id_author;

        DateFormat df = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss", Locale.getDefault());
        this.creation_time = df.format(Calendar.getInstance().getTime());
    }

    public MessageItem(String text, long id_author, String creation_time) {
        this.text = text;
        this.id_author = id_author;
        this.creation_time = creation_time;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public long getId_author() {
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

    public void setId_author(long id_author) {
        this.id_author = id_author;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }
}
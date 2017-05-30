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

public class MessageItem {

    //all id-s now are Strings
    String id;

    String text;

    String id_author;

    String id_dialog;

    String creation_time;

    public MessageItem() {
    }

    public MessageItem(String id, String id_author, String text, String id_dialog, String creation_time) {
        this.id = id;
        this.text = text;
        this.id_author = id_author;
        this.id_dialog = id_dialog;

        DateFormat df = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss", Locale.getDefault());
        this.creation_time = df.format(Calendar.getInstance().getTime());
    }

    public String getId() {
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

    public void setId(String id) {
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
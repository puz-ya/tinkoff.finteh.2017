package tinkoff.androidcourse.model.db;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//Properties that don't map to class fields are ignored when serializing to a class annotated with this annotation.
@IgnoreExtraProperties
public class DialogItem {

    String title;
    String desc;

    String creation_time;   //yes, DBFlow accept Date format, but...

    String author_id;   //just set author id for this time
    String author_name;

    String lastMessage;

    //todo: need to set up "attended users list" in dialogs for each dialog
    int usersCount = 0;
    public Map<String, Boolean> users = new HashMap<>();


    public DialogItem() {
    }

    //may be we need specify special TIME
    public DialogItem(String id_author, String name_author, String title, String desc, String creation_time, String lastMessage) {

        this.author_id = id_author;
        this.author_name = name_author;

        this.title = title;
        this.desc = desc;

        this.creation_time = creation_time;
        this.lastMessage = lastMessage;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", author_id);
        result.put("author_name", author_name);

        result.put("title", title);
        result.put("desc", desc);

        result.put("creation_time", creation_time);

        result.put("usersCount", usersCount);
        result.put("users", users);

        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
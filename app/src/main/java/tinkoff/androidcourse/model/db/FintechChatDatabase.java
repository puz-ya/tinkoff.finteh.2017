package tinkoff.androidcourse.model.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.sql.migration.UpdateTableMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * @author Sergey Boishtyan
 * @author Puzino Yury
 */
@Database(name = FintechChatDatabase.NAME, version = FintechChatDatabase.VERSION)
public final class FintechChatDatabase {

    public static final String NAME = "chat_db";
    public static final int VERSION = 2;

    @Migration(version = 2, priority = 2, database = FintechChatDatabase.class)
    public static class AddLastMessageColumn extends AlterTableMigration<DialogItem> {

        public AddLastMessageColumn(Class<DialogItem> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            super.onPreMigrate();
            addColumn(SQLiteType.TEXT, "lastMessage");
        }
    }

    @Migration(version = 2, priority = 1, database = FintechChatDatabase.class)
    public static class Migration2 extends BaseMigration {

        @Override
        public void migrate(DatabaseWrapper database) {

            Cursor cursor_messages = database.rawQuery(
                    "SELECT MAX(id), text, id_dialog " +
                            "FROM MessageItem " +
                            "GROUP BY id_dialog", null);

            List<String> lastMess = new ArrayList<>();
            List<Long> lastIdDialog = new ArrayList<>();

            if (cursor_messages.moveToFirst()) {
                while (!cursor_messages.isAfterLast()) {
                    lastIdDialog.add(cursor_messages.getLong(cursor_messages.getColumnIndex("id_dialog")));
                    lastMess.add(cursor_messages.getString(cursor_messages.getColumnIndex("text")));
                    cursor_messages.moveToNext();
                }
            }
            cursor_messages.close();

            //bad practice, but nothing better :(
            for(int i=0; i < lastMess.size(); i++){

                SQLite.update(DialogItem.class)
                        .set(DialogItem_Table.lastMessage.eq(lastMess.get(i)))
                        .where(DialogItem_Table.id.eq(lastIdDialog.get(i)))
                        .execute(database); // required inside a migration to pass the wrapper

            }

        }
    }
}

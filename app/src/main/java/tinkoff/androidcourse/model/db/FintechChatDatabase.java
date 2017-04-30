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

            //Cursor cursor_messages = database.rawQuery("SELECT `id`,`text`,`id_dialog`,`creation_time` FROM MessageItem WHERE `creation_time` GROUP By `id_dialog`", null);
            for(int i=0; i < lastMess.size(); i++){
                /*
                SQLite.update(DialogItem_Table.class)
                        .set(DialogItem_Table.lastMessage.eq(lastMess.get(i)))
                        .where(DialogItem_Table.id.is(lastIdDialog.get(i)));
                       */
                /*
                Cursor cursor_update = database.rawQuery(
                        "UPDATE DialogItem SET lastMessage = ? " +
                                "WHERE id = ?",
                        new String[]{lastMess.get(i),String.format(Locale.getDefault(), "%d", lastIdDialog.get(i))});
                cursor_update.close();
                //*/

                /*
                ContentValues cv = new ContentValues();
                cv.put("lastMessage", lastMess.get(i));

                String condition = String.format(Locale.getDefault(), "%d", lastIdDialog.get(i));
                String where = "id = " + condition;

                database.updateWithOnConflict("DialogItem", cv,
                        where, null, 0);
                //*/

                /*
                String condition = String.format(Locale.getDefault(), "%d", lastIdDialog.get(i));
                String s = "UPDATE DialogItem SET `lastMessage` = " + lastMess.get(i) + " WHERE `id`=" + condition;
                database.execSQL(s);
                */

                SQLite.update(DialogItem.class)
                        .set(DialogItem_Table.lastMessage.eq(lastMess.get(i)))
                        .where(DialogItem_Table.id.eq(lastIdDialog.get(i)))
                        .execute(database); // required inside a migration to pass the wrapper
                /*
                Cursor cursor_up = database.rawQuery(s, null);
                if (cursor_up.moveToFirst()) {
                    Integer w = 0;
                    w++;
                }
                cursor_up.close();
                */
            }

            //SQLite.update(DialogItem_Table.class)
            //        .set(DialogItem_Table.lastMessage.eq("New Name")).where(DialogItem_Table.id.greaterThan(0L));

            /*
            SQLite.update(DialogItem_Table.class)
                    .set(Employee_Table.status.eq("Invalid"))
                    .where(Employee_Table.job.eq("Laid Off"))
                    .execute(database); // required inside a migration to pass the wrapper
                    */
        }
    }
}

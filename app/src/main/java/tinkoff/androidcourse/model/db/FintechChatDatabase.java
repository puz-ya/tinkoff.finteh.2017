package tinkoff.androidcourse.model.db;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.raizlabs.android.dbflow.sql.migration.UpdateTableMigration;

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
    public static class SetLastMessageColumn extends UpdateTableMigration<DialogItem> {

        public SetLastMessageColumn(Class<DialogItem> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            set(DialogItem_Table.lastMessage.eq("New Name")).where(DialogItem_Table.id.greaterThan(0L));
            super.onPreMigrate();
        }
    }
    //*/
}

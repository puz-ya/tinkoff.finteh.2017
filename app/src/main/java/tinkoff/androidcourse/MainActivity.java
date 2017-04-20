package tinkoff.androidcourse;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import tinkoff.androidcourse.model.db.DbContract;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DialogsAdapter adapter;
    private Button addDialog;
    private SQLiteDatabase writableDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        writableDatabase = App.getDbhelper().getWritableDatabase();
        setContentView(R.layout.activity_main);
        initRecyclerView();
        ArrayList<DialogItem> dialogItems = getPreviousDialogItems();
        adapter.setItems(dialogItems);
        addDialog = (Button) findViewById(R.id.add_dialog);
        addDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialogItem();
            }
        });
    }

    private void addDialogItem() {
        int itemCount = adapter.getItemCount();
        DialogItem dialogItem = new DialogItem("Title is " + itemCount, "Description is " + itemCount);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.DialogEntry.COLUMN_TITLE, dialogItem.getTitle());
        contentValues.put(DbContract.DialogEntry.COLUMN_DESCRIPTION, dialogItem.getTitle());
        contentValues.put(DbContract.DialogEntry.COLUMN_TIMESTAMP, new Date().getTime());
        writableDatabase.insert(DbContract.DialogEntry.TABLE_NAME, null, contentValues);
        adapter.addDialog(dialogItem);
    }

    @NonNull
    private ArrayList<DialogItem> getPreviousDialogItems() {
        Cursor cursor = writableDatabase.query(DbContract.DialogEntry.TABLE_NAME,
                new String[]{
                        DbContract.DialogEntry.COLUMN_TITLE,
                        DbContract.DialogEntry.COLUMN_DESCRIPTION
                },
                null,
                null,
                null,
                null,
                null);
        ArrayList<DialogItem> dialogItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            int titleIndex = cursor.getColumnIndex(DbContract.DialogEntry.COLUMN_TITLE);
            int descriptionIndex = cursor.getColumnIndex(DbContract.DialogEntry.COLUMN_DESCRIPTION);
            String title = cursor.getString(titleIndex);
            String description = cursor.getString(descriptionIndex);
            dialogItems.add(new DialogItem(title, description));
        }
        cursor.close();
        return dialogItems;
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_dialogs);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DialogsAdapter(new ArrayList<DialogItem>(), new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "position = " + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}

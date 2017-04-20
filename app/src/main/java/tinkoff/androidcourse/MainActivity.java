package tinkoff.androidcourse;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
        addDialog = (Button) findViewById(R.id.add_dialog);
        addDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemCount = adapter.getItemCount();
                DialogItem dialogItem = new DialogItem("Title is " + itemCount, "Description is " + itemCount);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DbContract.DialogEntry.COLUMN_TITLE, dialogItem.getTitle());
                contentValues.put(DbContract.DialogEntry.COLUMN_DESCRIPTION, dialogItem.getTitle());
                contentValues.put(DbContract.DialogEntry.COLUMN_TIMESTAMP, new Date().getTime());
                writableDatabase.insert(DbContract.DialogEntry.TABLE_NAME, null, contentValues);
                adapter.addDialog(dialogItem);
            }
        });
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

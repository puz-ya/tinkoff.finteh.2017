package tinkoff.androidcourse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/** Show list of messages in selected chat group */
public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);

        initRecyclerChatView();
    }

    private void initRecyclerChatView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //setting chat from bottom
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatAdapter(createDataset(), new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ChatActivity.this, "message = " + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private List<MessageItem> createDataset() {
        List<MessageItem> list = new ArrayList<>();
        list.add(new MessageItem("Text1", "2017-03-20", "user1"));
        list.add(new MessageItem("Text2", "2017-03-21", "user2"));
        list.add(new MessageItem("Text3", "2017-03-22", "user3"));
        list.add(new MessageItem("Text4", "2017-03-23", "user4"));
        list.add(new MessageItem("Text5", "2017-03-24", "user5"));
        return list;
    }
}

package tinkoff.androidcourse;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import tinkoff.androidcourse.model.db.DialogItem;
import tinkoff.androidcourse.model.db.MessageItem;
import tinkoff.androidcourse.model.db.MessageItem_Table;
import tinkoff.androidcourse.ui.widgets.SendMessageCompoundView;

/** Show list of messages in selected chat group */
public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private SendMessageCompoundView sView;
    //private

    public final static String ARG_DIALOG_ID = "DialogID";
    private long chatId = -1L;

    public ChatFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ChatFragment newInstance(String title) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_POSITION, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        if (getArguments() != null) {
            chatId = getArguments().getLong(ARG_DIALOG_ID);
        }

        initRecyclerChatView(view);
        List<MessageItem> messageItems = getDialogMessageItems();
        adapter.setItems(messageItems);

        initSendMessageView(view);

        return view;
    }

    private void initSendMessageView(View view) {
        sView = (SendMessageCompoundView) view.findViewById(R.id.send_message_view);
        sView.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set click action
                String message = sView.getMessage();
                addMessageItemToChat(message);
                sView.setSendState();
            }
        });
    }

    /** //TODO: we didn't implement user_id feature (no DB_table, only loginName...) -> set default
     * */
    private void addMessageItemToChat(String message){
        MessageItem messageItem = new MessageItem(
                message,
                1,
                chatId);
        FlowManager.getModelAdapter(MessageItem.class).save(messageItem);
        adapter.addMessage(messageItem);
        recyclerView.smoothScrollToPosition(0);
    }

    private void initRecyclerChatView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //setting chat from bottom
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatAdapter(new ArrayList<MessageItem>(), new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), "Message id (from DB) = " + adapter.getItemId(position), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private List<MessageItem> getDialogMessageItems() {
        List<MessageItem> itemList = SQLite.select()
                .from(MessageItem.class)
                .where(MessageItem_Table.id_dialog.is(chatId))
                .orderBy(MessageItem_Table.creation_time, false)
                .queryList();

        return itemList;
    }

    /*
    private List<MessageItem> createDataset() {
        List<MessageItem> list = new ArrayList<>();
        list.add(new MessageItem("Text1", "2017-03-20", "user1"));
        list.add(new MessageItem("Text2", "2017-03-21", "user2"));
        list.add(new MessageItem("Text3", "2017-03-22", "user3"));
        list.add(new MessageItem("Text4", "2017-03-23", "user4"));
        list.add(new MessageItem("Text5", "2017-03-24", "user5"));
        return list;
    }
    //*/
}

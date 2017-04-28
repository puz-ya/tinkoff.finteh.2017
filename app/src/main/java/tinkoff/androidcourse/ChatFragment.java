package tinkoff.androidcourse;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import tinkoff.androidcourse.ui.widgets.SendMessageView;

/** Show list of messages in selected chat group */
public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    //private

    public static String ARG_POSITION = "ChatID";

    public ChatFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //title = getArguments().getString(ARG_TITLE);
        }
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

        initRecyclerChatView(view);
        initSendMessageView(view);

        return view;
    }

    private void initSendMessageView(View view) {
        SendMessageView sView = (SendMessageView) view.findViewById(R.id.send_message_view);
        Button button = (Button) view.findViewById(R.id.chat_button_send);

        //set click listener
    }

    private void initRecyclerChatView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //setting chat from bottom
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatAdapter(createDataset(), new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(ChatFragment.this, "message = " + position, Toast.LENGTH_SHORT).show();
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

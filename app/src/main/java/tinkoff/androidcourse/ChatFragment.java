package tinkoff.androidcourse;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;
import java.util.Locale;

import tinkoff.androidcourse.firebase.DialogRepository;
import tinkoff.androidcourse.firebase.MessagesRepository;
import tinkoff.androidcourse.firebase.OnTransactionComplete;
import tinkoff.androidcourse.model.db.DialogItem;
import tinkoff.androidcourse.model.db.MessageItem;
import tinkoff.androidcourse.model.db.MessageItem_Table;
import tinkoff.androidcourse.ui.KeyboardHide;
import tinkoff.androidcourse.ui.widgets.SendMessageCompoundView;

/** Show list of messages in selected chat group */
public class ChatFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<MessageItem>>{

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    public FirebaseRecyclerAdapter<MessageItem, ChatAdapter.ViewChatHolder> adapterFireBchat;

    private SendMessageCompoundView sView;
    ProgressDialog progress;
    private View mView;

    private MessagesRepository messagesRepository = MessagesRepository.getInstance();

    public final static String ARG_DIALOG_ID = "DialogID";
    private String chatId = "-1";
    private static final int DELAY_INSERT_UPDATE = 2000;
    private static final int DELAY_GET_FROM_SOURCE = 2000;

    private static final int USER_MESSAGE = 666;
    private static final int OTHERS_MESSAGE = 999;

    public ChatFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ChatFragment newInstance(String title) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        if (getArguments() != null) {
            chatId = getArguments().getString(ARG_DIALOG_ID);
        }

        initSendMessageView(view);
        this.mView = view;

        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity().findViewById(R.id.toolbar) != null){
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.chat_fragment_set_title) + String.format(Locale.getDefault(), "%s", chatId));
        }

        showProgressLoader();
        getLoaderManager().initLoader(0, null, this);
    }

        private void initSendMessageView(View view) {
        sView = (SendMessageCompoundView) view.findViewById(R.id.send_message_view);
        sView.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set click action
                String message = sView.getMessage();
                sView.setSendState();
                KeyboardHide.hideSoftKeyboard(getActivity());

                addMessageItemToChat(message);
            }
        });
    }

    /** set proper message, user ID, chat ID for this message
     * @param message - text from user input
     * */
    private void addMessageItemToChat(String message){

        UserInfo userInfo = FirebaseAuth.getInstance().getCurrentUser();
        String uID = "-1";
        if(userInfo != null){
            uID = userInfo.getUid();
        }

        MessageItem messageItem = new MessageItem(
                message,
                uID,
                chatId);

        updateMessages(messageItem);
    }

    private void initRecyclerChatView(List<MessageItem> dataSet) {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        //setting chat from bottom
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatAdapter(dataSet, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(),
                        getString(R.string.chat_toast_id) + adapter.getItemId(position),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //set Firebase adapter
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("messages");
        Query query = ref.orderByChild("id_dialog").limitToLast(20).equalTo(chatId);

        adapterFireBchat = new FirebaseRecyclerAdapter<MessageItem, ChatAdapter.ViewChatHolder>(
                MessageItem.class, R.layout.item_chat_dialog, ChatAdapter.ViewChatHolder.class, query
        ){
            @Override
            public ChatAdapter.ViewChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = null;
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());

                //set layout depends on TYPE
                switch(viewType){
                    case USER_MESSAGE:
                        view = inflater.inflate(R.layout.item_chat_messages_right, parent, false);
                        break;
                    case OTHERS_MESSAGE:
                        view = inflater.inflate(R.layout.item_chat_messages_left, parent, false);
                        break;
                    default:
                        view = inflater.inflate(R.layout.item_chat_messages_left, parent, false);
                        break;
                }

                return new ChatAdapter.ViewChatHolder(view);
            }

            @Override
            public int getItemViewType(int position) {
                // Simply returning an integer to use in onCreateViewHolder.
                UserInfo userInfo = FirebaseAuth.getInstance().getCurrentUser();
                MessageItem messageItem = getItem(position);
                if (userInfo != null){
                    if (messageItem.getId_author().equals(userInfo.getUid())){
                        return USER_MESSAGE;
                    }else{
                        return OTHERS_MESSAGE;
                    }
                }
                //todo: need to set error code
                return OTHERS_MESSAGE;
            }

            @Override
            public void populateViewHolder(ChatAdapter.ViewChatHolder viewHolder, MessageItem model, final int FBpos) {
                viewHolder.setText(model.getText());
                viewHolder.setTime(model.getCreation_time());
                viewHolder.setUserId(model.getId_author());

                viewHolder.getmView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //get dialog's ID from FB adapter and give it to Chat
                        long chatId = adapterFireBchat.getItem(FBpos).getId();
                        Toast.makeText(getActivity(),
                                getString(R.string.chat_toast_id) + chatId,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };

        recyclerView.setAdapter(adapterFireBchat);
    }

    @NonNull
    private List<MessageItem> getDialogMessageItems() {
        List<MessageItem> itemList = SQLite.select()
                .from(MessageItem.class)
                .where(MessageItem_Table.id_dialog.is(chatId))
                .orderBy(MessageItem_Table.creation_time, false)
                .queryList();
        //yes, it's redundant, but useful for debug
        return itemList;
    }

    private void updateMessages(final MessageItem message){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){

                //todo: either clean install either update DB version with migration (v3)
                //FlowManager.getModelAdapter(MessageItem.class).save(message);
                adapter.addMessage(message);
                adapter.notifyDataSetChanged();

                //add to FB
                final OnTransactionComplete<Void> onTransactionComplete = new OnTransactionComplete<Void>() {

                    @Override
                    public void onCommit(Void v) {
                        //finish();
                    }

                    @Override
                    public void onAbort(Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    };
                };
                messagesRepository.addMessage(message, onTransactionComplete);

                recyclerView.smoothScrollToPosition(adapterFireBchat.getItemCount());

            }
        }, DELAY_INSERT_UPDATE);
    }

    @Override
    public Loader<List<MessageItem>> onCreateLoader(int id, Bundle args) {

        Loader<List<MessageItem>> mLoader = new Loader<List<MessageItem>>(getActivity()){
            @Override
            protected void onStartLoading() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(DELAY_GET_FROM_SOURCE);
                        }catch (InterruptedException ex){
                            ex.printStackTrace();
                        }
                        deliverResult(getDialogMessageItems());
                    }
                }).start();
            }

            @Override
            protected void onStopLoading() {}
        };

        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<MessageItem>> loader, final List<MessageItem> data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initRecyclerChatView(data);
                hideProgressLoader();
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<List<MessageItem>> loader) {}

    /** ProgressDialog - show & hide */
    public void showProgressLoader(){
        progress = new ProgressDialog(getActivity());
        progress.setTitle(getString(R.string.chat_progress_title));
        progress.setMessage(getString(R.string.chat_progress_message));
        progress.setCancelable(false);
        progress.show();
    }

    public void hideProgressLoader(){
        progress.dismiss();
    }

}

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

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;
import java.util.Locale;

import tinkoff.androidcourse.model.db.MessageItem;
import tinkoff.androidcourse.model.db.MessageItem_Table;
import tinkoff.androidcourse.ui.widgets.SendMessageCompoundView;

/** Show list of messages in selected chat group */
public class ChatFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<MessageItem>>{

    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private SendMessageCompoundView sView;
    ProgressDialog progress;
    private View mView;

    public final static String ARG_DIALOG_ID = "DialogID";
    private long chatId = -1L;
    private static final int DELAY_INSERT_UPDATE = 2000;
    private static final int DELAY_GET_FROM_SOURCE = 7000;

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

        initSendMessageView(view);
        this.mView = view;

        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity().findViewById(R.id.toolbar) != null){
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.chat_fragment_set_title) + String.format(Locale.getDefault(), "%d", chatId));
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
                addMessageItemToChat(message);
                sView.setSendState();
            }
        });
    }

    /** //TODO: we didn't implement user_id feature (no DB_table, only loginName...) -> set default 1
     * */
    private void addMessageItemToChat(String message){
        MessageItem messageItem = new MessageItem(
                message,
                1,
                chatId);

        updateMessages(messageItem);
    }

    private void initRecyclerChatView(List<MessageItem> dataSet) {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //setting chat from bottom
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatAdapter(dataSet, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(),
                        getString(R.string.chat_toast_id) + adapter.getItemId(position),
                        Toast.LENGTH_SHORT).show();
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

    private void updateMessages(final MessageItem message){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                FlowManager.getModelAdapter(MessageItem.class).save(message);
                adapter.addMessage(message);
                recyclerView.smoothScrollToPosition(0);
                adapter.notifyDataSetChanged();
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

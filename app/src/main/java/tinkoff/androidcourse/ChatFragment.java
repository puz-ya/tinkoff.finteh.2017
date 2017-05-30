package tinkoff.androidcourse;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import tinkoff.androidcourse.firebase.MessagesRepository;
import tinkoff.androidcourse.firebase.OnTransactionComplete;
import tinkoff.androidcourse.model.db.DialogItem;
import tinkoff.androidcourse.model.db.MessageItem;
import tinkoff.androidcourse.model.db.User;
import tinkoff.androidcourse.ui.KeyboardHide;
import tinkoff.androidcourse.ui.widgets.SendMessageCompoundView;

/** Show list of messages in selected chat group */
public class ChatFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Query>
{

    private RecyclerView recyclerView;

    private SendMessageCompoundView sView;
    ProgressDialog progress;
    private View mView;

    public final static String ARG_DIALOG_ID = "DialogID";
    private String chatId = "-1";
    private static final int DELAY_INSERT_UPDATE = 3000;
    private static final int DELAY_GET_FROM_SOURCE = 3000;

    private DatabaseReference dialogsReference;
    private DatabaseReference messagesReference;
    private ValueEventListener mPostListener;
    private ChatAdapter chatAdapter;

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


        // Initialize Database
        dialogsReference = FirebaseDatabase.getInstance().getReference()
                .child("dialogs").child(chatId);
        messagesReference = FirebaseDatabase.getInstance().getReference()
                .child("messages").child(chatId);


        initSendMessageView(view);
        this.mView = view;

        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showProgressLoader();
        getLoaderManager().initLoader(0, null, this);

        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        //setting chat from bottom
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        // Add value event listener to the post
        ValueEventListener dialogListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get DialogItem object and update the UI
                DialogItem dialogItem = dataSnapshot.getValue(DialogItem.class);

                if (getActivity().findViewById(R.id.toolbar) != null){
                    Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                    toolbar.setTitle(getString(R.string.chat_fragment_set_title) + String.format(Locale.getDefault(), "%s", dialogItem.getTitle()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(getActivity(), "Failed to load dialog info", Toast.LENGTH_SHORT).show();
            }
        };
        dialogsReference.addValueEventListener(dialogListener);

        // must keep copy of post listener so we can remove it on stop
        this.mPostListener = dialogListener;

        // Listen for messages
        chatAdapter = new ChatAdapter(getActivity(), messagesReference);
        recyclerView.setAdapter(chatAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
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


    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            dialogsReference.removeEventListener(mPostListener);
        }

        // Clean up messages listener
        chatAdapter.cleanupListener();
    }


    /** set proper message, user ID, chat ID for this message
     * @param message - text from user input
     * */
    private void addMessageItemToChat(String message){

        final String messageFinal = message;
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get user information
                        User user = dataSnapshot.getValue(User.class);
                        String authorName = user.name;

                        DateFormat df = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss", Locale.getDefault());
                        String time = df.format(Calendar.getInstance().getTime());

                        // new message object
                        MessageItem messageItem = new MessageItem(uid, authorName, messageFinal, chatId, time);

                        // push the message, it will appear at the bottom
                        messagesReference.push().setValue(messageItem);
                        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

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

    @Override
    public void onDestroy(){
        super.onDestroy();

        progress.dismiss();
    }

    /** Empty loader for progressDialog */

    @Override
    public Loader<Query> onCreateLoader(int id, Bundle args) {

        Loader<Query> mLoader = new Loader<Query>(getActivity()){
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
                        //useless thing just for loader
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("dialogs");
                        Query query = ref.orderByChild("creation_time");
                        deliverResult(query);
                    }
                }).start();
            }

            @Override
            protected void onStopLoading() {}
        };

        return mLoader;
    }
    @Override
    public void onLoadFinished(Loader<Query> loader, final Query data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressLoader();
            }
        });
    }
    @Override
    public void onLoaderReset(Loader<Query> loader) {}
}

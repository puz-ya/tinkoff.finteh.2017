package tinkoff.androidcourse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tinkoff.androidcourse.dialogsAdd.DialogAddActivity;
import tinkoff.androidcourse.firebase.DialogItemValueListener;
import tinkoff.androidcourse.firebase.DialogRepository;
import tinkoff.androidcourse.firebase.OnTransactionComplete;
import tinkoff.androidcourse.model.db.DialogItem;
import tinkoff.androidcourse.model.db.User;

import static android.app.Activity.RESULT_OK;
import static tinkoff.androidcourse.App.ARG_MENU_ID;
import static tinkoff.androidcourse.App.ARG_TITLE;

/** Show list of chat groups */
public class DialogFragment extends Fragment {

    private RecyclerView recyclerView;
    //private DialogAdapter adapter;
    public FirebaseRecyclerAdapter<DialogItem, DialogAdapter.ViewHolder> adapterFB;

    private Button addDialog;

    OnLoadChat mCallback;

    View mView;
    ProgressDialog progress4Dialogs;

    public static final String EXTRA_DIALOG_TITLE = "DIALOG_TITLE";
    public static final String EXTRA_DIALOG_DESCR = "DIALOG_DESCR";
    private static final int REQUEST_CODE_ADD_DIALOG = 55;

    private static final int DELAY_INSERT_UPDATE = 2000;
    private static final int DELAY_GET_FROM_SOURCE = 3000;

    private DatabaseReference mDatabase;
    private DialogRepository dialogRepository = DialogRepository.getInstance();
    private Query query;

    public DialogFragment(){}

    public static DialogFragment newInstance(String title, int menu_id) {
        DialogFragment fragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_MENU_ID, menu_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        addDialog = (Button) view.findViewById(R.id.add_dialog);
        addDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialogItem();
            }
        });

        this.mView = view;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_dialogs);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    /** init LoadManager here: https://developer.android.com/reference/android/app/LoaderManager.html
     * */
    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //start spinning and get the data
        showProgressLoader();

        //set layout to recycler
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        initRecyclerViewFirebase();

        //hide progress loader (spinning)
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                hideProgressLoader();
            }
        }, DELAY_GET_FROM_SOURCE);
    }

    /**
     * get data from firebase & input into adapeter, then into recyclerview
     */
    private void initRecyclerViewFirebase() {
        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = mDatabase.child("dialogs").limitToFirst(100);

        adapterFB = new FirebaseRecyclerAdapter<DialogItem, DialogAdapter.ViewHolder>(
                DialogItem.class, R.layout.item_chat_dialog, DialogAdapter.ViewHolder.class, postsQuery
        ){
            @Override
            protected void populateViewHolder(final DialogAdapter.ViewHolder viewHolder, final DialogItem model, final int position) {
                final DatabaseReference postRef = getRef(position);

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());

                //get dialog's ID from DB and give it to Chat
                final String postKey = postRef.getKey();

                // Set click listener for the dialog item
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getActivity(),
                                getString(R.string.dialog_toast_id) + postKey,
                                Toast.LENGTH_SHORT).show();

                        // Launch ChatFragment with messages
                        mCallback.startChatScreen(postKey);
                    }
                });

                /*
                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });
                */
            }
        };
        recyclerView.setAdapter(adapterFB);
    }

        /** Start DialogAddActivity with "Compound View" to retrieve Title & Description of the new Dialog
         * */
    private void addDialogItem() {
        Intent intent = new Intent(getActivity(), DialogAddActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_ADD_DIALOG) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Check extra
                if(data.hasExtra(EXTRA_DIALOG_TITLE) && data.hasExtra(EXTRA_DIALOG_DESCR)) {

                    final String title = data.getStringExtra(EXTRA_DIALOG_TITLE);
                    final String body = data.getStringExtra(EXTRA_DIALOG_DESCR);

                    FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();

                    if(userInfo != null) {
                        final String userId = userInfo.getUid();
                        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // Get user value
                                        User user = dataSnapshot.getValue(User.class);

                                        // [START_EXCLUDE]
                                        if (user == null) {
                                            // User is null, error out

                                            Toast.makeText(getActivity(),
                                                    getString(R.string.dialog_add_userinfo_error),
                                                    Toast.LENGTH_SHORT).show();

                                        } else {
                                            // Write new post
                                            writeNewPost(userId, user.name, title, body);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                    }
                }
            }
        }
    }

    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new dialog at /user-dialogs/$userid/$dialogid and at
        // /dialogs/$dialogid simultaneously
        String key = mDatabase.child("dialogs").push().getKey();

        DateFormat df = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss", Locale.getDefault());
        String creation_time = df.format(Calendar.getInstance().getTime());

        DialogItem dialogItem = new DialogItem(userId, username, title, body, creation_time, "");
        Map<String, Object> dialogValues = dialogItem.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/dialogs/" + key, dialogValues);
        childUpdates.put("/user-dialogs/" + userId + "/" + key, dialogValues);

        mDatabase.updateChildren(childUpdates);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnLoadChat) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement all interfaces!");
        }
    }

    public interface OnLoadChat{
        void startChatScreen(long position);
        void startChatScreen(String position);
    }

    /** ProgressDialog - show & hide */
    public void showProgressLoader(){
        progress4Dialogs = new ProgressDialog(getActivity());
        progress4Dialogs.setTitle(getString(R.string.dialog_progress_title));
        progress4Dialogs.setMessage(getString(R.string.dialog_progress_message));
        progress4Dialogs.setCancelable(false);
        progress4Dialogs.show();
    }

    public void hideProgressLoader(){
        progress4Dialogs.dismiss();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(adapterFB != null) {
            adapterFB.cleanup();
        }
    }

}

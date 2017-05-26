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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import tinkoff.androidcourse.dialogsAdd.DialogAddActivity;
import tinkoff.androidcourse.firebase.DialogRepository;
import tinkoff.androidcourse.firebase.OnTransactionComplete;
import tinkoff.androidcourse.model.db.DialogItem;

import static android.app.Activity.RESULT_OK;
import static tinkoff.androidcourse.App.ARG_MENU_ID;
import static tinkoff.androidcourse.App.ARG_TITLE;

/** Show list of chat groups */
public class DialogFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<DialogItem>>{

    private RecyclerView recyclerView;
    private DialogAdapter adapter;
    public FirebaseRecyclerAdapter<DialogItem, DialogAdapter.ViewHolder> FBadapter;

    private Button addDialog;

    OnLoadChat mCallback;

    View mView;
    ProgressDialog progress4Dialogs;

    public static final String EXTRA_DIALOG_TITLE = "DIALOG_TITLE";
    public static final String EXTRA_DIALOG_DESCR = "DIALOG_DESCR";
    private static final int REQUEST_CODE_ADD_DIALOG = 55;

    private static final int DELAY_INSERT_UPDATE = 1000;
    private static final int DELAY_GET_FROM_SOURCE = 3000;

    private DialogRepository dialogRepository = DialogRepository.getInstance();

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

        return view;
    }

    /** init LoadManager here: https://developer.android.com/reference/android/app/LoaderManager.html
     * */
    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //start spinning and get the data
        showProgressLoader();
        getLoaderManager().initLoader(0, null, this);
    }

    private void initRecyclerView(List<DialogItem> dataSet) {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_dialogs);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DialogAdapter(dataSet, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(),
                        getString(R.string.dialog_toast_id) + adapter.getItemId(position),
                        Toast.LENGTH_SHORT).show();

                //get dialog's ID from DB (adapter) and give it to Chat
                long chatId = adapter.getItemId(position);
                mCallback.startChatScreen(chatId);
            }
        });

        //set Firebase adapter
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("dialogs");
        FBadapter = new FirebaseRecyclerAdapter<DialogItem, DialogAdapter.ViewHolder>(
                DialogItem.class, R.layout.item_chat_dialog, DialogAdapter.ViewHolder.class, ref
        ){
            @Override
            public void populateViewHolder(DialogAdapter.ViewHolder viewHolder, DialogItem model, final int FBpos) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());

                viewHolder.getmView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),
                                getString(R.string.dialog_toast_id) + FBadapter.getItem(FBpos).getId(),
                                Toast.LENGTH_SHORT).show();

                        //get dialog's ID from DB (adapter) and give it to Chat
                        long chatId = FBadapter.getItem(FBpos).getId();
                        mCallback.startChatScreen(chatId);
                    }
                });
            }

        };

        //set adapter to RecyclerView
        recyclerView.setAdapter(FBadapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
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

                    DialogItem dialogItem = new DialogItem(
                            data.getStringExtra(EXTRA_DIALOG_TITLE),
                            data.getStringExtra(EXTRA_DIALOG_DESCR));
                    updateDialogs(dialogItem);
                }
            }
        }
    }

    private void updateDialogs(final DialogItem dialogItem){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                FlowManager.getModelAdapter(DialogItem.class).save(dialogItem);
                final OnTransactionComplete<Void> onTransactionComplete = new OnTransactionComplete() {

                    @Override
                    public void onCommit(Object result) {
                        //finish();
                    }

                    @Override
                    public void onAbort(Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    };
                };
                dialogRepository.addDialog(dialogItem, onTransactionComplete);

                adapter.addDialog(dialogItem);
                adapter.notifyDataSetChanged();
            }
        }, DELAY_INSERT_UPDATE);
    }

    @Override
    public Loader<List<DialogItem>> onCreateLoader(int id, Bundle args) {

        Loader<List<DialogItem>> mLoader = new Loader<List<DialogItem>>(getActivity()){
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
                        deliverResult(getPreviousDialogItems());
                    }
                }).start();
            }

            @Override
            protected void onStopLoading() {}
        };

        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<DialogItem>> loader, final List<DialogItem> data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initRecyclerView(data);
                hideProgressLoader();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<DialogItem>> loader) {}

    @NonNull
    private List<DialogItem> getPreviousDialogItems() {
        List<DialogItem> itemList = SQLite.select()
                .from(DialogItem.class)
                .queryList();
        //itemList is redundant, but useful while debugging

        //TODO: here we will get List from Firebase

        return itemList;
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

}

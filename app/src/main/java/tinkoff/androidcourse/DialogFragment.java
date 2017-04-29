package tinkoff.androidcourse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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

import static android.app.Activity.RESULT_OK;

/** Show list of chat groups */
public class DialogFragment extends Fragment {

    private RecyclerView recyclerView;
    private DialogsAdapter adapter;
    private Button addDialog;

    OnLoadChat mCallback;

    public static final String EXTRA_DIALOG_TITLE = "DIALOG_TITLE";
    public static final String EXTRA_DIALOG_DESCR = "DIALOG_DESCR";
    private static final int REQUEST_CODE_ADD_DIALOG = 55;

    public DialogFragment(){

    }

    public static DialogFragment newInstance(String title) {
        DialogFragment fragment = new DialogFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //title = getArguments().getString(ARG_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initRecyclerView(view);
        List<DialogItem> dialogItems = getPreviousDialogItems();
        adapter.setItems(dialogItems);
        addDialog = (Button) view.findViewById(R.id.add_dialog);
        addDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialogItem();
            }
        });

        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_dialogs);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DialogsAdapter(new ArrayList<DialogItem>(), new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), "position = " + position, Toast.LENGTH_SHORT).show();
                mCallback.startChatScreen(adapter.getItemId(position));
            }
        });
        recyclerView.setAdapter(adapter);
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
                            "Title is " + data.getStringExtra(EXTRA_DIALOG_TITLE),
                            "Description is " + data.getStringExtra(EXTRA_DIALOG_DESCR));
                    FlowManager.getModelAdapter(DialogItem.class).save(dialogItem);
                    adapter.addDialog(dialogItem);
                }
            }
        }
    }

    @NonNull
    private List<DialogItem> getPreviousDialogItems() {
        return SQLite.select().from(DialogItem.class).queryList();
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

}

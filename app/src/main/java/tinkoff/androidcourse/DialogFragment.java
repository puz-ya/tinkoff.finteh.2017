package tinkoff.androidcourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/** Show list of chat groups */
public class DialogFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private static final String EXTRA_ID = "CHAT_ID";

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
        //TextView textViewTitle = (TextView) view.findViewById(R.id.text_view_stub);
        //textViewTitle.setText(title);

        initRecyclerView(view);

        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_dialogs);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        adapter = new DialogsAdapter(createDataset(), new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), "position = " + position, Toast.LENGTH_SHORT).show();
                startChatScreen(adapter.getItemId(position));
            }
        });
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private List<DialogItem> createDataset() {
        List<DialogItem> list = new ArrayList<>();
        list.add(new DialogItem("Dialog1", "desc1"));
        list.add(new DialogItem("Dialog2", "desc"));
        list.add(new DialogItem("Dialog3", "desc3"));
        list.add(new DialogItem("Dialog4", "desc"));
        list.add(new DialogItem("Dialog5", "desc"));
        list.add(new DialogItem("Dialog6", "desc"));
        list.add(new DialogItem("Dialog7", "desc7"));
        return list;
    }

    private void startChatScreen(long pos) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(EXTRA_ID, pos);
        startActivity(intent);
    }
}

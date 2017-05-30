package tinkoff.androidcourse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tinkoff.androidcourse.model.db.MessageItem;

/**
 * Created on 30.05.2017
 *
 * @author Puzino Yury
 */

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewChatHolder> {

    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private List<String> messagesIds = new ArrayList<>();
    private List<MessageItem> messages = new ArrayList<>();

    private static final int USER_MESSAGE = 666;
    private static final int OTHERS_MESSAGE = 999;

    public ChatAdapter(final Context context, DatabaseReference ref) {
        mContext = context;
        mDatabaseReference = ref;

        // Create child event listener
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                // A new message has been added, add it to the displayed list
                MessageItem messageItem = dataSnapshot.getValue(MessageItem.class);

                // Update RecyclerView
                messagesIds.add(dataSnapshot.getKey());
                messages.add(messageItem);
                notifyItemInserted(messages.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                // A message has changed, use the key to determine if we are displaying this message
                MessageItem newMessage = dataSnapshot.getValue(MessageItem.class);
                String messageKey = dataSnapshot.getKey();

                int messageIndex = messagesIds.indexOf(messageKey);
                if (messageIndex > -1) {
                    // Replace with the new data
                    messages.set(messageIndex, newMessage);

                    // Update the RecyclerView
                    notifyItemChanged(messageIndex);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A message has changed, use the key to determine if we are displaying this message and if so remove it
                String messageKey = dataSnapshot.getKey();

                int messageIndex = messagesIds.indexOf(messageKey);
                if (messageIndex > -1) {
                    // Remove data from the list
                    messagesIds.remove(messageIndex);
                    messages.remove(messageIndex);

                    // Update the RecyclerView
                    notifyItemRemoved(messageIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A message has changed position, use the key to determine if we are
                // displaying this message and if so move it.
                MessageItem movedMessage = dataSnapshot.getValue(MessageItem.class);
                String messageKey = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addChildEventListener(childEventListener);

        // Store reference to listener so it can be removed on app stop
        mChildEventListener = childEventListener;
    }

    public static class ViewChatHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public TextView time;
        public TextView userId;
        public String gID;
        View mView;

        public ViewChatHolder(View view) {
            super(view);
            this.text = (TextView) view.findViewById(R.id.tv_message_text);
            this.time = (TextView) view.findViewById(R.id.tv_message_time);
            this.userId = (TextView) view.findViewById(R.id.tv_message_username);
            this.mView = view;
        }

        public View getmView() {
            return mView;
        }

        public void setText(String text) {
            this.text.setText(text);
        }

        public void setTime(String time) {
            this.time.setText(time);
        }

        public void setUserId(String userId) {
            this.userId.setText(userId);
        }

        public String getgID() {
            return gID;
        }

        public void setgID(String gID) {
            this.gID = gID;
        }

        public ViewChatHolder(View view, OnItemClickListener listener) {
            super(view);

            text = (TextView) view.findViewById(R.id.tv_message_text);
            time = (TextView) view.findViewById(R.id.tv_message_time);
            userId = (TextView) view.findViewById(R.id.tv_message_username);
            setListener(listener);
        }

        private void setListener(final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(ViewChatHolder holder, int position) {
        holder.text.setText(messages.get(position).getText());
        holder.time.setText(messages.get(position).getCreation_time());
        holder.userId.setText(String.format(Locale.getDefault(),"%s", messages.get(position).getId_author()) );
    }

    @Override
    public ViewChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);

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
        return new ViewChatHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        // Simply returning an integer to use in onCreateViewHolder.
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        MessageItem messageItem = messages.get(position);
        if (userInfo != null){
            if (messageItem.getId().equals(userInfo.getUid())){
                return USER_MESSAGE;
            }else{
                return OTHERS_MESSAGE;
            }
        }
        return OTHERS_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }


}
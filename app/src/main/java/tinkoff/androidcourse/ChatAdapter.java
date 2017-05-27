package tinkoff.androidcourse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import tinkoff.androidcourse.model.db.DialogItem;
import tinkoff.androidcourse.model.db.MessageItem;

/**
 * Created on 23.03.2017
 *
 * @author Puzino Yury
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewChatHolder> {

    private LayoutInflater inflater = null;
    private List<MessageItem> dataset;
    private OnItemClickListener clickListener;

    private static final int USER_MESSAGE = 666;
    private static final int OTHERS_MESSAGE = 999;

    public ChatAdapter(List<MessageItem> dataset, OnItemClickListener clickListener) {
        this.dataset = dataset;
        this.clickListener = clickListener;
    }

    @Override
    public ChatAdapter.ViewChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        inflater = LayoutInflater.from(parent.getContext());

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

        return new ChatAdapter.ViewChatHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewChatHolder holder, int position) {
        holder.text.setText(dataset.get(position).getText());
        holder.time.setText(dataset.get(position).getCreation_time());
        holder.userId.setText(String.format(Locale.getDefault(),"%s", dataset.get(position).getId_author()) );
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setItems(List<MessageItem> messageItems) {
        dataset = messageItems;
        notifyDataSetChanged();
    }

    public void addMessage(MessageItem messageItem) {
        dataset.add(0, messageItem);
        notifyItemInserted(0);
    }

    /* get correct type based on User
    * in version with local DB users was not defined, so it was only 1 user with id == "1"
    * */
    @Override
    public int getItemViewType(int position) {

        MessageItem messageItem = dataset.get(position);

        if (messageItem != null){
            if(messageItem.getId_author().equals("1")){
                return USER_MESSAGE;
            }
            return OTHERS_MESSAGE;
        }

        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return dataset.get(position).getId();
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
}

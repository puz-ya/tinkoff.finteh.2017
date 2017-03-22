package tinkoff.androidcourse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
        if (viewType == USER_MESSAGE) {
            view = inflater.inflate(R.layout.item_chat_messages_right, parent, false);
        }
        else if (viewType == OTHERS_MESSAGE) {
            view = inflater.inflate(R.layout.item_chat_messages_left, parent, false);
        }

        return new ChatAdapter.ViewChatHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewChatHolder holder, int position) {
        holder.text.setText(dataset.get(position).getText());
        holder.time.setText(dataset.get(position).getDate());
        holder.username.setText(dataset.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    /* get correct type based on User */
    @Override
    public int getItemViewType(int position) {

        MessageItem messageItem = dataset.get(position);

        if (messageItem != null){
            if(messageItem.getUsername().equals("user1")){
                return USER_MESSAGE;
            }
            return OTHERS_MESSAGE;
        }

        return super.getItemViewType(position);
    }

    public static class ViewChatHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public TextView time;
        public TextView username;

        public ViewChatHolder(View view, OnItemClickListener listener) {
            super(view);

            text = (TextView) view.findViewById(R.id.tv_message_text);
            time = (TextView) view.findViewById(R.id.tv_message_time);
            username = (TextView) view.findViewById(R.id.tv_message_username);
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

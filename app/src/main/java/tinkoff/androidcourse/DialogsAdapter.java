package tinkoff.androidcourse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tinkoff.androidcourse.model.db.DialogItem;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder> {

    private List<DialogItem> dataset;
    private OnItemClickListener clickListener;

    public DialogsAdapter(List<DialogItem> dataset, OnItemClickListener clickListener) {
        this.dataset = dataset;
        this.clickListener = clickListener;
    }

    @Override
    public DialogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_dialog, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DialogItem dialogItem = dataset.get(position);
        holder.title.setText(dialogItem.getTitle());
        holder.desc.setText(dialogItem.getDesc());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addDialog(DialogItem dialogItem) {
        dataset.add(dialogItem);
        notifyItemInserted(dataset.size());
    }

    public void setItems(List<DialogItem> dialogItems) {
        dataset = dialogItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView desc;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_dialog_title);
            desc = (TextView) view.findViewById(R.id.tv_dialog_desc);
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
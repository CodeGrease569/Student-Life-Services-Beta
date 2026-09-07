package com.example.studentlife.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context context;
    private final List<NotificationItem> items;
    private final DatabaseHelper db;

    public NotificationAdapter(Context context, List<NotificationItem> items, DatabaseHelper db) {
        this.context = context;
        this.items = new ArrayList<>(items);
        this.db = db;
    }

    public void updateData(List<NotificationItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem item = items.get(position);
        holder.tvNotifTitle.setText(item.getTitle());
        holder.tvNotifMessage.setText(item.getMessage());
        holder.tvNotifTime.setText(item.getTimestamp());

        if (item.isRead()) {
            holder.dotUnread.setVisibility(View.GONE);
        } else {
            holder.dotUnread.setVisibility(View.VISIBLE);
        }

        // Set type icon
        if ("scholarship".equalsIgnoreCase(item.getType())) {
            holder.ivNotifIcon.setImageResource(R.drawable.ic_mortarboard);
        } else if ("document".equalsIgnoreCase(item.getType())) {
            holder.ivNotifIcon.setImageResource(R.drawable.ic_document);
        } else if ("concern".equalsIgnoreCase(item.getType()) || "inquiry".equalsIgnoreCase(item.getType())) {
            holder.ivNotifIcon.setImageResource(R.drawable.ic_chat);
        } else {
            holder.ivNotifIcon.setImageResource(R.drawable.ic_bell);
        }

        holder.itemView.setOnClickListener(v -> {
            if (!item.isRead()) {
                db.markNotificationAsRead(item.getId());
                item.setRead(true);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNotifIcon;
        TextView tvNotifTitle, tvNotifMessage, tvNotifTime;
        View dotUnread;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNotifIcon = itemView.findViewById(R.id.ivNotifIcon);
            tvNotifTitle = itemView.findViewById(R.id.tvNotifTitle);
            tvNotifMessage = itemView.findViewById(R.id.tvNotifMessage);
            tvNotifTime = itemView.findViewById(R.id.tvNotifTime);
            dotUnread = itemView.findViewById(R.id.dotUnread);
        }
    }
}

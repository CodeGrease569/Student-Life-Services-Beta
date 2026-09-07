package com.example.studentlife.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.studentlife.activities.SubmitRequirementActivity;
import com.example.studentlife.models.Requirement;

import java.util.List;

public class PendingActionAdapter extends RecyclerView.Adapter<PendingActionAdapter.ViewHolder> {

    private final Context context;
    private final List<Requirement> items;

    public PendingActionAdapter(Context context, List<Requirement> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pending_action, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Requirement req = items.get(position);
        holder.tvActionTitle.setText(req.getTitle());
        holder.tvDueDate.setText("Due " + req.getDueDate());

        if (position == 0) {
            holder.dotIndicator.setBackgroundResource(R.drawable.bg_dot_red);
        } else {
            holder.dotIndicator.setBackgroundResource(R.drawable.bg_dot_yellow);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubmitRequirementActivity.class);
            intent.putExtra("requirement_id", req.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View dotIndicator;
        TextView tvActionTitle, tvDueDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dotIndicator = itemView.findViewById(R.id.dotIndicator);
            tvActionTitle = itemView.findViewById(R.id.tvActionTitle);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
        }
    }
}

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
import com.example.studentlife.activities.RequestDetailActivity;
import com.example.studentlife.models.StudentRequest;

import java.util.ArrayList;
import java.util.List;

public class RequestHistoryAdapter extends RecyclerView.Adapter<RequestHistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<StudentRequest> fullList;
    private final List<StudentRequest> displayList;

    public RequestHistoryAdapter(Context context, List<StudentRequest> list) {
        this.context = context;
        this.fullList = new ArrayList<>(list);
        this.displayList = new ArrayList<>(list);
    }

    public void updateData(List<StudentRequest> newList) {
        fullList.clear();
        fullList.addAll(newList);
        displayList.clear();
        displayList.addAll(newList);
        notifyDataSetChanged();
    }

    public void filter(String query, String statusFilter) {
        displayList.clear();
        String q = query != null ? query.toLowerCase().trim() : "";
        for (StudentRequest r : fullList) {
            boolean matchesQuery = q.isEmpty()
                    || r.getReferenceNumber().toLowerCase().contains(q)
                    || r.getRequestType().toLowerCase().contains(q)
                    || r.getCategory().toLowerCase().contains(q);

            boolean matchesStatus = statusFilter == null
                    || statusFilter.equalsIgnoreCase("All")
                    || r.getStatus().equalsIgnoreCase(statusFilter);

            if (matchesQuery && matchesStatus) {
                displayList.add(r);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentRequest req = displayList.get(position);
        holder.tvHistoryType.setText(req.getRequestType());
        holder.tvHistoryRef.setText(req.getReferenceNumber());
        holder.tvHistoryDates.setText(req.getCategory() + "  ·  Submitted " + req.getSubmittedDate() + "  ·  Updated " + req.getUpdatedDate());
        holder.tvHistoryStatus.setText(req.getStatus());

        RecentRequestAdapter.applyStatusBadgeStyle(holder.tvHistoryStatus, req.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RequestDetailActivity.class);
            intent.putExtra("ref_number", req.getReferenceNumber());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistoryType, tvHistoryRef, tvHistoryDates, tvHistoryStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistoryType = itemView.findViewById(R.id.tvHistoryType);
            tvHistoryRef = itemView.findViewById(R.id.tvHistoryRef);
            tvHistoryDates = itemView.findViewById(R.id.tvHistoryDates);
            tvHistoryStatus = itemView.findViewById(R.id.tvHistoryStatus);
        }
    }
}

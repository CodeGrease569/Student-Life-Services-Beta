package com.example.studentlife.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.studentlife.activities.RequestDetailActivity;
import com.example.studentlife.models.StudentRequest;

import java.util.List;

public class RecentRequestAdapter extends RecyclerView.Adapter<RecentRequestAdapter.ViewHolder> {

    private final Context context;
    private final List<StudentRequest> requests;

    public RecentRequestAdapter(Context context, List<StudentRequest> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentRequest req = requests.get(position);
        holder.tvRequestType.setText(req.getRequestType());
        holder.tvRefNumber.setText(req.getReferenceNumber());
        holder.tvDate.setText(req.getSubmittedDate());
        holder.tvStatusBadge.setText(req.getStatus());

        applyStatusBadgeStyle(holder.tvStatusBadge, req.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RequestDetailActivity.class);
            intent.putExtra("ref_number", req.getReferenceNumber());
            context.startActivity(intent);
        });
    }

    public static void applyStatusBadgeStyle(TextView tv, String status) {
        if ("Processing".equalsIgnoreCase(status)) {
            tv.setBackgroundResource(R.drawable.bg_status_blue);
            tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.status_blue_text));
        } else if ("Completed".equalsIgnoreCase(status) || "Approved".equalsIgnoreCase(status) || "Active".equalsIgnoreCase(status)) {
            tv.setBackgroundResource(R.drawable.bg_status_green);
            tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.status_green_text));
        } else if ("Under Review".equalsIgnoreCase(status) || "Pending".equalsIgnoreCase(status) || "Submitted".equalsIgnoreCase(status)) {
            tv.setBackgroundResource(R.drawable.bg_status_yellow);
            tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.status_yellow_text));
        } else {
            tv.setBackgroundResource(R.drawable.bg_status_red);
            tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.status_red_text));
        }
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequestType, tvRefNumber, tvDate, tvStatusBadge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestType = itemView.findViewById(R.id.tvRequestType);
            tvRefNumber = itemView.findViewById(R.id.tvRefNumber);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatusBadge = itemView.findViewById(R.id.tvStatusBadge);
        }
    }
}

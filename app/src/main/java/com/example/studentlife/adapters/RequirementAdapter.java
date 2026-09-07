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
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RequirementAdapter extends RecyclerView.Adapter<RequirementAdapter.ViewHolder> {

    private final Context context;
    private final List<Requirement> requirements;

    public RequirementAdapter(Context context, List<Requirement> requirements) {
        this.context = context;
        this.requirements = requirements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_requirement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Requirement req = requirements.get(position);
        holder.tvReqTitle.setText(req.getTitle());
        holder.tvReqDesc.setText(req.getDescription());
        holder.tvReqDueDate.setText("Due: " + req.getDueDate());
        holder.tvReqStatus.setText(req.getStatus());

        RecentRequestAdapter.applyStatusBadgeStyle(holder.tvReqStatus, req.getStatus());

        if ("Approved".equalsIgnoreCase(req.getStatus())) {
            holder.btnUploadReq.setVisibility(View.GONE);
            if (req.getSubmittedDate() != null) {
                holder.tvReqDueDate.setText("Submitted: " + req.getSubmittedDate());
            }
        } else {
            holder.btnUploadReq.setVisibility(View.VISIBLE);
            holder.btnUploadReq.setText("Submit >");
            holder.btnUploadReq.setOnClickListener(v -> {
                Intent intent = new Intent(context, SubmitRequirementActivity.class);
                intent.putExtra("requirement_id", req.getId());
                context.startActivity(intent);
            });
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubmitRequirementActivity.class);
            intent.putExtra("requirement_id", req.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return requirements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReqTitle, tvReqDesc, tvReqDueDate, tvReqStatus;
        MaterialButton btnUploadReq;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReqTitle = itemView.findViewById(R.id.tvReqTitle);
            tvReqDesc = itemView.findViewById(R.id.tvReqDesc);
            tvReqDueDate = itemView.findViewById(R.id.tvReqDueDate);
            tvReqStatus = itemView.findViewById(R.id.tvReqStatus);
            btnUploadReq = itemView.findViewById(R.id.btnUploadReq);
        }
    }
}

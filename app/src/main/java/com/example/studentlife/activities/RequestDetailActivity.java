package com.example.studentlife.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.adapters.RecentRequestAdapter;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.StudentRequest;

public class RequestDetailActivity extends AppCompatActivity {

    private TextView tvType, tvStatusBadge, tvRefNumber, tvCategory, tvSubmittedDate, tvUpdatedDate;
    private TextView tvSubject, tvDescription, tvFileName, tvRemarks;
    private LinearLayout layoutAttachedFile;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.cardDetailBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnDetailDone).setOnClickListener(v -> finish());

        tvType = findViewById(R.id.tvDetailType);
        tvStatusBadge = findViewById(R.id.tvDetailStatusBadge);
        tvRefNumber = findViewById(R.id.tvDetailRefNumber);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvSubmittedDate = findViewById(R.id.tvDetailSubmittedDate);
        tvUpdatedDate = findViewById(R.id.tvDetailUpdatedDate);
        tvSubject = findViewById(R.id.tvDetailSubject);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvFileName = findViewById(R.id.tvDetailFileName);
        tvRemarks = findViewById(R.id.tvDetailRemarks);
        layoutAttachedFile = findViewById(R.id.layoutDetailAttachedFile);

        String refNumber = getIntent().getStringExtra("reference_number");
        StudentRequest request = null;

        if (refNumber != null) {
            request = dbHelper.getRequestByReference(refNumber);
        }

        if (request != null) {
            tvType.setText(request.getType());
            tvStatusBadge.setText(request.getStatus());
            RecentRequestAdapter.applyStatusBadgeStyle(tvStatusBadge, request.getStatus());

            tvRefNumber.setText("Reference: " + request.getReferenceNumber());
            tvCategory.setText(request.getCategory());
            tvSubmittedDate.setText(request.getSubmittedDate());
            tvUpdatedDate.setText(request.getUpdatedDate());

            tvSubject.setText(request.getType());
            tvDescription.setText(request.getDescription());

            if (request.getAttachedFile() != null && !request.getAttachedFile().isEmpty()) {
                layoutAttachedFile.setVisibility(View.VISIBLE);
                tvFileName.setText(request.getAttachedFile());
            } else {
                layoutAttachedFile.setVisibility(View.GONE);
            }

            if (request.getRemarks() != null && !request.getRemarks().isEmpty()) {
                tvRemarks.setText(request.getRemarks());
            } else {
                tvRemarks.setText("Your request is in the official Student Life workflow. Check back for updates.");
            }
        }
    }
}

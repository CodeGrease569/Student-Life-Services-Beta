package com.example.studentlife.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SessionManager sessionManager = new SessionManager(this);

        findViewById(R.id.cardProfileBack).setOnClickListener(v -> finish());

        TextView tvInitials = findViewById(R.id.tvProfileAvatarInitials);
        TextView tvFullName = findViewById(R.id.tvProfileFullName);
        TextView tvStudentId = findViewById(R.id.tvProfileStudentId);
        TextView tvFirstName = findViewById(R.id.tvProfileFirstName);
        TextView tvMiddleName = findViewById(R.id.tvProfileMiddleName);
        TextView tvEmail = findViewById(R.id.tvProfileEmail);
        MaterialButton btnLogout = findViewById(R.id.btnProfileLogout);

        String fullName = sessionManager.getStudentName();
        String firstName = sessionManager.getStudentFirstName();
        String middleName = sessionManager.getStudentMiddleName();
        String studentId = sessionManager.getStudentId();
        String email = sessionManager.getStudentEmail();

        tvFullName.setText(fullName);
        tvStudentId.setText("Student ID: " + studentId);
        tvFirstName.setText(!firstName.isEmpty() ? firstName : (fullName != null ? fullName.split("\\s+")[0] : ""));
        tvMiddleName.setText(!middleName.isEmpty() ? middleName : "—");
        tvEmail.setText(email);

        // Initials
        if (fullName != null && !fullName.isEmpty()) {
            String[] parts = fullName.trim().split("\\s+");
            if (parts.length >= 2) {
                tvInitials.setText(("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase());
            } else {
                tvInitials.setText(fullName.substring(0, Math.min(2, fullName.length())).toUpperCase());
            }
        }

        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Sign Out")
                    .setMessage("Are you sure you want to sign out of Student Life?")
                    .setPositiveButton("Sign Out", (dialog, which) -> {
                        sessionManager.logoutUser();
                        Intent intent = new Intent(ProfileActivity.this, LandingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}

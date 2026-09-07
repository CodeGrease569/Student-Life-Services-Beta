package com.example.studentlife.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user already logged in
        SessionManager session = new SessionManager(this);
        if (session.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_landing);

        MaterialButton btnSignIn = findViewById(R.id.btnLandingSignIn);
        MaterialButton btnSignUp = findViewById(R.id.btnLandingSignUp);

        btnSignIn.setOnClickListener(v -> {
            startActivity(new Intent(LandingActivity.this, LoginActivity.class));
        });

        btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LandingActivity.this, RegisterActivity.class));
        });
    }
}

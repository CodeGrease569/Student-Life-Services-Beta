package com.example.studentlife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.Student;
import com.example.studentlife.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName, etMiddleName, etFullName, etStudentId, etEmail, etPassword;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private boolean isFullNameManuallyEdited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        etFirstName = findViewById(R.id.etRegisterFirstName);
        etMiddleName = findViewById(R.id.etRegisterMiddleName);
        etFullName = findViewById(R.id.etRegisterFullName);
        etStudentId = findViewById(R.id.etRegisterStudentId);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        MaterialButton btnSubmit = findViewById(R.id.btnRegisterSubmit);
        TextView tvGoToSignIn = findViewById(R.id.tvGoToSignIn);

        findViewById(R.id.cardRegisterBack).setOnClickListener(v -> finish());

        // Dynamic auto-sync between First Name, Middle Name, and Full Name
        TextWatcher nameSyncWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!isFullNameManuallyEdited) {
                    String fn = etFirstName.getText().toString().trim();
                    String mn = etMiddleName.getText().toString().trim();
                    StringBuilder combined = new StringBuilder();
                    if (!fn.isEmpty()) combined.append(fn);
                    if (!mn.isEmpty()) {
                        if (combined.length() > 0) combined.append(" ");
                        combined.append(mn);
                    }
                    etFullName.setText(combined.toString());
                }
            }
        };

        etFirstName.addTextChangedListener(nameSyncWatcher);
        etMiddleName.addTextChangedListener(nameSyncWatcher);

        etFullName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                isFullNameManuallyEdited = true;
            }
        });

        btnSubmit.setOnClickListener(v -> attemptRegistration());

        tvGoToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegistration() {
        String firstName = etFirstName.getText().toString().trim();
        String middleName = etMiddleName.getText().toString().trim(); // Optional
        String fullName = etFullName.getText().toString().trim();
        String studentId = etStudentId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError("First name is required");
            etFirstName.requestFocus();
            return;
        }

        // If Full Name was left empty, build it from First and optional Middle Name
        if (TextUtils.isEmpty(fullName)) {
            if (!middleName.isEmpty()) {
                fullName = firstName + " " + middleName;
            } else {
                fullName = firstName;
            }
            etFullName.setText(fullName);
        }

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(studentId)) {
            etStudentId.setError("Student ID is required");
            etStudentId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        Student newStudent = new Student(studentId, firstName, middleName, fullName, email, password);
        boolean success = dbHelper.registerStudent(newStudent);

        if (success) {
            sessionManager.createLoginSession(studentId, fullName, firstName, middleName, email);
            Toast.makeText(this, "Registration successful! Welcome to Student Life.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Student ID already exists. Please sign in.", Toast.LENGTH_SHORT).show();
        }
    }
}


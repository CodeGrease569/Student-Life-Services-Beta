package com.example.studentlife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.Student;
import com.example.studentlife.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etStudentId, etEmail, etCourse, etPassword;
    private Spinner spYearLevel;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        etName = findViewById(R.id.etRegisterName);
        etStudentId = findViewById(R.id.etRegisterStudentId);
        etEmail = findViewById(R.id.etRegisterEmail);
        etCourse = findViewById(R.id.etRegisterCourse);
        spYearLevel = findViewById(R.id.spRegisterYear);
        etPassword = findViewById(R.id.etRegisterPassword);
        MaterialButton btnSubmit = findViewById(R.id.btnRegisterSubmit);
        TextView tvGoToSignIn = findViewById(R.id.tvGoToSignIn);

        findViewById(R.id.cardRegisterBack).setOnClickListener(v -> finish());

        // Setup year level spinner
        String[] yearLevels = {"1st Year", "2nd Year", "3rd Year", "4th Year", "5th Year", "Graduate"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, yearLevels);
        spYearLevel.setAdapter(adapter);
        spYearLevel.setSelection(2); // default 3rd Year

        btnSubmit.setOnClickListener(v -> attemptRegistration());

        tvGoToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegistration() {
        String name = etName.getText().toString().trim();
        String studentId = etStudentId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String course = etCourse.getText().toString().trim();
        String yearLevel = spYearLevel.getSelectedItem().toString();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Full name is required");
            etName.requestFocus();
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

        if (TextUtils.isEmpty(course)) {
            etCourse.setError("Course / Program is required");
            etCourse.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        Student newStudent = new Student(studentId, name, email, password, course, yearLevel);
        boolean success = dbHelper.registerStudent(newStudent);

        if (success) {
            sessionManager.createLoginSession(studentId, name, email, course, yearLevel);
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

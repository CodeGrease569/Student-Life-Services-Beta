package com.example.studentlife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.Student;
import com.example.studentlife.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private EditText etIdOrEmail, etPassword;
    private CheckBox cbRememberMe;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        etIdOrEmail = findViewById(R.id.etLoginIdOrEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        MaterialButton btnSignIn = findViewById(R.id.btnLoginSubmit);
        TextView tvGoToSignUp = findViewById(R.id.tvGoToSignUp);
        TextView tvDemoStudent = findViewById(R.id.tvDemoStudent);
        TextView tvDemoAdmin = findViewById(R.id.tvDemoAdmin);

        findViewById(R.id.cardBack).setOnClickListener(v -> finish());

        // Tap demo accounts to autofill
        tvDemoStudent.setOnClickListener(v -> {
            etIdOrEmail.setText("student@suu.edu.ph");
            etPassword.setText("password123");
        });

        tvDemoAdmin.setOnClickListener(v -> {
            etIdOrEmail.setText("admin@suu.edu.ph");
            etPassword.setText("admin123");
        });

        btnSignIn.setOnClickListener(v -> attemptLogin());

        tvGoToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private void attemptLogin() {
        String idOrEmail = etIdOrEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(idOrEmail)) {
            etIdOrEmail.setError("Please enter your Student ID or Email");
            etIdOrEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }

        Student student = dbHelper.authenticateStudent(idOrEmail, password);
        if (student != null) {
            sessionManager.createLoginSession(
                    student.getStudentId(),
                    student.getFullName(),
                    student.getEmail(),
                    student.getCourse(),
                    student.getYearLevel()
            );

            Toast.makeText(this, "Welcome, " + student.getFullName() + "!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            // Demo fallback if student not in DB, create and login
            sessionManager.createLoginSession(
                    "2023-00456",
                    "Maria Santos",
                    idOrEmail.contains("@") ? idOrEmail : "student@suu.edu.ph",
                    "BS Computer Science",
                    "3rd Year"
            );
            Toast.makeText(this, "Welcome, Maria Santos!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}

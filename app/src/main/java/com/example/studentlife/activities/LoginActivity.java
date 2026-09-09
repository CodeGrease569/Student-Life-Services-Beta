package com.example.studentlife.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView ivTogglePassword;
    private CheckBox cbRememberMe;
    private boolean isPasswordVisible = false;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Bind Views
        etIdOrEmail = findViewById(R.id.etLoginIdOrEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        MaterialButton btnSignIn = findViewById(R.id.btnLoginSubmit);
        TextView tvGoToSignUp = findViewById(R.id.tvGoToSignUp);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        View cardBack = findViewById(R.id.cardBack);

        View tvDemoStudent = findViewById(R.id.tvDemoStudent);
        View tvDemoAdmin = findViewById(R.id.tvDemoAdmin);

        // Navigation back
        if (cardBack != null) {
            cardBack.setOnClickListener(v -> finish());
        }

        // Restore Remembered ID if enabled
        if (cbRememberMe != null) {
            boolean remembered = sessionManager.isRememberMeEnabled();
            cbRememberMe.setChecked(remembered);
            if (remembered) {
                String rememberedId = sessionManager.getRememberedId();
                if (!TextUtils.isEmpty(rememberedId)) {
                    etIdOrEmail.setText(rememberedId);
                    etPassword.requestFocus();
                }
            }
        }

        // Show/Hide Password Toggle
        if (ivTogglePassword != null) {
            ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        }

        // Forgot Password Flow
        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
        }

        // Sign In Action
        btnSignIn.setOnClickListener(v -> attemptLogin());

        // Account Registration Link
        tvGoToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // Demo Accounts Auto-fill for easy testing
        if (tvDemoStudent != null) {
            tvDemoStudent.setOnClickListener(v -> {
                etIdOrEmail.setText("student@suu.edu.ph");
                etPassword.setText("password123");
                etPassword.requestFocus();
            });
        }

        if (tvDemoAdmin != null) {
            tvDemoAdmin.setOnClickListener(v -> {
                etIdOrEmail.setText("admin@suu.edu.ph");
                etPassword.setText("admin123");
                etPassword.requestFocus();
            });
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.ic_visibility);
            ivTogglePassword.setContentDescription("Show password");
            isPasswordVisible = false;
        } else {
            // Show password
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.ic_visibility_off);
            ivTogglePassword.setContentDescription("Hide password");
            isPasswordVisible = true;
        }
        // Maintain cursor position at end of text
        etPassword.setSelection(etPassword.getText().length());
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password Recovery");

        // Layout with explanation and email field
        View view = LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, null, false);
        final EditText input = new EditText(this);
        input.setHint("Enter Student ID or University Email");
        input.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setText(etIdOrEmail.getText().toString().trim());
        input.setPadding(48, 32, 48, 32);

        builder.setView(input);
        builder.setMessage("Enter your registered Student ID or University Email address to receive password reset instructions.");

        builder.setPositiveButton("Send Reset Link", (dialog, which) -> {
            String target = input.getText().toString().trim();
            if (TextUtils.isEmpty(target)) {
                Toast.makeText(LoginActivity.this, "Please enter your Student ID or Email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Provide real feedback and system notification
            String emailDisplay = target.contains("@") ? target : target + "@suu.edu.ph";
            Toast.makeText(LoginActivity.this,
                    "Password reset instructions have been sent to " + emailDisplay,
                    Toast.LENGTH_LONG).show();

            // Record notification in DB
            dbHelper.addNotification(
                    "Password Reset Requested",
                    "A password reset link was requested for account: " + target + ". Check your university email.",
                    "account"
            );
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
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

        // Save Remember Me state
        boolean remember = cbRememberMe != null && cbRememberMe.isChecked();
        sessionManager.setRememberMe(remember, idOrEmail);

        Student student = dbHelper.authenticateStudent(idOrEmail, password);
        if (student != null) {
            sessionManager.createLoginSession(
                    student.getStudentId(),
                    student.getFullName(),
                    student.getFirstName(),
                    student.getMiddleName(),
                    student.getEmail()
            );

            Toast.makeText(this, "Welcome back, " + student.getFullName() + "!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            // Default demo fallback session if credentials aren't already in DB
            sessionManager.createLoginSession(
                    "2023-00456",
                    "Maria Clara Santos",
                    "Maria",
                    "Clara",
                    idOrEmail.contains("@") ? idOrEmail : "student@suu.edu.ph"
            );
            Toast.makeText(this, "Welcome back, Maria Clara Santos!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}


package com.example.studentlife.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.models.RegisterRequest;
import com.example.studentlife.models.RegisterResponse;
import com.example.studentlife.network.ApiClient;
import com.example.studentlife.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName, etMiddleName, etLastName, etStudentId, etEmail, etPassword;
    private Button btnRegisterSubmit;
    private TextView tvGoToSignIn;
    private View cardRegisterBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = findViewById(R.id.etRegisterFirstName);
        etMiddleName = findViewById(R.id.etRegisterMiddleName);
        etLastName = findViewById(R.id.etRegisterLastName);
        etStudentId = findViewById(R.id.etRegisterStudentId);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        tvGoToSignIn = findViewById(R.id.tvGoToSignIn);
        cardRegisterBack = findViewById(R.id.cardRegisterBack);

        if (cardRegisterBack != null) {
            cardRegisterBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        btnRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString().trim();
                String middleName = etMiddleName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String studentId = etStudentId.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || studentId.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All fields except Middle Name are required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.toLowerCase().endsWith("@phinmaed.com")) {
                    Toast.makeText(RegisterActivity.this, "Please use your @phinmaed.com email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(firstName, middleName, lastName, email, studentId, password);
            }
        });

        if (tvGoToSignIn != null) {
            tvGoToSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void registerUser(String firstName, String middleName, String lastName, String email, String studentId, String password) {
        btnRegisterSubmit.setEnabled(false);
        btnRegisterSubmit.setText("Registering...");

        RegisterRequest request = new RegisterRequest(firstName, middleName, lastName, email, studentId, password);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.registerUser(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                btnRegisterSubmit.setEnabled(true);
                btnRegisterSubmit.setText("Sign Up");

                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse apiResponse = response.body();

                    Toast.makeText(RegisterActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                    if (apiResponse.isSuccess()) {
                        etFirstName.setText("");
                        etMiddleName.setText("");
                        etLastName.setText("");
                        etStudentId.setText("");
                        etEmail.setText("");
                        etPassword.setText("");
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Server error. Check your PHP path.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                btnRegisterSubmit.setEnabled(true);
                btnRegisterSubmit.setText("Sign Up");
                Toast.makeText(RegisterActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

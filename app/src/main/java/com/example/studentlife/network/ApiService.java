package com.example.studentlife.network;

import com.example.studentlife.models.RegisterRequest;
import com.example.studentlife.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register.php")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);
}

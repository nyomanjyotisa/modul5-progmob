package com.example.praktikum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.api.RetroHelper;
import com.example.praktikum.api.UserAPIHelper;
import com.example.praktikum.model.UserHandler;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextUsername, editTextEmail, editTextPassword;
    Button buttonSignup;
    TextView textViewLogin;

    String emailSignup, passwordSignup,usernameSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textViewLogin = (TextView) findViewById(R.id.txtSignIn);
        buttonSignup = (Button) findViewById(R.id.btnSignUp);
        editTextUsername = (EditText) findViewById(R.id.textUsernameSignUp);
        editTextEmail = (EditText) findViewById(R.id.textEmailSignUp);
        editTextPassword = (EditText) findViewById(R.id.txtPasswordSignUp);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailSignup = editTextEmail.getText().toString();
                passwordSignup = editTextPassword.getText().toString();
                usernameSignup = editTextUsername.getText().toString();

                if(TextUtils.isEmpty(emailSignup) && TextUtils.isEmpty(passwordSignup) && TextUtils.isEmpty(usernameSignup)) {
                    editTextEmail.requestFocus();
                    editTextPassword.requestFocus();
                    editTextUsername.requestFocus();
                    editTextUsername.setError("Username harus diisi!");
                    editTextPassword.setError("Password harus diisi!");
                    editTextEmail.setError("Email harus diisi!");

                }else if (TextUtils.isEmpty(emailSignup)){
                    editTextEmail.requestFocus();
                    editTextEmail.setError("Email harus diisi!");

                }else if(TextUtils.isEmpty(passwordSignup)){
                    editTextPassword.requestFocus();
                    editTextPassword.setError("Password harus diisi!");
                }else if(TextUtils.isEmpty(usernameSignup)){
                    editTextUsername.requestFocus();
                    editTextUsername.setError("Username harus diisi!");

                }else{
                    register();
                }
            }
        });
    }

    public void register(){
        UserAPIHelper userRegister = RetroHelper.connectRetrofit().create(UserAPIHelper.class);
        Call<UserHandler> addPengguna = userRegister.penggunaInsertData(usernameSignup, emailSignup, passwordSignup);

        addPengguna.enqueue(new Callback<UserHandler>() {
            @Override
            public void onResponse(Call<UserHandler> call, Response<UserHandler> response) {
                Boolean statusAPI = response.body().getStatusAPI();
                String message = response.body().getMessage();
                if (statusAPI == true) {
                    Toast.makeText(RegisterActivity.this, ""+ message, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserHandler> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Gagal menambah data pengguna : "+ t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

package com.example.praktikum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView textSignup;
    Button buttonLogin;

    EditText email, password;

    String emailLogin;
    String passwordLogin;

    SharedPreferences shp;
    SharedPreferences.Editor shpEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        textSignup = (TextView) findViewById(R.id.txtSignUp);
        buttonLogin = (Button) findViewById(R.id.btnSignIn);
        email = (EditText) findViewById(R.id.editTextEmailSignIn);
        password = (EditText) findViewById(R.id.editTextPasswordSignIn);

        CheckLogin();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailLogin = email.getText().toString();
                passwordLogin = password.getText().toString();

                if(TextUtils.isEmpty(emailLogin)) {
                    email.requestFocus();
                    email.setError("Email harus diisi!");
                }if(TextUtils.isEmpty(passwordLogin)){
                    password.requestFocus();
                    password.setError("Password harus diisi!");
                }else{
                    login();
                }
            }
        });

        textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login(){
        UserAPIHelper userRequestData = RetroHelper.connectRetrofit().create(UserAPIHelper.class);
        Call<UserHandler> getPengguna = userRequestData.checkUsernamePassword(emailLogin, passwordLogin);

        getPengguna.enqueue(new Callback<UserHandler>() {
            @Override
            public void onResponse(Call<UserHandler> call, Response<UserHandler> response) {
                Boolean statusAPI = response.body().getStatusAPI();
                String message = response.body().getMessage();
                List<UserHandler> dataPenggunaList = response.body().getData();

                if(dataPenggunaList.size() > 0) {
                    if (shp == null)
                        shp = getSharedPreferences("loginPre", MODE_PRIVATE);
                    shpEditor = shp.edit();
                    shpEditor.putString("id_user", String.valueOf(dataPenggunaList.get(0).getId()));
                    shpEditor.putString("username", String.valueOf(dataPenggunaList.get(0).getUsername()));
                    shpEditor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("id", String.valueOf(dataPenggunaList.get(0).getId()));
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Username atau Password Salah!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserHandler> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Gagal mengambil username dan password : "+ t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void CheckLogin() {
        if (shp == null)
            shp = getSharedPreferences("loginPre", MODE_PRIVATE);
        String shpIdUser = shp.getString("id_user", "");

        Log.v("what", shpIdUser);

        if (shpIdUser != null && !shpIdUser.equals("")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}

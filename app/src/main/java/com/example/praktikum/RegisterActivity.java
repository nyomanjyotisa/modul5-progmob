package com.example.praktikum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.api.Constant;

import org.json.JSONException;
import org.json.JSONObject;

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
                    register(usernameSignup, emailSignup, passwordSignup);
                }
            }
        });
    }

    private void register(String username,String email, String password){
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        String postUrl = Constant.REGISTER;

        JSONObject postData = new JSONObject();
        try {
            postData.put("name", username);
            postData.put("email", email);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject object = response;
                try {
                    if (object.getBoolean("success")) {
                        Context context = RegisterActivity.this;
                        Toast toast = Toast.makeText(context, "Register berhasil.", Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Context context = RegisterActivity.this;
                    Toast toast = Toast.makeText(context, "Register Gagal.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Context context = RegisterActivity.this;
                Toast toast = Toast.makeText(context, "Register Gagal.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}

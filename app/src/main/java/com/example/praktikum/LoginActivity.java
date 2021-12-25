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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.api.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private void login(){
        String postUrl = Constant.LOGIN;
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", emailLogin);
            postData.put("password", passwordLogin);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {

        @Override
            public void onResponse(JSONObject response) {
                JSONObject object = response;
                try {
                    if(object.getBoolean("success")) {
                        JSONArray user = object.getJSONArray("data");
                        for(int i=0; i < user.length(); i++) {
                            JSONObject jsonobject = user.getJSONObject(i);
                            String id       = jsonobject.getString("id");
                            String name    = jsonobject.getString("name");
                            String email  = jsonobject.getString("email");
                            String email_verified_at = jsonobject.getString("email_verified_at");
                            String created_at = jsonobject.getString("created_at");
                            String updated_at = jsonobject.getString("updated_at");

                            if (shp == null)
                                shp = getSharedPreferences("loginPre", MODE_PRIVATE);
                            shpEditor = shp.edit();
                            shpEditor.putString("id_user", id);
                            shpEditor.putString("username", name);
                            shpEditor.commit();
                        }
                        Context context = LoginActivity.this;
                        int duration = Toast.LENGTH_SHORT;
                        Toast sukses = Toast.makeText(context, "Login Success", duration);
                        sukses.show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Context context = LoginActivity.this;
                    int duration = Toast.LENGTH_SHORT;
                    Toast sukses = Toast.makeText(context, "Login failed", duration);
                    sukses.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Context context = LoginActivity.this;
                Toast toast = Toast.makeText(context, "Login Failed.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        requestQueue.add(jsonObjectRequest);
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

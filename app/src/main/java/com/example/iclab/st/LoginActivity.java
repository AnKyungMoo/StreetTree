package com.example.iclab.st;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.security.KeyChain;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.example.iclab.st.NewplaceActivity.GCSurvey;

// 로그인 액티비티
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText idText = findViewById(R.id.idInput);
        final EditText passwordText = findViewById(R.id.pwInput);

        Button loginBtn = findViewById(R.id.loginBtn);

        //
        loginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();

                final AsyncHttpClient client = new AsyncHttpClient();
                final RequestParams params=new RequestParams();
                params.add("id", userID);
                params.add("pw", userPassword);

                PersistentCookieStore myCookieStore = new PersistentCookieStore(LoginActivity.this);
                client.setCookieStore(myCookieStore);

                client.post("http://220.69.209.49/login",params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        GCSurvey.id = userID;
                        Intent intent = new Intent(getApplicationContext(), FunctionActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}

package com.example.iclab.st;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;

import static com.example.iclab.st.NewplaceActivity.GCSurvey;

// 로그인 액티비티
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText idText = findViewById(R.id.idInput);
        final EditText passwordText = findViewById(R.id.pwInput);
        final Button loginBtn = findViewById(R.id.loginBtn);

        passwordText.setImeOptions(EditorInfo.IME_ACTION_DONE);// 패스워드 엔터키 완료로 수정
        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==EditorInfo.IME_ACTION_DONE){// 패스워드 입력 받고 엔터키 누르면
                    InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(passwordText.getWindowToken(),0);
                    loginBtn.performClick();// 로그인 버튼 클릭 효과
                    return true;
                }
                return false;
            }
        });



        loginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();
                final AsyncHttpClient client = new AsyncHttpClient();
                client.setCookieStore(new PersistentCookieStore(LoginActivity.this));

                RequestParams params = new RequestParams();
                params.put("id", userID);
                params.put("pw", userPassword);
                client.post(LoginActivity.this,"http://220.69.209.49/login", params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Intent intent = new Intent(getApplicationContext(), FunctionActivity.class);
                        startActivity(intent);
                        try {
                            GCSurvey.authorId = response.getString("user_id");
                            GCSurvey.id = response.getString("username");
                            GCSurvey.authorFullName=response.getString("fullName");
                            SaveSharedPreference.setUserName(LoginActivity.this, GCSurvey.id,GCSurvey.authorFullName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            Toast.makeText(getApplicationContext(), errorResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}

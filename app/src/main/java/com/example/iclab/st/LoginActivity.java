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
import static com.example.iclab.st.NewplaceActivity.GCSurvey;

// 로그인 액티비티
public class LoginActivity extends AppCompatActivity {

    static RequestParams loginParams=new RequestParams();
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
                PersistentCookieStore myCookieStore = new PersistentCookieStore(LoginActivity.this);
                client.setCookieStore(myCookieStore);
                loginParams.add("id", userID);
                loginParams.add("pw", userPassword);

                client.post(LoginActivity.this,"http://220.69.209.49/login",  loginParams,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        GCSurvey.id = userID;
                        Intent intent = new Intent(getApplicationContext(), FunctionActivity.class);
                        startActivity(intent);
                        try {
                            GCSurvey.authorFullName=response.getString("fullName");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            loginParams.remove("id");
                            loginParams.remove("pw");
                            Toast.makeText(getApplicationContext(), errorResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    @Override
    protected  void onRestart(){
        super.onRestart();
        loginParams.remove("id");
        loginParams.remove("pw");
    }
}

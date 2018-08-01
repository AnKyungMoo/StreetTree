package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import static com.example.iclab.st.NewplaceActivity.GCSurvey;

// 로그인 액티비티
public class LoginActivity extends AppCompatActivity {

    static PersistentCookieStore myCookieStore;
    final static RequestParams loginParams=new RequestParams();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText idText = findViewById(R.id.idInput);
        final EditText passwordText = findViewById(R.id.pwInput);

        final Button loginBtn = findViewById(R.id.loginBtn);


        //
        loginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();

                final AsyncHttpClient client = new AsyncHttpClient();

                loginParams.add("id", userID);
                loginParams.add("pw", userPassword);
                myCookieStore = new PersistentCookieStore(getApplicationContext());
                client.setCookieStore(myCookieStore);

                client.post("http://220.69.209.49/login",loginParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        GCSurvey.id = userID;
                        Intent intent = new Intent(getApplicationContext(), FunctionActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        Toast.makeText(getApplicationContext(),"로그인 실패"+statusCode,Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}

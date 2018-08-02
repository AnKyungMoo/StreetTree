package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import static com.example.iclab.st.NewplaceActivity.GCSurvey;

// 실측완료를 누르면 최종 결과 값이 출력되는 액티비티
public class CompleteActivity extends AppCompatActivity{

    static String extraData="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        TextView data, extra;
        Button resultBtn = findViewById(R.id.resultBtn);
        data = findViewById(R.id.dataText);
        extra = findViewById(R.id.extradataText);

        data.setMovementMethod(new ScrollingMovementMethod());
        extra.setMovementMethod(new ScrollingMovementMethod());
        data.setText("현장명 :  " + GCSurvey.siteName+"\n발주처 :  " + GCSurvey.clientName +"\n실측일 :  " + GCSurvey.createdAt+ "\n담당자 :  ");
        extra.setText(extraData);

        // 완료 버튼 누르면 기능선택 화면으로 다시 이동
        resultBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                final AsyncHttpClient client = new AsyncHttpClient();
                PersistentCookieStore myCookieStore = new PersistentCookieStore(CompleteActivity.this);
                client.setCookieStore(myCookieStore);

                // 지도에 찍혀있는 마커 리스트 초기화
                MapActivity.markerList.clear();

                StringEntity entity = new StringEntity(new Gson().toJson(GCSurvey), "utf-8");
                client.post(CompleteActivity.this, "http://220.69.209.49/measure/new", entity, "application/json", new AsyncHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // 서버 연결
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // 서버 응답 없음
                    }
                });


                extraData="";
                GCSurvey.list.clear();// 전송 완료후 데이터 초기화
                Intent intent = new Intent(getApplicationContext(), FunctionActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }
}

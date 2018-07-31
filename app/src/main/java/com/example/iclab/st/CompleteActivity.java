package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import static com.example.iclab.st.NewplaceActivity.GCSurvey;

// 실측완료를 누르면 최종 결과 값이 출력되는 액티비티
public class CompleteActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        Button resultBtn = findViewById(R.id.resultBtn);

        // 완료 버튼 누르면 기능선택 화면으로 다시 이동
        resultBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                final AsyncHttpClient client = new AsyncHttpClient();
                PersistentCookieStore myCookieStore = new PersistentCookieStore(CompleteActivity.this);
                client.setCookieStore(myCookieStore);

                StringEntity entity = new StringEntity(new Gson().toJson(GCSurvey), "utf-8");


                client.post(CompleteActivity.this, "http://220.69.209.49", entity, "application/json", new AsyncHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // 서버 연결
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // 서버 응답 없음
                    }
                });
                Intent intent = new Intent(getApplicationContext(), FunctionActivity.class);
                startActivity(intent);


                GCSurvey.list.clear();// 전송 완료후 데이터 초기화

                finish();
            }
        });



    }
}

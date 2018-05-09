package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

// 실측 액티비티(수목 실측): 지도에서 마커를 찍으면 넘어오는 화면
public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // 위도 경도 좌표 값
        Intent preIntent = getIntent();

        double latitude = preIntent.getDoubleExtra("latitude", 0.0f);
        double longitude = preIntent.getDoubleExtra("longitude", 0.0f);

        Log.d("latitude", latitude + "");
        Log.d("longitude", longitude + "");

        Button nextBtn = (Button)findViewById(R.id.nextBtn);
        Button rootBtn = (Button)findViewById(R.id.rootBtn);
        Button completeBtn = (Button)findViewById(R.id.completeBtn);


        // 다음 버튼 누르면 맵 화면으로 전환
        nextBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

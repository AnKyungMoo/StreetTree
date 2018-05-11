package com.example.iclab.st;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

// 실측 액티비티(수목 실측): 지도에서 마커를 찍으면 넘어오는 화면
public class SurveyActivity extends AppCompatActivity {

    FrameLayout frame;
    ImageView point3, point4;
    EditText inputP3_1, inputP3_2, inputP3_3, inputP4_1, inputP4_2, inputP4_3, inputP4_4;
    RadioGroup rg;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // 위도 경도 좌표 값
        Intent preIntent = getIntent();

        final double latitude = preIntent.getDoubleExtra("latitude", 0.0f);
        final double longitude = preIntent.getDoubleExtra("longitude", 0.0f);

        Log.d("latitude", latitude + "");
        Log.d("longitude", longitude + "");

        Button nextBtn = (Button)findViewById(R.id.nextBtn);
        Button startBtn = (Button)findViewById(R.id.SurveyStart);
        Button rootBtn = (Button)findViewById(R.id.rootBtn);
        Button completeBtn = (Button)findViewById(R.id.completeBtn);
        rg = (RadioGroup)findViewById(R.id.radioGroup);
        frame = (FrameLayout)findViewById(R.id.frame);
        point3 = (ImageView)findViewById(R.id.point3);
        point4 = (ImageView)findViewById(R.id.point4);
        inputP3_1 = (EditText)findViewById(R.id.inputP3_1);
        inputP3_2 = (EditText)findViewById(R.id.inputP3_2);
        inputP3_3 = (EditText)findViewById(R.id.inputP3_3);
        inputP4_1 = (EditText)findViewById(R.id.inputP4_1);
        inputP4_2 = (EditText)findViewById(R.id.inputP4_2);
        inputP4_3 = (EditText)findViewById(R.id.inputP4_3);
        inputP4_4 = (EditText)findViewById(R.id.inputP4_4);

        changeView(index);

        // 라디오버튼 제어(설치전, 설치후)
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public  void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.beforeRadio) {
                    index = 1;
                }
                else if(i == R.id.afterRadio) {
                    index = 2;
                }
            }
        });

        // 실측시작 버튼 누르면 실측값 입력 화면 출력
        startBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                changeView(index);
            }
        });

        // 다음 버튼 누르면 맵 화면으로 전환
        nextBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);

                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);

                startActivity(intent);
                finish();
            }
        });

        // 실측완료 버튼 누르면 결과출력 화면으로 전환
        completeBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompleteActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // 설치 전(3군데) - 설치 후(4군데)에 대한 view 전환
    public void changeView(int index) {
        switch (index) {
            case 0 :
                frame.removeView(inputP3_1);
                frame.removeView(inputP3_2);
                frame.removeView(inputP3_3);
                frame.removeView(inputP4_1);
                frame.removeView(inputP4_2);
                frame.removeView(inputP4_3);
                frame.removeView(inputP4_4);
                frame.removeView(point3);
                frame.removeView(point4);
                break;
            case 1 :
                frame.addView(point3);
                frame.addView(inputP3_1);
                frame.addView(inputP3_2);
                frame.addView(inputP3_3);
                frame.removeView(point4);
                frame.removeView(inputP4_1);
                frame.removeView(inputP4_2);
                frame.removeView(inputP4_3);
                frame.removeView(inputP4_4);
                break;
            case 2 :
                frame.addView(point4);
                frame.addView(inputP4_1);
                frame.addView(inputP4_2);
                frame.addView(inputP4_3);
                frame.addView(inputP4_4);
                frame.removeView(point3);
                frame.removeView(inputP3_1);
                frame.removeView(inputP3_2);
                frame.removeView(inputP3_3);
                break;
        }
    }
}

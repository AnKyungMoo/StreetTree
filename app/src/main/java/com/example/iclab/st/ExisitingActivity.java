package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// 기존현장 확인 액티비티
public class ExisitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exisiting);

        Button btn1 = (Button)findViewById(R.id.btn1);
        Button btn2 = (Button)findViewById(R.id.btn2);

        // 지역으로 검색 버튼 누르면 Regionsrch 액티비티로 전환
        btn1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegionsrchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 현장명으로 검색 버튼 누르면 Namesrch 액티비티로 전환
        btn2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NamesrchActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
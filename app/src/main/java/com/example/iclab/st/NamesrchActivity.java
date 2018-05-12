package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// 현장명으로 찾기 액티비티
public class NamesrchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namesrch);

        Button backBtn6 = (Button)findViewById(R.id.backBtn6);

        // 지역으로 검색 버튼 누르면 Regionsrch 액티비티로 전환
        backBtn6.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExisitingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

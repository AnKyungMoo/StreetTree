package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// 보호판 선택 화면
public class ProtectpanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protectpanel);

        Button backBtn3 = (Button)findViewById(R.id.backBtn3);

        // 뒤로 버튼 누르면 실측화면으로 다시 이동
        backBtn3.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

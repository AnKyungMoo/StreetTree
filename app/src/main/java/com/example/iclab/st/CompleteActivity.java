package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// 실측완료를 누르면 최종 결과 값이 출력되는 액티비티
public class CompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        Button resultBtn = (Button)findViewById(R.id.resultBtn);

        // 완료 버튼 누르면 기능선택 화면으로 다시 이동
        resultBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FunctionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

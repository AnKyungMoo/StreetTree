package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// 기능선택 액티비티
public class FunctionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        Button bt1Btn = (Button)findViewById(R.id.button1);

        // 신규현장실측 버튼 누르면 Newplace 액티비티로 전환
        bt1Btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewplaceActivity.class);
                startActivity(intent);
            }
        });
    }
}


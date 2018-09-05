package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.iclab.st.NewplaceActivity.GCSurvey;

// 기능선택 액티비티
public class FunctionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        Button bt1 = findViewById(R.id.button1);
        Button bt2 = findViewById(R.id.button2);
        Button bt3 = findViewById(R.id.button3);
        Button btOut = findViewById(R.id.logout);

        // 신규현장실측 버튼 누르면 Newplace 액티비티로 전환
        bt1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewplaceActivity.class);
                startActivity(intent);
            }
        });

        // 기존현장확인 버튼 누르면 Exisiting 액티비티로 전환
        bt2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExisitingActivity.class);
                startActivity(intent);
            }
        });

        // 제품사양 버튼 누르면 product 액티비티로 전환
        bt3.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                startActivity(intent);
            }
        });

        btOut.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SaveSharedPreference.clearUserData(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}


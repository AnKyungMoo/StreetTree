package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// 신규현장실측 - 현장명입력 - [주소 입력]
public class RegionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        Button backBtn = (Button)findViewById(R.id.backBtn);

        // 뒤로 버튼 누르면 현장명입력 화면으로 다시 이동
        backBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewplaceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// 신규현장실측(현장명입력) 액티비티
public class NewplaceActivity extends AppCompatActivity {

    EditText inputHyunjang;
    EditText inputBalju;
    TextView contentTxV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newplace);

        Button saveBtn = (Button)findViewById(R.id.save);
        Button okBtn = (Button)findViewById(R.id.ok);
        inputHyunjang = (EditText)findViewById(R.id.inputHyunjang);
        inputBalju = (EditText)findViewById(R.id.inputBalju);
        contentTxV = (TextView)findViewById(R.id.contentView);

        // 레이아웃 배치를 위해 임시로 텍스트 띄우는 기능만 (날짜랑 담당자 받아오는 기능 구현X)
        okBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                contentTxV.setText("  현장명 :  " + inputHyunjang.getText() +"\n\n" + "  발주처 :  " + inputBalju.getText()
                        +"\n\n" + "  날짜 :  " + "\n\n" + "  담당자 :  ");
            }
        });


        // 저장 버튼 누르면 지도 화면으로 전환
        saveBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

// 신규현장실측(현장명입력) 액티비티
public class NewplaceActivity extends AppCompatActivity {

    EditText inputHyunjang;
    EditText inputBalju;
    TextView contentTxV;
    long now = System.currentTimeMillis();
    Date date;
    String nowDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newplace);

        Button saveBtn = (Button)findViewById(R.id.save);
        Button okBtn = (Button)findViewById(R.id.ok);
        inputHyunjang = (EditText)findViewById(R.id.inputHyunjang);
        inputBalju = (EditText)findViewById(R.id.inputBalju);
        contentTxV = (TextView)findViewById(R.id.contentView);
        date = new Date(now);

        // 현재 날짜를 해당 포맷으로 받아옴
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
        nowDate = sdfNow.format(date);

        //현장명 입력 EditText 클릭시 주소 입력 화면 이동
        inputHyunjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegionActivity.class);
                startActivity(intent);

                finish();
            }
        });



        // 레이아웃 배치를 위해 임시로 텍스트 띄우는 기능만 (담당자X - 아직 로그인 구현X)
        okBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                contentTxV.setText("  현장명 :  " + inputHyunjang.getText() +"\n\n" + "  발주처 :  " + inputBalju.getText()
                        +"\n\n" + "  날짜 :  " + nowDate.toString()+ "\n\n" + "  담당자 :  ");
            }
        });


        // 저장 버튼 누르면 지도 화면으로 전환
        saveBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                CSurvey.field_name = inputHyunjang.getText().toString(); // 데이터저장
                CSurvey.date = nowDate.toString();
                CSurvey.client = inputBalju.getText().toString();

                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });
    }
}

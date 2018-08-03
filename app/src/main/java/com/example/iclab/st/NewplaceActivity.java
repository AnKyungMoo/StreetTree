package com.example.iclab.st;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// 신규현장실측(현장명입력) 액티비티

public class NewplaceActivity extends AppCompatActivity {

    static public CSurvey GCSurvey=new CSurvey();
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

        Button saveBtn = findViewById(R.id.save);
        final Button okBtn = findViewById(R.id.ok);
        inputHyunjang = findViewById(R.id.inputHyunjang);
        inputBalju = findViewById(R.id.inputBalju);
        contentTxV = findViewById(R.id.contentView);
        date = new Date(now);
        inputHyunjang.setPrivateImeOptions("defaultInputmode=korean; ");
        inputBalju.setPrivateImeOptions("defaultInputmode=korean; ");

        inputBalju.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputBalju.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(inputBalju.getWindowToken(),0);
                    okBtn.performClick();
                    return true;
                }
                return false;
            }
        });
        // 현재 날짜를 해당 포맷으로 받아옴
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
        nowDate = sdfNow.format(date);


        // 레이아웃 배치를 위해 임시로 텍스트 띄우는 기능만 (담당자X// - 아직 로그인 구현X) // 0730 로그인 구현
        okBtn.setOnClickListener(new Button.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                contentTxV.setText("  현장명 :  " + inputHyunjang.getText() +"\n\n" + "  발주처 :  " + inputBalju.getText()
                        +"\n\n" + "  날짜 :  " + nowDate+ "\n\n" + "  담당자 :  "+GCSurvey.authorFullName);
            }
        });
        // 저장 버튼 누르면 지도 화면으로 전환
        saveBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                GCSurvey.siteName = inputHyunjang.getText().toString(); // 데이터저장
                GCSurvey.createdAt = nowDate;
                GCSurvey.clientName = inputBalju.getText().toString();

              Intent intent = new Intent(getApplicationContext(), MapActivity.class); // 원래 , 맵으로이동이지만 잠시 테스트 하기 위해
//                Intent intent = new Intent(getApplicationContext(), SurveyActivity.class); // 잠시 테스트하기 위해 변경
                startActivity(intent);
            }
        });
    }
}


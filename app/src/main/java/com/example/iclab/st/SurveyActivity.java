package com.example.iclab.st;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.iclab.st.RootActivity.imageId;

// 실측 액티비티(수목 실측): 지도에서 마커를 찍으면 넘어오는 화면
public class SurveyActivity extends AppCompatActivity {

    FrameLayout frame;
    ImageView  point4;
    EditText inputTN;
    RadioGroup rg;
    TextView noTree;
    int index = 0;
    CheckBox ckBox;
    EditText inputP[];// 기존의 input_P
    String points[] = new String[4]; // 뿌리 값
    String sido;
    String goon;
    String gu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // 위도 경도 좌표 값
        Intent preIntent = getIntent();
        final double latitude = preIntent.getDoubleExtra("latitude", 0.0f);
        final double longitude = preIntent.getDoubleExtra("longitude", 0.0f);
        Button nextBtn = findViewById(R.id.nextBtn);
        Button startBtn = findViewById(R.id.SurveyStart);
        Button rootBtn = findViewById(R.id.rootBtn);
        Button completeBtn =findViewById(R.id.completeBtn);
        Button modifyBtn = findViewById(R.id.modifyBtn);

        noTree=findViewById(R.id.number);

        noTree.setText("No. "+SurveyList.count);
        inputTN = findViewById(R.id.inputTN);
        rg = findViewById(R.id.radioGroup);
        frame =findViewById(R.id.frame);
        point4 = findViewById(R.id.point4);
        ckBox = findViewById(R.id.checkBox);
        inputP=new EditText[4];
        for(int k=0;k<4;k++){
            inputP[k]=new EditText(SurveyActivity.this);
            int pointId=R.id.inputP4_1+k;
            inputP[k]=findViewById(pointId);
        }

        changeView(index); // 실측화면 초기화
        imageId=null;
        // 체크박스 제어(수목번호 유무)
        ckBox.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(ckBox.isChecked()) { //없음이 체크되면
                    inputTN.setText(null); //기존에 입력된 내용 지우기
                    inputTN.setFocusableInTouchMode(false);
                    inputTN.setFocusable(false); // 입력창 비활성화
                }
                else { // 없음 체크 해제
                    inputTN.setFocusableInTouchMode(true);
                    inputTN.setFocusable(true); // 입력창 다시 활성화
                }
            }
        });

        // 라디오버튼 제어(설치전, 설치후)
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public  void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.beforeRadio) {
                    index = 1;
                }
                else if(i == R.id.afterRadio) {
                    index = 2;
                }
            }
        });

        // 실측시작 버튼 누르면 실측값 입력 화면 출력
        startBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                changeView(index);
            }
        });

        // 수정 버튼 누르면 보호판 선택 화면으로 전환
        modifyBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProtectpanelActivity.class);

                startActivity(intent);
            }
        });

        // 다음 버튼 누르면 맵 화면으로 전환
        nextBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);//  테스트로 인해 잠시 변경
               // Intent intent = new Intent(getApplicationContext(), FunctionActivity.class);// 테스트로 인해 잠시 변경
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                make_list(latitude, longitude); // 저장
                startActivity(intent);
                finish();
            }
        });

        // 수목뿌리 버튼 누르면 액티비티 전환
        rootBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RootActivity.class);

                startActivity(intent);
            }
        });

        // 실측완료 버튼 누르면 결과출력 화면으로 전환
        completeBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompleteActivity.class);
                make_list(latitude, longitude); // 저장

                startActivity(intent);
                finish();
            }
        });

    }

    // 설치 전(3군데) - 설치 후(4군데)에 대한 view 전환
    public void changeView(int index) {
        frame.removeView(point4);
        for(int k=0;k<4;k++)
            frame.removeView(inputP[k]);
        if(index!=0){
            frame.addView(point4);
            for(int k=0;k<2+index;k++)
                frame.addView(inputP[k]);
        }

    }
    void make_list(double la, double lo)
    {
        for(int k=0;index==2?k<4:k<3;k++)
            points[k]=inputP[k].getText().toString();
        Geocoder gCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addr = null;

        try {
            addr = gCoder.getFromLocation(la, lo, 1);
            Address a = addr.get(0);
            String s[];
            for (int i = 0; i <= a.getMaxAddressLineIndex(); i++) {
                FindCode fCode= new FindCode();
                //if(a.getFeatureName())
                String loc=a.getLocality();
                if(a.getSubLocality()!=null)
                    loc+=a.getSubLocality();
                goon=fCode.kmaJson(loc);// 군
                sido=goon.substring(0,2);// 시
                gu=fCode.finder(a.getThoroughfare(),goon);// 구
//                Log.d("실험","   "+gu+ "   "+ fCode.kmaJson(sido));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String tnStr=inputTN.getText().toString();
        CSurvey.add_list("PLATE",ckBox.isChecked()?null:tnStr,index ==2,points, la,lo,imageId,sido,goon,gu);
    }


}

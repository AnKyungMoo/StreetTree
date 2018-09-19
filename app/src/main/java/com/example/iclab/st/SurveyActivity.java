package com.example.iclab.st;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;

import static com.example.iclab.st.NewplaceActivity.GCSurvey;
import static com.example.iclab.st.RootActivity.imageId;

// 실측 액티비티(수목 실측): 지도에서 마커를 찍으면 넘어오는 화면
public class SurveyActivity extends AppCompatActivity {
    Spinner sp1;
    Spinner sp2;
    Spinner sp3;
    FrameLayout frame;
    ImageView  point4;
    EditText inputTN;

    RadioGroup rg,rg1,rg2;

    TextView noTree;
    int index = 0;
    CheckBox ckBox;
    EditText inputP[];// 기존의 input_P
    String points[] = new String[4]; // 뿌리 값
    String sido;
    String goon;
    String gu;


    EditText et;
    String etStr="";
    ArrayList<String> list1=new ArrayList<>();
    ArrayList<String> list2=new ArrayList<>();
    ArrayList<String> list3=new ArrayList<>();

    String str1,str2;

    static ArrayList<String> plateId=new ArrayList<>();
    static ArrayList<String> frameId=new ArrayList<>();
    LinearLayout a;
    TextView plateView;
    Boolean frameCh;
    Boolean gagakCh=true;
    Boolean jijuguCh=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        plateView=findViewById(R.id.selectPP);

        sp1=new Spinner(this);
        sp2=new Spinner(this);
        sp3=new Spinner(this);

        a=new LinearLayout(this);
        // 위도 경도 좌표 값
        Intent preIntent = getIntent();
        final double latitude = preIntent.getDoubleExtra("latitude", 0.0f);
        final double longitude = preIntent.getDoubleExtra("longitude", 0.0f);
        Button nextBtn = findViewById(R.id.nextBtn);
        final Button startBtn = findViewById(R.id.SurveyStart);
        Button rootBtn = findViewById(R.id.rootBtn);
        Button completeBtn =findViewById(R.id.completeBtn);
        Button modifyBtn = findViewById(R.id.modifyBtn);
        final Button memobutton=findViewById(R.id.memobutton);

        noTree=findViewById(R.id.number);

        noTree.setText("No. "+(GCSurvey.list.size()+1));
        inputTN = findViewById(R.id.inputTN);
        rg = findViewById(R.id.radioGroup);
        rg1 = findViewById(R.id.radioGroup1);
        rg2 = findViewById(R.id.radioGroup2);

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

        // 가각 제어(설치전, 설치후)
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public  void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.beforeRadio1) {
                    gagakCh=true;

                }
                else if(i == R.id.afterRadio1) {
                    gagakCh=false;

                }
            }
        });
        // 지주구 제어(설치전, 설치후)
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public  void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.beforeRadio2) {
                    jijuguCh=false;
                }
                else if(i == R.id.afterRadio2) {
                    jijuguCh=false;
                }
            }
        });

        // 실측시작 버튼 누르면 실측값 입력 화면 출력
        startBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                changeView(index);
            }
        });
        //

        final AsyncHttpClient client=new AsyncHttpClient();
        client.setCookieStore(new PersistentCookieStore(SurveyActivity.this));
        client.get(SurveyActivity.this,"http://220.69.209.49/plates",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for(int i=0;i<response.length();i++) {
                    try {
                        JSONObject object =response.getJSONObject(i);
                        plateId.add(object.getString("plate_id"));
                        object=object.getJSONObject("frame");
                        frameId.add(object.getString("frame_id"));

                        int ind=plateId.get(i).indexOf("-");
                        if(!list1.contains(plateId.get(i).substring(0,ind)))
                            list1.add(plateId.get(i).substring(0,ind));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers,String s, Throwable throwable) {
                super.onFailure(statusCode, headers,s, throwable);
                Log.e("Test","실패 "+statusCode);

            }
        });

        RadioGroup frameRg=new RadioGroup(this);

        final RadioButton frameRg1=new RadioButton(this);
        final RadioButton frameRg2=new RadioButton(this);

        frameRg1.setText("있음");
        frameRg2.setText("없음");

        frameRg.addView(frameRg1);
        frameRg.addView(frameRg2);



        frameRg1.setChecked(true);
        frameCh=true;
        frameRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public  void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == frameRg1.getId()) {// 1
                    frameCh=true;


                }
                else if(i == frameRg2.getId()) {// 2
                    frameCh=false;
                }

            }
        });

        a.addView(sp1);
        a.addView(sp2);
        a.addView(sp3);
        a.addView(frameRg);

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(SurveyActivity.this);
        alt_bld.setCancelable(
                false).setPositiveButton("완료",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        alt_bld.setView(a);
        final AlertDialog alert = alt_bld.create();
        alert.setTitle("보호판 선택");

        // 수정 버튼 누르면 보호판 선택 화면으로 전환
        modifyBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Collections.sort(list1);
                ArrayAdapter<String> listAdap1 = new ArrayAdapter<String>(SurveyActivity.this, R.layout.support_simple_spinner_dropdown_item, list1);
                sp1.setAdapter(listAdap1);

                alert.show();


            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                str1=list1.get(index);

                list2.clear();
                for(int i=0;i<plateId.size();i++){
                    int j=plateId.get(i).indexOf("-");
                    if(str1.equals(plateId.get(i).substring(0,j))){
                        int k=plateId.get(i).substring(j+1).indexOf("-");
                        if(!list2.contains(plateId.get(i).substring(j+1,k+j+1))) {
                            list2.add(plateId.get(i).substring(j + 1, k+j+1));
                        }
                    }

                }


                Collections.sort(list2);
                ArrayAdapter<String> listAdap2 = new ArrayAdapter<String>(SurveyActivity.this, R.layout.support_simple_spinner_dropdown_item, list2);
                sp2.setAdapter(listAdap2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                str2=str1+"-"+list2.get(index);
                list3.clear();



                for(int i=0;i<plateId.size();i++){
                    if(plateId.get(i).contains(str2)){
                        int j = plateId.get(i).lastIndexOf("-");
                        if(!list3.contains(plateId.get(i).substring(j+1))){
                            list3.add(plateId.get(i).substring(j+1));
                        }
                    }
                }
                Collections.sort(list3);
                list3.add(0,"없음");
                ArrayAdapter<String> listAdap3 = new ArrayAdapter<String>(SurveyActivity.this, R.layout.support_simple_spinner_dropdown_item, list3);
                sp3.setAdapter(listAdap3);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // plateView 에 출력
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int ind, long l) {



                if(ind==0){
                    plateView.setText(str2);
                    startBtn.setVisibility(View.VISIBLE);
//                    GCSurvey.list.get(GCSurvey.list.size()-1).plate_id=str2+"-"+list3.get(ind);
                }
                else{
                    plateView.setText(str2+"-"+list3.get(ind));
                    startBtn.setVisibility(View.INVISIBLE);
//                    GCSurvey.list.get(GCSurvey.list.size()-1).plate_id=str2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // 다음 버튼 누르면 맵 화면으로 전환
        nextBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);//  테스트로 인해 잠시 변경
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                make_list(latitude, longitude); // 저장
                Gson newGson = new Gson();
                String json = newGson.toJson(GCSurvey);
                SaveSharedPreference.setUserData(SurveyActivity.this, json);
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


        // 메모 버튼

        memobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(SurveyActivity.this);

                et=new EditText(getApplicationContext());
                et.setHint("여기에 메모를 입력하세요.");

                alt_bld.setView(et);
                alt_bld.setCancelable(
                        false).setPositiveButton("완료",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                etStr=et.getText().toString();
//                                Log.e("타이핑",GCSurvey.list.get(GCSurvey.list.size()-1).memo);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.setTitle("메모 기능 (100자 제한)");
                alert.show();



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
                if(a.getSubLocality()!=null&&!loc.contains("서울"))
                    loc+=a.getSubLocality();
                else if(loc.contains("서울"))
                    loc=a.getSubLocality();
                goon=fCode.kmaJson(loc);// 군
                sido=goon.substring(0,2);// 시
                gu=fCode.finder(a.getThoroughfare(),goon);// 구
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String tnStr=inputTN.getText().toString();
        CSurvey.add_list(plateView.getText().toString(),ckBox.isChecked()?null:tnStr,index ==2,points, la,lo,imageId,sido,goon,gu,etStr
               ,frameCh,gagakCh,jijuguCh);

    }
}

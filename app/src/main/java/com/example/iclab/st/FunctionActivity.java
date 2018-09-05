package com.example.iclab.st;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.iclab.st.NewplaceActivity.GCSurvey;

// 기능선택 액티비티
public class FunctionActivity extends AppCompatActivity {
    String str = null;
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
                    str = SaveSharedPreference.getUserData(getApplicationContext());
                    if(str.length() != 0) {
                        AlertDialog.Builder alt_bld = new AlertDialog.Builder(FunctionActivity.this);
                        JSONObject object = null;
                        String sitestr=null;
                        try {
                            object = new JSONObject(str);
                            sitestr = object.getString("siteName");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        alt_bld.setMessage("이전에 작업한 기록이 있습니다.\n 불러오시겠습니까?"+"\n(현장명 : "+sitestr+")").setCancelable(
                                false).setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            JSONObject object = new JSONObject(str);
                                            GCSurvey.list.clear();
                                            JSONparsing(object);
                                            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        GCSurvey.list.clear();
                                        SurveyList.count = 1;
                                        Intent intent = new Intent(getApplicationContext(), NewplaceActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        AlertDialog alert = alt_bld.create();
                        alert.setTitle("불러오기");
                        alert.show();

                    } else {
                        Intent intent = new Intent(getApplicationContext(), NewplaceActivity.class);
                        startActivity(intent);
                    }


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
    public void JSONparsing(JSONObject jsonObject) // 로컬에 저장된 JSON 파싱
    {
        try {
            Log.d("테스트",jsonObject.toString());
            GCSurvey.list.clear();
            GCSurvey.clientName = jsonObject.getString("clientName");
            GCSurvey.createdAt = jsonObject.getString("createdAt");
            GCSurvey.siteName = jsonObject.getString("siteName");
            GCSurvey.salespersonName = jsonObject.getString("salespersonName");
            GCSurvey.deliveryTarget = jsonObject.getString("deliveryTarget");
            GCSurvey.deliveryDate = jsonObject.getString("deliveryDate");
            GCSurvey.differenceValue = jsonObject.getString("differenceValue");
            JSONArray jsonArray = jsonObject.getJSONArray("list");

            for(int i=0;i<jsonArray.length();i++)
            {
                String points[] = new String[4];
                points[3] = "";
                JSONObject object = jsonArray.getJSONObject(i);
                SurveyList list = new SurveyList();
                list.sequenceNumber = i+1;
                list.latitude = object.getDouble("latitude");
                list.longitude = object.getDouble("longitude");
                list.sido = object.getString("sido");
                list.goon = object.getString("goon");
                list.gu = object.getString("gu");

                list.plateName = object.getString("plateName");
                list.treeNumber = object.getString("treeNumber");
                list.isInstalled = object.getBoolean("isInstalled");
                list.treeLocation= object.getString("treeLocation");
                list.memo = object.getString("memo");

                for(int k=0;k< object.getJSONArray("points").length();k++)
                    points[k] = object.getJSONArray("points").getString(k);
                GCSurvey.list.add(list);
            }
            SurveyList.count = jsonArray.length()+1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}


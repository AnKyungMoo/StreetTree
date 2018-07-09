package com.example.iclab.st;

import android.content.Intent;
import android.graphics.Region;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;


// 신규현장실측 - 현장명입력 - [주소 입력]
public class RegionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        LinkedHashMap<String, Integer> regionMap;

        Button backBtn = (Button) findViewById(R.id.backBtn);
        Spinner top = (Spinner) findViewById(R.id.top);

        // 뒤로 버튼 누르면 현장명입력 화면으로 다시 이동
        backBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewplaceActivity.class);
                startActivity(intent);
                finish();
            }
        });

        try {
            URL url = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt");
            ArrayList<String> sidoList = new ArrayList<String>();

            URLAsyncTask urlTask = new URLAsyncTask();

            urlTask.execute(url);

            // URLAsyncTask 실행 결과에서 키 값 가져오기
            regionMap = urlTask.get();

            Set keySet = regionMap.keySet();

            Iterator keyIterator = keySet.iterator();

            while(keyIterator.hasNext())
            {
                sidoList.add(keyIterator.next()+"");
            }

            // sido Spinner에 데이터 저장
            ArrayAdapter<String> sidoAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sidoList);

            top.setAdapter(sidoAdapter);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}

package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;


// 신규현장실측 - 현장명입력 - [주소 입력]
public class RegionActivity extends AppCompatActivity {

    LinkedHashMap<String, Integer> sidoMap;
    LinkedHashMap<String, Integer> goonMap;
    LinkedHashMap<String, Integer> guMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        Button backBtn =  findViewById(R.id.backBtn);
        final Spinner top = findViewById(R.id.top);
        final Spinner mid = findViewById(R.id.mid);
        final Spinner leaf = findViewById(R.id.leaf);

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

            URLAsyncTask sidoTask = new URLAsyncTask();

            sidoTask.execute(url);

            // sidoTask에서 처리된 값을 가져와서 key값 나열
            sidoMap = sidoTask.get();

            Set keySet = sidoMap.keySet();

            Iterator keyIterator = keySet.iterator();

            while(keyIterator.hasNext()) {
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

        // 시도 부분 spinner 클릭했을 때
        top.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {

                URLAsyncTask goonTask = new URLAsyncTask();
                URL goonURL = null;
                try {
                    goonURL = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl." + sidoMap.get(top.getItemAtPosition(index) + "") + ".json.txt");
                    ArrayList<String> goonList = new ArrayList<String>();

                    goonTask.execute(goonURL);

                    // goonTask에서 처리된 값을 가져와서 key값 나열
                    goonMap = goonTask.get();

                    Set goonKeySet = goonMap.keySet();

                    Iterator goonKeyIterator = goonKeySet.iterator();

                    while(goonKeyIterator.hasNext()){
                        goonList.add(goonKeyIterator.next()+"");
                    }

                    // spinner에 값 저장
                    ArrayAdapter<String> goonAdapter = new ArrayAdapter<String>(RegionActivity.this, R.layout.support_simple_spinner_dropdown_item, goonList);

                    mid.setAdapter(goonAdapter);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 군 부분 Spinner를 클릭했을 때
        mid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                URLAsyncTask guTask = new URLAsyncTask();
                URL guURL = null;
                try {
                    guURL = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf." + goonMap.get(mid.getItemAtPosition(index) + "") + ".json.txt");
                    ArrayList<String> guList = new ArrayList<String>();

                    guTask.execute(guURL);

                    // guTask에서 처리된 값을 가져와서 key값 나열
                    guMap = guTask.get();

                    Set guKeySet = guMap.keySet();

                    Iterator guKeyIterator = guKeySet.iterator();

                    while(guKeyIterator.hasNext())
                    {
                        guList.add(guKeyIterator.next()+"");
                    }

                    // spinner에 값 저장
                    ArrayAdapter<String> guAdapter = new ArrayAdapter<String>(RegionActivity.this, R.layout.support_simple_spinner_dropdown_item, guList);

                    leaf.setAdapter(guAdapter);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}

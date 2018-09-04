package com.example.iclab.st;

// 실측값 리스트 출력 액티비티 (지역으로 검색, 현장명으로 검색하고 이동)
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.example.iclab.st.NamesrchActivity.newCS;

public class ValueprintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valueprint);
        Intent intent = getIntent();
        final ListView vList = findViewById(R.id.valueList);
        List<String> listinfo=new ArrayList<>();
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, listinfo);

        int pos = -1;
        if(intent.getExtras() != null)
        {
             pos = intent.getIntExtra("position",-1);
        }
        for(int i=0; i< newCS.get(pos).list.size();i++)
        {
            SurveyList sl = newCS.get(pos).list.get(i);
            String s = "NO."+(i+1)+ "   보호판 : "+sl.plateName +"\n위도 : "+sl.latitude+"  \n경도 : "+sl.longitude;
            listinfo.add(s);
        }
        vList.setAdapter(listAdapter);
    }
}

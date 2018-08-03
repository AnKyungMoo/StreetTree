package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

// 현장명으로 찾기 액티비티
public class NamesrchActivity extends AppCompatActivity {

    String[] hyunjangList = {"list1", "list2", "list3", "list4", "list5", "list6", "list7", "list8", "list9", "list10", "list11", "list12", "list13", "list14", "list15"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namesrch);
        final ListView hhList = findViewById(R.id.hyunjangList2);

        // 현장명 리스트뷰 어댑터 생성
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, hyunjangList);
        hhList.setAdapter(listAdapter);

        // 리스트뷰 아이템 클릭 시 액티비티 이동
        hhList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NamesrchActivity.this, ValueprintActivity.class);
                startActivity(intent);
            }
        });

    }
}

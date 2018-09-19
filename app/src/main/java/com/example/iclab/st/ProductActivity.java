package com.example.iclab.st;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;



// 제품 사양 액티비티
public class ProductActivity extends AppCompatActivity {
    int ind;
    int page=0;
    ArrayAdapter<String> listAdapter2=null;
    ArrayAdapter<String> listAdapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        final ListView listview = findViewById(R.id.productlist);
        Button backBtn4 = findViewById(R.id.backBtn4);
        final Button backBtn5 = findViewById(R.id.backBtn5);
        final List<String> listName0=new ArrayList<>();// 리스트 전체
        final List<String> listName1=new ArrayList<>();// ㅁㅁㅁ-ㅁㅁㅁ
        final List<String> listName2=new ArrayList<>();// ㅁㅁㅁ-ㅁㅁㅁ-ㅁ
        final List<String> listName3=new ArrayList<>();// ㅁㅁㅁ-ㅁㅁㅁ-ㅁㅁㅁ-Model.pdf


        // 뒤로 버튼 누르면 기능선택으로 다시 이동
        backBtn4.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), FunctionActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // 리스트 뒤로가기 느낌 버튼 추가. 리스트 3개인 이유


        // listname에 서버에서 받아온 값들 저장.

        final AsyncHttpClient client=new AsyncHttpClient();
        client.setCookieStore(new PersistentCookieStore(ProductActivity.this));
        client.get(ProductActivity.this,"http://220.69.209.49/plates",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for(int i=0;i<response.length();i++) {
                    try {
                        JSONObject object =response.getJSONObject(i);
                        String plateId=object.getString("plate_id");
                        listName0.add(plateId);
                        ind=plateId.lastIndexOf("-");

                        if(!listName1.contains(plateId.substring(0,ind)))
                            listName1.add(plateId.substring(0, ind));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // 현장명 리스트뷰 어댑터 생성
                listAdapter = new ArrayAdapter<String> (ProductActivity.this, android.R.layout.simple_list_item_1, listName1);
                listview.setAdapter(listAdapter);


            }
            @Override
            public void onFailure(int statusCode, Header[] headers,String s, Throwable throwable) {
                super.onFailure(statusCode, headers,s, throwable);
                Log.e("Test","실패 "+statusCode);

            }
        });


        backBtn5.setVisibility(View.INVISIBLE);


        // 리스트뷰 아이템 클릭 시
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                backBtn5.setVisibility(View.VISIBLE);
                if(page==0) {
                    listName2.clear();
                    // 리스트 클릭시 세부이름?
                    for (int i = 0; i < listName0.size(); i++)
                        if (listName0.get(i).contains(listName1.get(position)) && !listName2.contains(listName0.get(i).substring(0, ind + 2)))
                            listName2.add(listName0.get(i).substring(0, ind + 2));
                    listAdapter2 = new ArrayAdapter<String>(ProductActivity.this, android.R.layout.simple_list_item_1, listName2);
                    listview.setAdapter(listAdapter2);
                    page=1;
                }
                else if(page==1){
                    /// 사진 띄우기
//                    final AsyncHttpClient client = new AsyncHttpClient();
//                    client.setCookieStore(new PersistentCookieStore(ProductActivity.this));
//                    client.get("http://220.69.209.49/", new JsonHttpResponseHandler(){
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONArray response)  {
//                            super.onSuccess(statusCode, headers, response);
//                            Toast.makeText(getApplicationContext(), "검색 결과", Toast.LENGTH_SHORT).show();
//
//                        }
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                            super.onFailure(statusCode, headers, throwable, errorResponse);
//                            Toast.makeText(getApplicationContext(), "서버 응답 없음\nstatus: "+statusCode, Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    page=2;
                }



            }
        });


         backBtn5.setOnClickListener(new Button.OnClickListener() {
             public void onClick(View v) {
                 if(page!=0) {

                     page--;

                     if(page==0){
                         listview.setAdapter(listAdapter);
                     }else if(page==1){
                         listview.setAdapter(listAdapter2);
                     }else if(page==2){
                         listview.setAdapter(listAdapter2);// 수정 필요
                     }
                     listview.invalidate();
                 }
                 if(page==0)
                     backBtn5.setVisibility(View.INVISIBLE);


             }
         });
    }
}

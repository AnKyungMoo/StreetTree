package com.example.iclab.st;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

// 현장명으로 찾기 액티비티
public class NamesrchActivity extends AppCompatActivity {
    static ArrayList<CSurvey> newCS = new ArrayList<>();
    List<String> listName=new ArrayList<>();
    int num;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namesrch);
        final ListView hhList = findViewById(R.id.hyunjangList2);
        final EditText editText=findViewById(R.id.hyunjangSrch);
        final Button button=findViewById(R.id.srchBtn2);


        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);// 패스워드 엔터키 완료로 수정
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==EditorInfo.IME_ACTION_DONE){
                    InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(editText.getWindowToken(),0);
                    button.performClick();// 로그인 버튼 클릭 효과

                    return true;
                }
                return false;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listName.clear();
                newCS.clear();
                final AsyncHttpClient client = new AsyncHttpClient();
                client.setCookieStore(new PersistentCookieStore(NamesrchActivity.this));

                client.get("http://220.69.209.49/measureset/search?q="+editText.getText().toString(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(getApplicationContext(), "검색 결과", Toast.LENGTH_SHORT).show();


                        for (int i = 0; i < response.length(); i++) {
                            newCS.add(new CSurvey(response, i));
                            listName.add(newCS.get(i).siteName+"  ("+newCS.get(i).createdAt+")");
                        }
                        hhList.invalidateViews();
                        hhList.refreshDrawableState();
                        Log.d("리스트랭스",response.length()+"");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getApplicationContext(), "서버 응답 없음\nstatus: "+statusCode, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        // 현장명 리스트뷰 어댑터 생성
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, listName);
        hhList.setAdapter(listAdapter);

        // 리스트뷰 아이템 클릭 시 액티비티 이동
        hhList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NamesrchActivity.this, ValueprintActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

    }
}
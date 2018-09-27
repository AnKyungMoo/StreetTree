package com.example.iclab.st;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;


// 제품 사양 액티비티
public class ProductActivity extends AppCompatActivity {
    int ind;
    int page = 0;
    File newfile = new File(Environment.getExternalStorageDirectory(),"Test.pdf");
    ArrayAdapter<String> listAdapter2 = null;
    ArrayAdapter<String> listAdapter = null;
    String attachmentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        final ListView listview = findViewById(R.id.productlist);
        Button backBtn4 = findViewById(R.id.backBtn4);
        final Button backBtn5 = findViewById(R.id.backBtn5);
        final List<String> attach = new ArrayList<>();//
        final List<String> listName0 = new ArrayList<>();// 리스트 전체
        final List<String> listName1 = new ArrayList<>();// ㅁㅁㅁ-ㅁㅁㅁ
        final List<String> listName2 = new ArrayList<>();// ㅁㅁㅁ-ㅁㅁㅁ-ㅁ
        final List<String> listName3 = new ArrayList<>();// ㅁㅁㅁ-ㅁㅁㅁ-ㅁㅁㅁ
//
        CookieSyncManager.createInstance(ProductActivity.this.getApplicationContext());
        CookieSyncManager.getInstance().sync();


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

        final AsyncHttpClient client = new AsyncHttpClient();
        client.setCookieStore(new PersistentCookieStore(ProductActivity.this));

        client.get(ProductActivity.this, "http://220.69.209.49/plates", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String plateId = object.getString("plate_id");
                        listName0.add(plateId);
                        attach.add(object.getString("attachmentUrl"));
                        ind = plateId.lastIndexOf("-");

                        if (!listName1.contains(plateId.substring(0, ind)))
                            listName1.add(plateId.substring(0, ind));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // 현장명 리스트뷰 어댑터 생성
                listAdapter = new ArrayAdapter<String>(ProductActivity.this, android.R.layout.simple_list_item_1, listName1);
                listview.setAdapter(listAdapter);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String s, Throwable throwable) {
                super.onFailure(statusCode, headers, s, throwable);
                Log.e("Test", "실패 " + statusCode);

            }
        });


        backBtn5.setVisibility(View.INVISIBLE);


        // 리스트뷰 아이템 클릭 시
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                backBtn5.setVisibility(View.VISIBLE);
                if (page == 0) {
                    listName2.clear();
                    // 리스트 클릭시 세부이름?
                    for (int i = 0; i < listName0.size(); i++)
                        if (listName0.get(i).contains(listName1.get(position)) && !listName2.contains(listName0.get(i).substring(0, ind + 2))) {
                            listName2.add(listName0.get(i).substring(0, ind + 2));
                            listName3.add(listName0.get(i));
                        }
                    listAdapter2 = new ArrayAdapter<String>(ProductActivity.this, android.R.layout.simple_list_item_1, listName2);
                    listview.setAdapter(listAdapter2);
                    page = 1;
                } else if (page == 1) {
                    for(int i=0;i<listName0.size();i++)
                        if(listName3.get(position).equals(listName0.get(i))) {
                            attachmentUrl = attach.get(i);
                        }

                    ////////////////
                    AsyncHttpClient client = new AsyncHttpClient();
                    PersistentCookieStore a=new PersistentCookieStore(ProductActivity.this);
                    client.setCookieStore(a);
                    client.setURLEncodingEnabled(false);
                    client.get("http://220.69.209.49"+attachmentUrl, new FileAsyncHttpResponseHandler(ProductActivity.this) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File response) {
                            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                FileOutputStream os;
                                try{
                                    os = new FileOutputStream(newfile);
                                    os.write(fileToBinary(response));
                                    os.close();
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                                if (newfile.exists()) {
                                    Intent intent=new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(
                                            FileProvider.getUriForFile(ProductActivity.this, getApplicationContext().getPackageName()+ ".fileprovider", newfile)
                                            , "application/pdf");
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivity(intent);

                                }else {
                                    Toast.makeText(ProductActivity.this, "PDF 파일이 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                              Log.d("Error", "메모리 로드 안됨");
                            }
                            /////////////////// 캐시 파일 비우기
                            File dir = new File(getCacheDir().getPath());
                            if (dir.exists()) {// 캐시파일 비우기
                                for (File f : dir.listFiles()) {
                                    f.delete();
                                }
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                            Log.e("Test", "실패 "+ statusCode+"\n"+file.getName()
                            +file+"\n"+file.toString());

                        }
                    });


                }
            }
        });


        backBtn5.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (page != 0) {

                    page--;

                    if (page == 0) {
                        listview.setAdapter(listAdapter);
                    } else if (page == 1) {
                        listview.setAdapter(listAdapter2);
                    }else if(page==2){

                    }
                    listview.invalidate();

                }
                if (page == 0)
                    backBtn5.setVisibility(View.INVISIBLE);

            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();

    }
    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();

    }
    public static byte[] fileToBinary(File file) {//  파일을 binary 값으로
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] fileArray=null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.d("err"," 파일 오류");
        }
        int len;
        byte[] buf = new byte[1024];
        try {
            while ((len = fis.read(buf)) != -1)
                baos.write(buf, 0, len);
            fileArray= baos.toByteArray();
            fis.close();
            baos.close();
        } catch (IOException e) {
            Log.d("err"," 변환 오류");
        }
        return fileArray;
    }


}



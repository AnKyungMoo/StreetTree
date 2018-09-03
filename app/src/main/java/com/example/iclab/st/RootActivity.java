package com.example.iclab.st;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class RootActivity extends AppCompatActivity {

    //그리기 뷰 전역 변수
    private DrawLine drawLine = null;
    static public String imageId="";
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    String timeStamp;
    String imageFileName;
    File photoFile;
    File screenShot;
    String screenShotName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.activity_root);

        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTakePhotoIntent();
            }
        });
        findViewById(R.id.memo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawLine.setLineColor(Color.CYAN);
            }
        });

        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawLine.setLineColor(Color.WHITE);
            }
        });

        findViewById(R.id.savePicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photoFile==null){
                    imageId=null;
                    Toast.makeText(getApplicationContext(), "사진 없음", Toast.LENGTH_SHORT).show();
                }else{
                    final AsyncHttpClient client = new AsyncHttpClient();
                    client.setCookieStore(new PersistentCookieStore(RootActivity.this));

                    Toast.makeText(getApplicationContext(), "사진 저장 중...", Toast.LENGTH_SHORT).show();
                    View rootView = getWindow().getDecorView();
                    screenShot = ScreenShot(rootView);

                    RequestParams params = new RequestParams();
                    try {
                        params.put("file", screenShot, "image/png");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    client.post(RootActivity.this,"http://220.69.209.49/upload", params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Toast.makeText(getApplicationContext(), "사진 전송 성공", Toast.LENGTH_SHORT).show();
                            try {
                                imageId=response.getString("file_id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            screenShot.delete();// 스크린 캡쳐 사진 삭제
                            finish(); // RootActivity 종료
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Toast.makeText(getApplicationContext(), "서버 응답 없음\nstatus: "+statusCode, Toast.LENGTH_SHORT).show();
                        }
                    });
                    photoFile.delete();// 원본 파일 삭제
                }
            }
        });
    }
    public File ScreenShot(View view){// 스크린 캡쳐
        view.setDrawingCacheEnabled(true);  // 캐시 o
        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환
        screenShotName = imageFileName+"screen.png";// 파일명
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), screenShotName);  // 경로와 파일명
        FileOutputStream os;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 95, os);   //비트맵을 PNG파일로 변환
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        view.setDrawingCacheEnabled(false);// 캐시 x
        return file;
    }

    // 카메라로 사진찍어서 넘겨주는 메소드
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    // 넘겨받은 이미지를 이미지뷰에 띄워주는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent

            data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            ((ImageView) findViewById(R.id.picture)).setImageBitmap(rotate(bitmap, exifDegree));
        }
    }

    // 이미지 파일을 생성하는 메소드
    private File createImageFile() throws IOException {
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    // 이미지 회전하는 메소드
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        //hasFocus : 앱이 화면에 보여졌을때
        if(hasFocus && drawLine == null)
        {
            RelativeLayout llcanvas =  findViewById(R.id.llCanvas);
            if(llcanvas != null) //그리기 뷰가 보여질 레이아웃이 있으면
            {
                //그리기 뷰 레이아웃의 넓이와 높이를 찾아서 Rect 변수 생성.
                Rect rect = new Rect(0, 0,
                        llcanvas.getMeasuredWidth(), llcanvas.getMeasuredHeight());
                //그리기 뷰 초기화
                drawLine = new DrawLine(this, rect);
                //그리기 뷰를 화면에 보이기
                llcanvas.addView(drawLine);
                drawLine.setLineColor(Color.CYAN);
            }
        }

        super.onWindowFocusChanged(hasFocus);
    }
}
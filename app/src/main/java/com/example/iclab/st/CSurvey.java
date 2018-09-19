package com.example.iclab.st;

import android.media.Image;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayList;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;


import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static com.example.iclab.st.NewplaceActivity.GCSurvey;

// static 지우고
public class CSurvey {
    // users
    public String id; // 로그인 ID
    public String authorId; // (아직없음)
    public String authorFullName; // 담당자 이름
    public String siteRegionCode; // 지역 코드 (아직없음)
    // field
    public String siteName; // 현장명
    public String clientName; // 발주처
    public String createdAt; // 실측 날짜
    public String measureset_id;

    public String salespersonName="";
    public String deliveryTarget="";
    public String deliveryDate="";
    public String differenceValue="";
    // survey
    ArrayList<SurveyList> list= new ArrayList<>();// 데이터 저장 리스트

    //
    public static void add_list(String plate, String tree_num, boolean is_installed, String points[],double la, double lo,String imageId,String si,String goon,String gu,String etStr,
                                Boolean frameCh,Boolean gagak,Boolean jiju)
    {
        SurveyList tmp = new SurveyList();
        tmp.plate_id = plate;
        tmp.treeNumber = tree_num;
        tmp.isInstalled = is_installed;
        tmp.points = points;
        tmp.latitude = la;
        tmp.longitude = lo;
        tmp.rootImageId=imageId;
        tmp.sido=si;
        tmp.goon=goon;
        tmp.gu=gu;
        tmp.memo=etStr;
        tmp.frameCheck=frameCh;
        tmp.gagakCheck=gagak;
        tmp.jijuguCheck=jiju;

        GCSurvey.list.add(tmp);
    }
    public CSurvey()
    {}

    public CSurvey(JSONObject JObject)
    {
        try {
            siteName = JObject.getString("siteName");
            clientName = JObject.getString("clientName");
            createdAt = JObject.getString("createdAt");
            salespersonName = JObject.getString("salespersonName");
            deliveryTarget = JObject.getString("deliveryTarget");
            deliveryDate = JObject.getString("deliveryDate");
            differenceValue = JObject.getString("differenceValue");
            measureset_id=JObject.getString("measureset_id");


            JSONArray newJArray = JObject.getJSONArray("measures");

            for(int k=0;k<newJArray.length();k++)
                list.add(new SurveyList(newJArray.getJSONObject(k)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

class  SurveyList {
    public int sequenceNumber;
    public String plate_id;// 보호판 이름
    public String treeNumber;
    public boolean isInstalled;
    public String points[];
    public String rootImageId;// 사진번호
    public double latitude;
    public double longitude;
    static int count = 1;
    public String sido;
    public String goon;
    public String gu;
    public String treeLocation="";
    public String memo="";
    public String measure_id="";

    public Boolean frameCheck;// 받침틀 납품여부
    public Boolean gagakCheck;// 가각 여부
    public Boolean jijuguCheck;// 지주구 여부

    public SurveyList() {
        points = new String[4];
        sequenceNumber = count++;
        points[3] = "";
    }
    public SurveyList(JSONObject JObject)
    {
        points = new String[4];
        points[3] = "";
        try {
            sequenceNumber = JObject.getInt("sequenceNumber");
            JSONObject tmp = JObject.getJSONObject("coordinates");
            latitude = tmp.getDouble("latitude");
            longitude = tmp.getDouble("longitude");
            tmp = JObject.getJSONObject("region");
            sido = tmp.getString("siCode");
            goon = tmp.getString("guCode");
            gu = tmp.getString("dongCode");

            plate_id = JObject.getString("plate_id");
            treeNumber = JObject.getString("treeNumber");
            isInstalled = JObject.getBoolean("isInstalled");

            for(int i=0;i< JObject.getJSONArray("points").length();i++)
                points[i] = JObject.getJSONArray("points").getString(i);
            rootImageId = JObject.getString("rootImageUrl");

            treeLocation  = JObject.getString("treeLocation");
            memo = JObject.getString("memo");

            measure_id=JObject.getString("measure_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
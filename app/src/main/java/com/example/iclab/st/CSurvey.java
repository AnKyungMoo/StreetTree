package com.example.iclab.st;

import android.media.Image;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayList;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

import static com.example.iclab.st.CompleteActivity.extraData;
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

    // survey
    ArrayList<SurveyList> list= new ArrayList<>();// 데이터 저장 리스트


    public static void add_list(String plate, String tree_num, boolean is_installed, String points[],double la, double lo,String imageId,String si,String goon,String gu)
    {
        SurveyList tmp = new SurveyList();
        tmp.plateName = plate;
        tmp.treeNumber = tree_num;
        tmp.isInstalled = is_installed;
        tmp.points = points;
        tmp.latitude = la;
        tmp.longitude = lo;
        tmp.rootImageId=imageId;
        tmp.sido=si;
        tmp.goon=goon;
        tmp.gu=gu;

        GCSurvey.list.add(tmp);

        String pointSum="";
        for(int i=0;i<4&&points[i]!=null;i++)
            pointSum+=points[i]+"  ";

        extraData+="No. "+(SurveyList.count-1)+"\n보호판 이름: "+plate+"\n나무번호: "+tree_num+"\n뿌리: "+pointSum+"\n\n";// 마지막 페이지 출력문
    }
}

class  SurveyList {
    public int sequenceNumber;
    public String plateName;// 보호판 이름
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


    public SurveyList() {
        points = new String[4];
        sequenceNumber = count++;
        points[3] = "";
    }

}
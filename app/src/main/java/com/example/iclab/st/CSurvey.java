package com.example.iclab.st;

import android.media.Image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayList;

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
    ArrayList<SurveyList> list= new ArrayList<>();

    public static void add_list(String plate, String tree_num, boolean is_installed, String points[],double la, double lo,String imageId)
    {
        SurveyList tmp = new SurveyList();
        tmp.plateName = plate;
        tmp.treeNumber = tree_num;
        tmp.isInstalled = is_installed;
        tmp.points = points;
        tmp.latitude = la;
        tmp.longitude = lo;
        tmp.rootImageId=imageId;
        GCSurvey.list.add(tmp);
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
    public SurveyList() {
        points = new String[4];
        sequenceNumber = count++;
        points[3] = "0";
    }

}
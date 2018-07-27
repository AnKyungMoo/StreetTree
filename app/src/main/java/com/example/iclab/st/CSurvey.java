package com.example.iclab.st;

import android.media.Image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayList;
public class CSurvey {
    // users
    static String id;
    static String name;
    // field
    static String field_name;
    static String client;
    static String date;

    // survey
    static ArrayList<SurveyList> list;
    //Collection list=new ArrayList();
    public  CSurvey()
    {
        list = new ArrayList<>();
    }
    public static void add_list(String plate, String tree_num, boolean is_installed, String points[],double la, double lo)
    {
        SurveyList tmp = new SurveyList();
        tmp.plate = plate;
        tmp.tree_number = tree_num;
        tmp.is_installed = is_installed;
        tmp.points = points;
        tmp.latitude = la;
        tmp.longitude = lo;

        list.add(tmp);
    }


}

class  SurveyList {
    public int number;
    public String plate;
    public String tree_number;
    public boolean is_installed;
    public String points[];
    // image ?
    public double latitude;
    public double longitude;
    static int count = 1;
    public SurveyList() {
        points = new String[4];
        number = count++;
        points[3] = "0";
    }
}
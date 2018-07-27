package com.example.iclab.st;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

import static com.example.iclab.st.NewplaceActivity.GCSurvey;


public class SendRequest extends StringRequest
{
    final static private String URL = "http://220.69.207.202";
    private Map<String, String> parameters;

    public SendRequest(Response.Listener<String> listener)
    {
        //super(Method.POST, URL, listener, null);
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<String, String>();

        Gson gson = new Gson();
        String json = gson.toJson(GCSurvey);

        parameters.put("str", json);

    }
    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}
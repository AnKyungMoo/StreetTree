package com.example.iclab.st;
import com.loopj.android.http.*;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LoginRequest extends StringRequest
{
    final static private String URL = "http://220.69.207.202/login";
    private Map<String, String> parameters;

    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", userID);
        parameters.put("pw", userPassword);


    }
    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }


}

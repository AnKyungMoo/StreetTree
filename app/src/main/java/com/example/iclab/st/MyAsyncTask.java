package com.example.iclab.st;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class MyAsyncTask extends AsyncTask<URL, Void, HashMap<String, Integer>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected HashMap<String, Integer> doInBackground(URL... urls) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

        try {
            StringBuffer jsonHtml = new StringBuffer();
            InputStream uis = urls[0].openConnection().getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(uis, "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                jsonHtml.append(line + "\r\n");
            }
            br.close();
            uis.close();

            JSONArray jsonArray = new JSONArray(jsonHtml.toString());

            for (int i = 0; i < jsonArray.length(); ++i)
            {
                Log.d("index: ", i + "");
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String a = jsonObject.getString("value");
                Integer b = new Integer(jsonObject.getInt("code"));


                Log.d("code: ", a);
                Log.d("value: ", b + "");

                hashMap.put(a, b);
            }

            return hashMap;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
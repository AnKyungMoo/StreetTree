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
import java.util.LinkedHashMap;

public class URLAsyncTask extends AsyncTask<URL, Void, LinkedHashMap<String, Integer>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected LinkedHashMap<String, Integer> doInBackground(URL... urls) {
        LinkedHashMap<String, Integer> regionMap = new LinkedHashMap<String, Integer>();

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

                String key = jsonObject.getString("value");
                Integer value = jsonObject.getInt("code");


                Log.d("key: ", key);
                Log.d("value: ", value + "");

                regionMap.put(key, value);
            }

            return regionMap;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
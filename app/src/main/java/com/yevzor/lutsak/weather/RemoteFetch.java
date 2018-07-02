package com.yevzor.lutsak.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

public class RemoteFetch {

    private static final String TAG = RemoteFetch.class.getSimpleName();

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    public static JSONObject getJSON(Context context, String city){
        Log.d(TAG, "getJSON");

        URL url;
        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String tempString;
        JSONObject jsonObject;

        try {

            url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            httpURLConnection = (HttpURLConnection)url.openConnection();

            httpURLConnection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            bufferedReader =
                    new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            stringBuilder =
                    new StringBuilder(1024);


            while((tempString = bufferedReader.readLine()) != null){
                stringBuilder.append(tempString).append("\n");
            }
            bufferedReader.close();

            jsonObject = new JSONObject(stringBuilder.toString());

            if(jsonObject.getInt("cod") != 200){
                return null;
            }

            return jsonObject;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
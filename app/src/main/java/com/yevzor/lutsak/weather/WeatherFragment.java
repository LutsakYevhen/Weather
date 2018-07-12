package com.yevzor.lutsak.weather;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONObject;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherFragment extends Fragment {

    private static final String TAG = WeatherFragment.class.getSimpleName();

    private Typeface mTypeface;
    private TextView mCity;
    private TextView mUpdate;
    private TextView mDetails;
    private TextView mCurrentTemperature;
    private TextView mWeatherIcon;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, ">> onCreate WeatherFragment");

        mTypeface = Typeface.createFromAsset(getResources().getAssets(), "weather.ttf");
        updateWeatherData(new CityPreference(getActivity()).getCity());

        Log.d(TAG, "<< onCreate WeatherFragment");
    }

    public WeatherFragment(){
        Log.d(TAG, "WeatherFragment");

        mHandler = new Handler();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        mCity = rootView.findViewById(R.id.city_field);
        mUpdate = rootView.findViewById(R.id.updated_field);
        mDetails = rootView.findViewById(R.id.details_field);
        mCurrentTemperature = rootView.findViewById(R.id.current_temperature_field);
        mWeatherIcon = rootView.findViewById(R.id.weather_icon);

        mWeatherIcon.setTypeface(mTypeface);
        return rootView;
    }

    private void updateWeatherData(final String city){
        Log.d(TAG, "updateWeatherData");

        new Thread(){
            public void run(){
                final JSONObject jsonObject = RemoteFetch.getJSON(getActivity(), city);
                if(jsonObject == null){
                    mHandler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getResources().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    mHandler.post(new Runnable(){
                        public void run(){
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        Log.d(TAG, "renderWeather");

        try {

            String cityText =
                    json.getString("name").toUpperCase(Locale.US) + ", " +
                            json.getJSONObject("sys").getString("country");

            mCity.setText(cityText);

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            String detailsText = details.getString("description").toUpperCase(Locale.US) +
                    "\n" + "Humidity: " + main.getString("humidity") + "%" +
                    "\n" + "Pressure: " + main.getString("pressure") + " hPa";

            mDetails.setText(detailsText);

            String currentTemperature = main.getDouble("temp") + " ℃";
            mCurrentTemperature.setText(currentTemperature);

            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            Date date = new Date(json.getLong("dt") * 1000);
            String updatedOn = "Last update: " + dateFormat.format(date);
            mUpdate.setText(updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        Log.d(TAG, "setWeatherIcon");

        final String weatherSunny = getResources().getString(R.string.weather_sunny);
        final String weatherClearNight = getResources().getString(R.string.weather_clear_night);
        final String weatherThunder = getResources().getString(R.string.weather_thunder);
        final String weatherDrizzle = getResources().getString(R.string.weather_drizzle);
        final String weatherFoggy = getResources().getString(R.string.weather_foggy);
        final String weatherCloudy = getResources().getString(R.string.weather_cloudy);
        final String weatherSnowy = getResources().getString(R.string.weather_snowy);
        final String weatherRainy = getResources().getString(R.string.weather_rainy);

        int id = actualId / 100;
        String icon = "";

        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime >= sunrise && currentTime < sunset) {
                icon = weatherSunny;
            } else {
                icon = weatherClearNight;
            }
        } else {
            switch(id) {
                case 2 : icon = weatherThunder;
                    break;
                case 3 : icon = weatherDrizzle;
                    break;
                case 7 : icon = weatherFoggy;
                    break;
                case 8 : icon = weatherCloudy;
                    break;
                case 6 : icon = weatherSnowy;
                    break;
                case 5 : icon = weatherRainy;
                    break;
            }
        }
        mWeatherIcon.setText(icon);
    }

    public void changeCity(String city){
        Log.d(TAG, "changeCity");

        updateWeatherData(city);
    }
}
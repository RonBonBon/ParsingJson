package com.arichafamily.parsingjson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Get Weather Data from OpenWeatherMap API: application programming interface
 */

public class WeatherDataSource {
    //http://api.openweathermap.org/data/2.5/weather?q=Tel-Aviv,IL&appid=288ca3c192923f79bd74f4d01a9299c0&units=metric

    public interface OnWeatherArrivedListener{
        void onWeatherArrived(Weather data, Exception e);
    }

    public static void getWeather(final OnWeatherArrivedListener listener) {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Tel-Aviv,IL&appid=288ca3c192923f79bd74f4d01a9299c0&units=metric");
                    URLConnection con = url.openConnection();
                    InputStream in = con.getInputStream();
                    String json = IO.getString(in);
                    Weather w = parseJson(json);
                    listener.onWeatherArrived(w, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onWeatherArrived(null, e);
                }
            }
        });
    }

    private static Weather parseJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray weatherArray = root.getJSONArray("weather");
        JSONObject weatherObject = weatherArray.getJSONObject(0);
        String description = weatherObject.getString("description");
        String icon = weatherObject.getString("icon");
        double temp = root.getJSONObject("main").getDouble("temp");
        JSONObject sys = root.getJSONObject("sys");
        long sunrise = sys.getLong("sunrise");
        long sunset = sys.getLong("sunset");
        return new Weather(description, icon, temp, sunrise, sunset);
    }


    public static class Weather {
        //properties:
        private String description;
        private String icon;
        private double temp;
        private long sunset;
        private long sunrise;

        //constructor
        public Weather(String description, String icon, double temp, long sunset, long sunrise) {
            this.description = description;
            this.icon = icon;
            this.temp = temp;
            this.sunset = sunset;
            this.sunrise = sunrise;
        }

        //getters
        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        public double getTemp() {
            return temp;
        }

        public long getSunset() {
            return sunset;
        }

        public long getSunrise() {
            return sunrise;
        }

        //toString()
        @Override
        public String toString() {
            return "Weather{" +
                    "description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    ", temp=" + temp +
                    ", sunset=" + sunset +
                    ", sunrise=" + sunrise +
                    '}';
        }
    }
}

package com.arichafamily.parsingjson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hackeru on 01/06/2017.
 */

public class MovieDataSource {

    public interface OnMovieArrivedListener{
        void onMoviesArrived(List<Movie> data, Exception e);
    }

    public static void getMovies(OnMovieArrivedListener listener){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://api.androidhive.info/json/movies.json");
                    URLConnection con = url.openConnection();
                    InputStream in = con.getInputStream();
                    String json = IO.getString(in);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static List<Movie> parseJson(String json) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();

        JSONArray root = new JSONArray(json);
        for (int i = 0; i < root.length(); i++) {
            JSONObject movieObject = root.getJSONObject(i);
            String title = movieObject.getString("title");
            String image = movieObject.getString("image");
            double rating = movieObject.getDouble("rating");
            int releaseYear = movieObject.getInt("releaseYear");
            JSONArray genresArray = movieObject.getJSONArray("genre");
            String[] genres = new String[genresArray.length()];
            for (int j = 0; j < genres.length; j++) {
                genres[j] = genresArray.getString(j);
            }
            movies.add(new Movie(title,image,rating,releaseYear,genres);
        }

        return movies;
    }

    public static class Movie{
        private final String title;
        private final String image;
        private final int releaseYear;
        private final double rating;
        private final String[] genre;

        public Movie(String title, String image, int releaseYear, double rating, String[] genre) {
            this.title = title;
            this.image = image;
            this.releaseYear = releaseYear;
            this.rating = rating;
            this.genre = genre;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public int getReleaseYear() {
            return releaseYear;
        }

        public double getRating() {
            return rating;
        }

        public String[] getGenre() {
            return genre;
        }

        @Override
        public String toString() {
            return "Movie{" +
                    "title='" + title + '\'' +
                    ", image='" + image + '\'' +
                    ", releaseYear=" + releaseYear +
                    ", rating=" + rating +
                    ", genre=" + Arrays.toString(genre) +
                    '}';
        }
    }
}

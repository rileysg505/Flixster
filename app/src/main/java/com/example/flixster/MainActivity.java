package com.example.flixster;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL= "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    //^takes in now-playing movies url --> embed api key in url
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvMovies= findViewById(R.id.rvMovies);
        movies= new ArrayList<>();

        //Create Adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        //set adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);
        //set layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client= new AsyncHttpClient(); //creates instance of Http client
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() { // creates get request for url, use JSON bc api returns JSON
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject; //calls json object
                try {
                    JSONArray results = jsonObject.getJSONArray("results");//creats results variable with results data from json
                    Log.i(TAG,"Results:"+ results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG,"Movies:"+ movies.size());

                } catch (JSONException e) {
                    Log.e(TAG,"Hit JSON exception", e);

                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG,"onFailure");
            }
        });


    }
}

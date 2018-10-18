package com.examole.moviedirectory.moviedirectory.Activities;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.examole.moviedirectory.moviedirectory.Model.Movie;
import com.examole.moviedirectory.moviedirectory.R;
import com.examole.moviedirectory.moviedirectory.Util.Constansts;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetail extends AppCompatActivity {

    private TextView movieTitle;
    private ImageView movieImage;
    private TextView movieYear;
    private TextView director;
    private TextView category;
    private TextView runtime;
    private TextView actors;
    private TextView writers;
    private TextView plot;
    private TextView boxOffice;
    private TextView rating;
    private Movie movie;
    private String movieId;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie=(Movie)getIntent().getSerializableExtra("movie");
        movieId=movie.getImdbId();
        this.setTitle(movie.getTitle());
        queue= Volley.newRequestQueue(this);
        setupUI();
        getMovieDetails(movieId);
    }

    private void setupUI() {
        movieTitle=findViewById(R.id.TvMovieTitleDet);
        movieImage=findViewById(R.id.ivMovieImageDet);
        movieYear=findViewById(R.id.TvMovieReleaseDet);
        director=findViewById(R.id.tvDirectedByDet);
        category=findViewById(R.id.tvMovieCatDet);
        runtime=findViewById(R.id.tvRuntimeDet);
        actors=findViewById(R.id.tvActorsDet);
        writers=findViewById(R.id.tvWritersDet);
        plot=findViewById(R.id.tvPlotDet);
        boxOffice=findViewById(R.id.tvBoxOfficeDet);
        rating=findViewById(R.id.tvRatingDet);
    }

    private void getMovieDetails(String movieId) {
        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, Constansts.URL + movieId + Constansts.API_KEY, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.has("Ratings")) {
                        System.out.println("reponse has ratings");
                        JSONArray ratings = response.getJSONArray("Ratings");
                        String source = null;
                        String value = null;
                        if (ratings.length() > 0) {
                            System.out.println("Ratings length is greater than 1");
                            JSONObject mratings = ratings.getJSONObject(ratings.length() - 1);
                            source = mratings.getString("Source");
                            value = mratings.getString("Value");
                            rating.setText(source + ":" + value);
                        } else {
                            rating.setText("Rating: N/A");
                        }

                        movieTitle.setText(response.getString("Title"));
                        movieYear.setText("Year: "+ response.getString("Year"));
                        director.setText("Director: "+response.getString("Director"));
                        category.setText("Genre: "+response.getString("Genre"));
                        runtime.setText("Runtime: "+response.getString("Runtime"));
                        actors.setText("Actors: "+ response.getString("Actors"));
                        writers.setText("Writer: "+response.getString("Writer"));
                        plot.setText("Plot: "+response.getString("Plot"));
                        boxOffice.setText("Box Office Collection: "+response.getString("BoxOffice"));
                        System.out.println("All widgets are loaded with information");
                        String poster=response.getString("Poster");
                        Picasso.with(getApplicationContext()).load(poster).into(movieImage);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ",error.getMessage());

            }
        });
        queue.add(objectRequest);
    }


}

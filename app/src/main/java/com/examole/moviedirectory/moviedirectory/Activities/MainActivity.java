package com.examole.moviedirectory.moviedirectory.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.examole.moviedirectory.moviedirectory.Data.MovieRecyclerViewAdapter;
import com.examole.moviedirectory.moviedirectory.Model.Movie;
import com.examole.moviedirectory.moviedirectory.R;
import com.examole.moviedirectory.moviedirectory.Util.Constansts;
import com.examole.moviedirectory.moviedirectory.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private List<Movie> tempList;
    private RequestQueue queue;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private EditText etSearch;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue= Volley.newRequestQueue(this);
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();

            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList=new ArrayList<>();
        tempList=new ArrayList<>();

        Prefs prefs=new Prefs(MainActivity.this);
        String search=prefs.getSearch();
        movieList= getMovies(search);
        movieRecyclerViewAdapter=new MovieRecyclerViewAdapter(this,movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();
        System.out.println("The size of movie list is: oncreate:"+movieList.size());

    }
    //Get Movies
    public List<Movie> getMovies(String search){
        movieList.clear();
         tempList.clear();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Constansts.URL_LEFT + search + Constansts.URL_RIGHT + Constansts.API_KEY, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayRequest=response.getJSONArray("Search");
                    System.out.println("Array length: "+jsonArrayRequest.length());
                    for(int i=0;i<jsonArrayRequest.length();i++){
                        JSONObject movieObject=jsonArrayRequest.getJSONObject(i);
                        Movie movie=new Movie();
                        movie.setTitle(movieObject.getString("Title"));
                        movie.setYear("Year: "+ movieObject.getString("Year"));
                        movie.setMovieType("Type: "+ movieObject.getString("Type"));
                        movie.setPoster(movieObject.getString("Poster"));
                        movie.setImdbId(movieObject.getString("imdbID"));
                        tempList.add(movie);
                        Log.d("Movie"+movie.getTitle(),"added successfully");
                        System.out.println("The size of movie list is:"+movieList.size());
                    }
                    movieRecyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
        return tempList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_search) {
            createPopupDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPopupDialog(){
        alertDialogBuilder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.dialog_view,null);
        etSearch=view.findViewById(R.id.etSearch);
        submit=view.findViewById(R.id.btSubmit);
        alertDialogBuilder.setView(view);
        alertDialog=alertDialogBuilder.create();
        alertDialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs = new Prefs(MainActivity.this);

                if (!etSearch.getText().toString().isEmpty()) {
                    String search_item = etSearch.getText().toString();
                    prefs.setSearch(search_item);
                    movieList.clear();
                    getMovies(search_item);

                }
                alertDialog.dismiss();
            }
        });
    }
}

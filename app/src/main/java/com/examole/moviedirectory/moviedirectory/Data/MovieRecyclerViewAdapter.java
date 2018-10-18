package com.examole.moviedirectory.moviedirectory.Data;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examole.moviedirectory.moviedirectory.Activities.MovieDetail;
import com.examole.moviedirectory.moviedirectory.Model.Movie;
import com.examole.moviedirectory.moviedirectory.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Movie> movieList;
    public MovieRecyclerViewAdapter(Context context,List<Movie> movies){
        this.context=context;
        movieList=movies;
    }
    @NonNull
    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerViewAdapter.ViewHolder holder, int position) {

        Movie movie=movieList.get(position);
        String posterLink=movie.getPoster();
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYear());
        holder.type.setText(movie.getMovieType());
        Picasso.with(context).load(posterLink).placeholder(android.R.drawable.ic_btn_speak_now)
                .into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView year;
        TextView type;
        ImageView poster;
        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context=ctx;
            title=itemView.findViewById(R.id.TvMovieTitle);
            year=itemView.findViewById(R.id.TvMovieRelease);
            type=itemView.findViewById(R.id.tvMovieCat);
            poster=itemView.findViewById(R.id.ivMovieImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie=movieList.get(getAdapterPosition());
                    Intent intent=new Intent(context, MovieDetail.class);
                    intent.putExtra("movie",movie);
                    ctx.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {




        }
    }
}

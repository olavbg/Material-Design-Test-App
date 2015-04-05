package materialtest.vivz.slidenerd.materialtest.addmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import materialtest.vivz.slidenerd.materialtest.Movie;
import materialtest.vivz.slidenerd.materialtest.R;
import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;
import materialtest.vivz.slidenerd.materialtest.utils.MovieList;
import materialtest.vivz.slidenerd.materialtest.utils.VolleyRequest;

import static android.text.TextUtils.isEmpty;

public class MovieSearchCardAdapter extends RecyclerView.Adapter<MovieSearchCardAdapter.MovieViewHolder> {
    List<SimpleSearchedMovie> movieList;

    public MovieSearchCardAdapter(final ArrayList<SimpleSearchedMovie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.movie_search_row, viewGroup, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder movieViewHolder, final int i) {
        final SimpleSearchedMovie movie = movieList.get(i);

        String posterUrl = "";
        final int posterHeight = 240;
        if (!isEmpty(movie.getImdbID())) {
            posterUrl = "http://img.omdbapi.com/?i=" + movie.getImdbID() + "&apikey=7490539a&h=" + posterHeight;
        }
        if (!isEmpty(posterUrl)) {
            Picasso.with(movieViewHolder.context)
                    .load(posterUrl)
                    .resize(220, 354)
                    .centerCrop()
                    .into(movieViewHolder.poster);
        }else{
            movieViewHolder.poster.setImageDrawable(null);
        }

        movieViewHolder.title.setText(movie.getTitle());

        if (movie.getYear().isEmpty() || movie.getYear().equalsIgnoreCase("0")) {
            movieViewHolder.year.setText(null);
        } else {
            movieViewHolder.year.setText(movie.getYear());
        }
        if (!movie.getType().isEmpty()) {
            movieViewHolder.tagline.setText("Type: " + movie.getType());
        } else {
            movieViewHolder.tagline.setText(null);
        }
        movieViewHolder.addMovie.setChecked(MovieList.getMovieBySearchIdent(movie.getImdbID(), movie.getFormat()) != null);
        if (!movie.getFormat().isEmpty()) {
            movieViewHolder.format.setText(movie.getFormat());
        }

        movieViewHolder.addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieViewHolder.addMovie.isChecked()) {
                    //TODO: Legg til filmen
                    final Movie newMovie = movie.convertFromSearch();
                    GlobalVars.requestQueue.add(VolleyRequest.getAddMovieRequest(newMovie, movieViewHolder.addMovie));
                } else {
                    final Movie movieBySearchIdent = MovieList.getMovieBySearchIdent(movie.getImdbID(), movie.getFormat());
                    if (movieBySearchIdent != null) {
                        GlobalVars.requestQueue.add(VolleyRequest.getDeleteMovieRequest(movieBySearchIdent, movieViewHolder.addMovie));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        protected Context context;
        protected LinearLayout movieRowLinearLayout;
        protected ImageView poster;
        protected TextView title;
        protected TextView year;
        protected TextView tagline;
        protected CheckBox addMovie;
        protected TextView format;

        public MovieViewHolder(View v) {
            super(v);
            context = v.getContext();
            movieRowLinearLayout = (LinearLayout) v.findViewById(R.id.movieRowLinearLayout);
            poster = (ImageView) v.findViewById(R.id.poster);
            title = (TextView) v.findViewById(R.id.title);
            year = (TextView) v.findViewById(R.id.year);
            tagline = (TextView) v.findViewById(R.id.tagline);
            format = (TextView) v.findViewById(R.id.format);
            addMovie = (CheckBox) v.findViewById(R.id.addMovie);
        }
    }
}

package materialtest.vivz.slidenerd.materialtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.text.TextUtils.isEmpty;

public class MovieCardAdapter extends RecyclerView.Adapter<MovieCardAdapter.MovieViewHolder> {
    List<Movie> movieList;

    public MovieCardAdapter(final List<Movie> movieList) {
        this.movieList = movieList;
    }

    public void addItem(int position, final Movie movie){
        movieList.add(position,movie);
        notifyItemInserted(position);
    }

    public void removeItem(final Movie movie){
        final int indexOf = movieList.indexOf(movie);
        movieList.remove(movie);
        notifyItemRemoved(indexOf);
    }

    public void updateItems(final List<Movie> items){
        movieList = items;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.movie_row, viewGroup, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder movieViewHolder, int i) {
        final Movie movie = movieList.get(movieViewHolder.getPosition());

        String posterUrl = "";
        final int posterHeight = 240;
        if (!isEmpty(movie.getImdb_id())) {
            posterUrl = "http://img.omdbapi.com/?i=" + movie.getImdb_id() + "&apikey=7490539a&h=" + posterHeight;
        } else if (!isEmpty(movie.getPoster())) {
            posterUrl = movie.getPoster();
        }
        if (!isEmpty(posterUrl)) {
            Picasso.with(movieViewHolder.context)
                    .load(posterUrl)
                    .resize(220, 354)
                    .centerCrop()
                    .into(movieViewHolder.poster);
        }

        movieViewHolder.title.setText(movie.getTittel());
        movieViewHolder.format.setText(movie.getFormat());

        if (movie.getRuntime().isEmpty() || movie.getRuntime().equalsIgnoreCase("0")){
            movieViewHolder.runtime.setText("");
        }else{
            movieViewHolder.runtime.setText(movie.getRuntime()+" mins");
        }

        if (!movie.getTagline().isEmpty()) {
            movieViewHolder.tagline.setText(movie.getTagline());
        } else if (!movie.getPlot().isEmpty()) {
            movieViewHolder.tagline.setText(movie.getPlot());
        }
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
        protected TextView runtime;
        protected TextView tagline;
        protected TextView format;

        public MovieViewHolder(View v) {
            super(v);
            context = v.getContext();
            movieRowLinearLayout = (LinearLayout) v.findViewById(R.id.movieRowLinearLayout);
            poster = (ImageView) v.findViewById(R.id.poster);
            title = (TextView) v.findViewById(R.id.title);
            runtime = (TextView) v.findViewById(R.id.runtime);
            tagline = (TextView) v.findViewById(R.id.tagline);
            format = (TextView) v.findViewById(R.id.format);
        }
    }
}

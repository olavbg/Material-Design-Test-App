package materialtest.vivz.slidenerd.materialtest.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import materialtest.vivz.slidenerd.materialtest.Movie;
import materialtest.vivz.slidenerd.materialtest.R;

import static android.text.TextUtils.isEmpty;

public class MovieCardAdapter extends AbstractListAdapter<Movie, MovieCardAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public MovieCardAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(
                mInflater.inflate(R.layout.movie_row, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(mData.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout movieRowLinearLayout;
        protected ImageView poster;
        protected TextView title;
        protected TextView runtime;
        protected TextView tagline;
        protected TextView format;
        private Movie movie;

        public ViewHolder(View v) {
            super(v);
            movieRowLinearLayout = (LinearLayout) v.findViewById(R.id.movieRowLinearLayout);
            poster = (ImageView) v.findViewById(R.id.poster);
            title = (TextView) v.findViewById(R.id.title);
            runtime = (TextView) v.findViewById(R.id.runtime);
            tagline = (TextView) v.findViewById(R.id.tagline);
            format = (TextView) v.findViewById(R.id.format);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(movie);
                    }
                }
            });
        }

        public void bind(Movie movie) {
            this.movie = movie;
            String posterUrl = "";
            final int posterHeight = 240;
            if (!isEmpty(movie.getImdb_id())) {
                posterUrl = "http://img.omdbapi.com/?i=" + movie.getImdb_id() + "&apikey=7490539a&h=" + posterHeight;
            } else if (!isEmpty(movie.getPoster())) {
                posterUrl = movie.getPoster();
            }
            if (!isEmpty(posterUrl)) {
//                Ion.with(poster).load(posterUrl);
                Picasso.with(mContext)
                        .load(posterUrl)
                        .resize(220, 354)
                        .centerCrop()
                        .into(poster);
            }else{
                poster.setImageDrawable(null);
                poster.setImageBitmap(null);
                poster.setImageURI(null);
            }

            title.setText(movie.getTittel());
            format.setText(movie.getFormat());

            if (movie.getRuntime().isEmpty() || movie.getRuntime().equalsIgnoreCase("0")){
                runtime.setText("");
            }else{
                runtime.setText(movie.getRuntime()+ (movie.getRuntime().contains("min") ? "" : " mins"));
            }

            if (!movie.getTagline().isEmpty()) {
                tagline.setText(movie.getTagline());
            } else if (!movie.getPlot().isEmpty()) {
                tagline.setText(movie.getPlot());
            }else{
                tagline.setText(null);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }
}
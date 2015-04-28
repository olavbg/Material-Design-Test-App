package materialtest.vivz.slidenerd.materialtest.Events;

import java.util.ArrayList;

import materialtest.vivz.slidenerd.materialtest.Movie;

public class SetMovieListEvent {
    private final ArrayList<Movie> movies;

    public SetMovieListEvent(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }
}

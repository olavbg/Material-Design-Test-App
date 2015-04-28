package materialtest.vivz.slidenerd.materialtest.Events;

import materialtest.vivz.slidenerd.materialtest.Movie;

public class AddMovieEvent {
    private final Movie movie;

    public AddMovieEvent(final Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}

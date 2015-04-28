package materialtest.vivz.slidenerd.materialtest.Events;

import materialtest.vivz.slidenerd.materialtest.Movie;

public class MovieDeletedEvent {
    private final Movie movie;

    public MovieDeletedEvent(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}

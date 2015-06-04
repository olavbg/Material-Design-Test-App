package materialtest.vivz.slidenerd.materialtest.Events;

import materialtest.vivz.slidenerd.materialtest.Movie;
import materialtest.vivz.slidenerd.materialtest.utils.Callback;

public class MovieDeletedEvent {
    private final Movie movie;
    private final Callback<Boolean> callback;

    public MovieDeletedEvent(Movie movie, Callback<Boolean> callback) {
        this.movie = movie;
        this.callback = callback;
    }

    public Movie getMovie() {
        return movie;
    }

    public void finishEvent() {
        if (callback != null) {
            callback.call(Boolean.TRUE);
        }
    }
}

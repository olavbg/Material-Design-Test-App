package materialtest.vivz.slidenerd.materialtest.utils;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import materialtest.vivz.slidenerd.materialtest.Events.SetMovieListEvent;
import materialtest.vivz.slidenerd.materialtest.Movie;
import materialtest.vivz.slidenerd.materialtest.R;
import materialtest.vivz.slidenerd.materialtest.recyclerview.MovieCardAdapter;

import static android.text.TextUtils.isEmpty;
import static materialtest.vivz.slidenerd.materialtest.utils.GlobalVars.PREF_KEY_BORROWED_MOVIE_CACHE;
import static materialtest.vivz.slidenerd.materialtest.utils.GlobalVars.PREF_KEY_LENT_MOVIE_CACHE;
import static materialtest.vivz.slidenerd.materialtest.utils.GlobalVars.PREF_KEY_MOVIE_CACHE;
import static materialtest.vivz.slidenerd.materialtest.utils.GlobalVars.gson;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.context;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.readFromPreferences;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.saveToPreferences;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.showToast;

public class MovieList {
    public static ArrayList<Movie> movies = new ArrayList<>();
    public static ArrayList<Movie> borrowedMovies = new ArrayList<>();
    public static ArrayList<Movie> lentMovies = new ArrayList<>();
    private static HashMap<Types.ChosenListType, ArrayList<Movie>> typeArrayListMap = new HashMap<Types.ChosenListType, ArrayList<Movie>>() {{
        put(Types.ChosenListType.Your, movies);
        put(Types.ChosenListType.Borrowed, borrowedMovies);
        put(Types.ChosenListType.Lent, lentMovies);
    }};
    public static Types.ChosenListType selectedListType = Types.ChosenListType.Your;
    public static ArrayList<Movie> selectedList = new ArrayList<>();

    public static void setSelectedList(final Types.ChosenListType chosenListType) {
        selectedListType = chosenListType;
        selectedList = typeArrayListMap.get(selectedListType);
        EventBus.getDefault().post(new SetMovieListEvent(selectedList));
    }

    public static int add(final Movie movie) {
        if (!userHasMovie(movie)) {
            if (movie.isBorrowed()) {
                borrowedMovies.add(movie);
            } else if (movie.isLent()) {
                lentMovies.add(movie);
            } else {
                movies.add(movie);
                return movies.indexOf(movie);
            }
        }
        return -1;
    }

    public static void addMovies(final List<Movie> movies) {
        for (Movie movie : movies) {
            add(movie);
        }
        cacheMoviesLocally();
    }

    public static boolean userHasMovie(final Movie movie) {
        for (Movie oldMovie : getAllMovies()) {
            if (oldMovie.getTittel().equals(movie.getTittel()) &&
                    oldMovie.getFormat().equals(movie.getFormat())) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Movie> getYourMovies() {
        return movies;
    }

    public static ArrayList<Movie> getBorrowedMovies() {
        return borrowedMovies;
    }

    public static ArrayList<Movie> getLentMovies() {
        return lentMovies;
    }

    public static ArrayList<Movie> getAllMovies() {
        final ArrayList<Movie> allMovies = new ArrayList<>(movies);
        allMovies.addAll(borrowedMovies);
        allMovies.addAll(lentMovies);
        Collections.sort(allMovies, new MovieTitleComparator());
        return allMovies;
    }

    public static Movie getMovieById(final int id) {
        for (Movie movie : getAllMovies()) {
            if (movie.getFilmID() == id) {
                return movie;
            }
        }
        return null;
    }

    public static Movie getMovieByIdent(final String title, final String format) {
        for (Movie movie : getAllMovies()) {
            if (movie.getTittel().equalsIgnoreCase(title.trim()) && movie.getFormat().equalsIgnoreCase(format.trim())) {
                return movie;
            }
        }
        return null;
    }

    public static Movie getMovieBySearchIdent(String imdbID, final String format) {
        for (Movie movie : getAllMovies()) {
            if (movie.getImdb_id().equals(imdbID) && movie.getFormat().equalsIgnoreCase(format)) {
                return movie;
            }
        }
        return null;
    }

    public static void updateMovie(final Movie updatedMovie, final MovieCardAdapter adapter, Activity activity) {
        int counter = 0;
        for (Movie movie : movies) {
            if (movie.getFilmID() == updatedMovie.getFilmID()) {
                movies.set(movies.indexOf(movie), updatedMovie);
                counter++;
                break;
            }
        }
        for (Movie movie : borrowedMovies) {
            if (movie.getFilmID() == updatedMovie.getFilmID()) {
                borrowedMovies.set(movies.indexOf(movie), updatedMovie);
                counter++;
                break;
            }
        }
        for (Movie movie : lentMovies) {
            if (movie.getFilmID() == updatedMovie.getFilmID()) {
                lentMovies.set(movies.indexOf(movie), updatedMovie);
                counter++;
                break;
            }
        }
        if (adapter != null && counter > 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
        showToast("Movies updated: " + counter);
    }

    public static void cacheMoviesLocally() {
        saveToPreferences(PREF_KEY_MOVIE_CACHE, gson.toJson(movies));
        saveToPreferences(PREF_KEY_BORROWED_MOVIE_CACHE, gson.toJson(borrowedMovies));
        saveToPreferences(PREF_KEY_LENT_MOVIE_CACHE, gson.toJson(lentMovies));
    }

    public static void loadCachedMoviesIfAny() {
        final String moviesJson = readFromPreferences(PREF_KEY_MOVIE_CACHE, "");
        final String borrowedMoviesJson = readFromPreferences(PREF_KEY_BORROWED_MOVIE_CACHE, "");
        final String lentMoviesJson = readFromPreferences(PREF_KEY_LENT_MOVIE_CACHE, "");

        try {
            if (!isEmpty(moviesJson)) {
                movies.clear();
                final JSONArray jsonArray = new JSONArray(moviesJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    movies.add(gson.fromJson(jsonArray.getString(i), Movie.class));
                }
            }
            if (!isEmpty(borrowedMoviesJson)) {
                borrowedMovies.clear();
                final JSONArray jsonArray = new JSONArray(borrowedMoviesJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    borrowedMovies.add(gson.fromJson(jsonArray.getString(i), Movie.class));
                }
            }
            if (!isEmpty(lentMoviesJson)) {
                lentMovies.clear();
                final JSONArray jsonArray = new JSONArray(lentMoviesJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    lentMovies.add(gson.fromJson(jsonArray.getString(i), Movie.class));
                }
            }
            refreshList();
        } catch (JSONException e) {
            showToast(context.getString(R.string.error_readingFromCache));
            e.printStackTrace();
        }
    }

    public static void sortMoviesByTitle() {
        Collections.sort(movies, new MovieTitleComparator());
        Collections.sort(borrowedMovies, new MovieTitleComparator());
        Collections.sort(lentMovies, new MovieTitleComparator());
    }

    public static void removeMovies(ArrayList<Movie> moviesToRemove) {
        for (Movie movieToRemove : moviesToRemove) {
            removeMovie(movieToRemove);
        }
    }

    public static void removeMovie(final Movie movieToRemove) {
        for (Movie movie : movies) {
            if (movie.getFilmID() == movieToRemove.getFilmID()) {
                movies.remove(movieToRemove);
                return;
            }
        }
        for (Movie movie : borrowedMovies) {
            if (movie.getFilmID() == movieToRemove.getFilmID()) {
                borrowedMovies.remove(movieToRemove);
                return;
            }
        }
        for (Movie movie : lentMovies) {
            if (movie.getFilmID() == movieToRemove.getFilmID()) {
                lentMovies.remove(movie);
                return;
            }
        }
    }

    public static void clearAll() {
        movies.clear();
        borrowedMovies.clear();
        lentMovies.clear();
        cacheMoviesLocally();
    }

    public static void refreshList() {
        MovieList.sortMoviesByTitle();
        setSelectedList(selectedListType);
    }

    public static class MovieTitleComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie movie, Movie movie2) {
            return movie.getTittel().compareTo(movie2.getTittel());
        }
    }
}

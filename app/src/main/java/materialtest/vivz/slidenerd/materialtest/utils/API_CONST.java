package materialtest.vivz.slidenerd.materialtest.utils;

import android.util.Log;

public class API_CONST {
    public static String LOGIN_URL = "http://www.olavbg.com/android.php";
    public static String SEARCH_EAN_URL = "http://www.olavbg.com/testing/android_search_ean.php";

    public static String LOGIN_TAG = "login";
    public static String REGISTER_TAG = "register";
    public static String FORGOT_PASSWORD_TAG = "forgotPassword";
    public static String GET_MOVIES = "getMovies";
    public static String ADD_MOVIE_TAG = "add_movie";
    public static String ADD_MOVIE_WITH_DETAILS_TAG = "add_movie_with_details";
    public static String DELETE_MOVIE_TAG = "delete_movie";
    public static String SEARCH_EAN_TAG = "search_ean";

    public static String OmdbAPISearchURL(final String title) {
        final String formattedTitle = title.replace(" ", "%20");
        Log.d("Search URL", "http://www.omdbapi.com/?s=" + formattedTitle + "&y=&plot=short&r=json");
        return "http://www.omdbapi.com/?s=" + formattedTitle + "&y=&plot=short&r=json";
    }

    public static String OmdbAPIFindMovieByImdbIdURL(final String imdbId) {
        Log.d("Finding movie imdb id", "http://www.omdbapi.com/?i="+imdbId+"&plot=full&r=json");
        return "http://www.omdbapi.com/?i="+imdbId+"&plot=short&r=json";
    }
}

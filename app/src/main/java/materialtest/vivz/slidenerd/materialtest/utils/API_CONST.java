package materialtest.vivz.slidenerd.materialtest.utils;

import android.util.Log;

public class API_CONST {
    public static String LOGIN_URL = "http://www.olavbg.com/android.php";
    public static String LOGIN_TAG = "login";
    public static String REGISTER_TAG = "register";
    public static String FORGOT_PASSWORD_TAG = "forgotPassword";
    public static String GET_MOVIES = "getMovies";
    public static String ADD_MOVIE_TAG = "add_movie";
    public static String DELETE_MOVIE_TAG = "delete_movie";

    public static String OmdbAPISearch(final String title) {
        final String formattedTitle = title.replace(" ", "%20");
        Log.d("Search URL", "http://www.omdbapi.com/?s=" + formattedTitle + "&y=&plot=short&r=json");
        return "http://www.omdbapi.com/?s=" + formattedTitle + "&y=&plot=short&r=json";
    }
}

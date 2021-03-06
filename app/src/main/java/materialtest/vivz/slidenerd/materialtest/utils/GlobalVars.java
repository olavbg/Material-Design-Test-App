package materialtest.vivz.slidenerd.materialtest.utils;

import com.google.gson.Gson;

import materialtest.vivz.slidenerd.materialtest.Bruker;

public class GlobalVars {
    public static Bruker loggedInUser = null;
    public static final Gson gson = new Gson();

    public static final String PREF_FILE_NAME = "sharedPrefsFile";
    public static final String PREF_KEY_LOGGED_IN_USER = "loggedInUser";
    public static final String PREF_KEY_MOVIE_CACHE = "movieCache";
    public static final String PREF_KEY_BORROWED_MOVIE_CACHE = "borrowedMovieCache";
    public static final String PREF_KEY_LENT_MOVIE_CACHE = "lentMovieCache";
}

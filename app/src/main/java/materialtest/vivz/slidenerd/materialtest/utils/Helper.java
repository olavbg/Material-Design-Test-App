package materialtest.vivz.slidenerd.materialtest.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

import de.greenrobot.event.EventBus;
import materialtest.vivz.slidenerd.materialtest.Bruker;
import materialtest.vivz.slidenerd.materialtest.Events.AddMovieEvent;
import materialtest.vivz.slidenerd.materialtest.Events.MovieDeletedEvent;
import materialtest.vivz.slidenerd.materialtest.JsonPOSTResponse;
import materialtest.vivz.slidenerd.materialtest.LoginActivity;
import materialtest.vivz.slidenerd.materialtest.Movie;
import materialtest.vivz.slidenerd.materialtest.addmovie.EANSearchedMovie;
import materialtest.vivz.slidenerd.materialtest.addmovie.SearchedMovie;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

import static materialtest.vivz.slidenerd.materialtest.utils.GlobalVars.loggedInUser;

public class Helper {
    static Context context;
    static ProgressDialog pDialog;
    static List<Future<?>> futures = new ArrayList<>();

    public static void init(final Context context) {
        Helper.context = context;
        pDialog = new ProgressDialog(context);
    }

    public static void showToast(final String text) {
        checkContext();
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showToast(final String text, final int length) {
        checkContext();
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, length).show();
            }
        });
    }

    public static void showProgressDialog(final String text) {
        checkContext();
        pDialog.setMessage(text);
        pDialog.show();
    }

    public static void hideProgressDialog() {
        checkContext();
        pDialog.hide();
    }

    public static void dismissProgressDialog() {
        checkContext();
        pDialog.dismiss();
    }

    public static void saveToPreferences(final String preferenceName, final String preferenceValue) {
        checkContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVars.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(final String preferenceName, final String defaultValue) {
        checkContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVars.PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public static boolean isConnected() {
        checkContext();
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private static void checkContext() {
        if (context == null) {
            throw new NullPointerException("Method Helper.init(Context) must be called before this method can be called.");
        }
    }

    public static void logOut(final Activity activity) {
        saveToPreferences(GlobalVars.PREF_KEY_LOGGED_IN_USER, "");
        MovieList.clearAll();
        loggedInUser = null;
        dismissProgressDialog();
        activity.startActivity(new Intent(context, LoginActivity.class));
        activity.finish();
    }

    public static void addMovie(final Movie movie, final CheckBox checkBox) {
        Ion.with(context).load(API_CONST.LOGIN_URL)
                .setBodyParameter("tag", API_CONST.ADD_MOVIE_WITH_DETAILS_TAG)
                .setBodyParameter("movie", GlobalVars.gson.toJson(movie))
                .as(new TypeToken<Movie>() {
                })
                .setCallback(new FutureCallback<Movie>() {
                    @Override
                    public void onCompleted(Exception e, Movie addedMovie) {
                        if (e != null || !addedMovie.getErr_msg().isEmpty()) {
                            showToast("Oops! Something went wrong when connecting to the cloud!");
                            uncheckCheckbox(checkBox);
                        } else {
                            EventBus.getDefault().post(new AddMovieEvent(addedMovie));
                        }
                        hideProgressDialog();
                    }
                });
    }

    public static void validateFormat(final Movie movie) {
        for (String format : Types.formats()) {
            if (movie.getFormat().equalsIgnoreCase(format)){
                return;
            }
        }
        movie.setFormat(Types.Format.BLU_RAY);
    }

    private static void validateFormat(EANSearchedMovie eanSearchedMovie) {
        for (String format : Types.formats()) {
            if (eanSearchedMovie.getFormat().equalsIgnoreCase(format)){
                return;
            }
        }
        eanSearchedMovie.setFormat(Types.Format.BLU_RAY);
    }

    public static void getMovieDetailsFromImdbId(final String imdbId, final Callback<SearchedMovie> callback) {
        Ion.with(context).load(API_CONST.OmdbAPIFindMovieByImdbIdURL(imdbId))
                .as(new TypeToken<SearchedMovie>() {
                })
                .setCallback(new FutureCallback<SearchedMovie>() {
                    @Override
                    public void onCompleted(Exception e, SearchedMovie searchedMovie) {
                        if (e != null || searchedMovie.getError() != null) {
                            showToast(searchedMovie.getError());
                        } else {
                            callback.call(searchedMovie);
                        }
                        hideProgressDialog();
                    }
                });
    }

    public static void getMovies(final Callback<List<Movie>> callback) {
        Ion.with(context).load(API_CONST.LOGIN_URL)
                .setBodyParameter("tag", API_CONST.GET_MOVIES)
                .setBodyParameter("userID", String.valueOf(GlobalVars.loggedInUser.getBrukerID()))
                .as(new TypeToken<List<Movie>>() {
                })
                .setCallback(new FutureCallback<List<Movie>>() {
                    @Override
                    public void onCompleted(Exception e, List<Movie> movies) {
                        if (e == null) {
                            callback.call(movies);
                        }
                    }
                });
    }

    public static void searchEANAndAddMovie(final String contents, final ZBarScannerView mScannerView) {
        Ion.with(context).load(API_CONST.SEARCH_EAN_URL)
                .setBodyParameter("eanNr", contents)
                .as(new TypeToken<EANSearchedMovie>() {
                })
                .setCallback(new FutureCallback<EANSearchedMovie>() {
                    @Override
                    public void onCompleted(Exception e, final EANSearchedMovie searchedMovie) {
                        if (e != null || !searchedMovie.getErr_msg().isEmpty()) {
                            showToast("Oops! Something went wrong when connecting to the cloud!");
                            mScannerView.startCamera();
                        } else if (MovieList.getMovieByIdent(searchedMovie.getTitle(), searchedMovie.getFormat()) == null) {
                            validateFormat(searchedMovie);
                            final String format = searchedMovie.getFormat();
                            Helper.searchByTitle(searchedMovie.getTitle(), searchedMovie.getFormat(), new Callback<ArrayList<SearchedMovie>>() {
                                @Override
                                public void call(ArrayList<SearchedMovie> searchedMovies) {
                                    if (searchedMovies.size() > 0 && MovieList.getMovieByIdent(searchedMovies.iterator().next().getTitle(), format) == null) {
                                        Helper.getMovieDetailsFromImdbId(searchedMovies.iterator().next().getImdbID(), new Callback<SearchedMovie>() {
                                            @Override
                                            public void call(SearchedMovie searchedMovie) {
                                                searchedMovie.setFormat(format);
                                                Helper.addMovie(searchedMovie.convertFromSearch(), null);
                                                mScannerView.startCamera();
                                            }
                                        });
                                    } else if (MovieList.getMovieByIdent(searchedMovies.iterator().next().getTitle(), format) != null) {
                                        showToast("You already got " + searchedMovies.iterator().next().getTitle() + " on " + format);
                                        mScannerView.startCamera();
                                    }
                                }
                            });
                        } else if (MovieList.getMovieByIdent(searchedMovie.getTitle(), searchedMovie.getFormat()) != null) {
                            showToast("You already got " + searchedMovie.getTitle() + " on " + searchedMovie.getFormat());
                            mScannerView.startCamera();
                        }
                        hideProgressDialog();
                    }
                });
    }

    public static void deleteMovie(final Movie movie, final CheckBox checkBox) {
        Ion.with(context).load(API_CONST.LOGIN_URL)
                .setBodyParameter("tag", API_CONST.DELETE_MOVIE_TAG)
                .setBodyParameter("movieID", String.valueOf(movie.getFilmID()))
                .as(new TypeToken<JsonPOSTResponse>() {
                })
                .setCallback(new FutureCallback<JsonPOSTResponse>() {
                    @Override
                    public void onCompleted(Exception e, JsonPOSTResponse response) {
                        if (e != null || !TextUtils.isEmpty(response.getErr_msg())) {
                            showToast("Oops! Something went wrong when deleting movie!");
                            hideProgressDialog();
                            checkBox.setChecked(true);
                        } else {
                            MovieList.removeMovie(movie);
                            MovieList.cacheMoviesLocally();
                            EventBus.getDefault().post(new MovieDeletedEvent(movie));
                        }
                    }
                });
    }

    public static void searchByTitle(final String title, final String format, final Callback<ArrayList<SearchedMovie>> callback) {
        abortFutures();
        final Future<JsonObject> future = Ion.with(context).load(API_CONST.OmdbAPISearchURL(title))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (error(e, result)) {
                            showToast("Oops! Something went wrong when connecting to the cloud!");
                            hideProgressDialog();
                        } else if (result != null && result.has("Search")) {
                            final JsonArray movies = result.get("Search").getAsJsonArray();
                            final ArrayList<SearchedMovie> searchedMovies = new ArrayList<>();
                            for (int i = 0; i < movies.size(); i++) {
                                Log.d("Found in search", movies.get(i).toString());
                                final SearchedMovie searchedMovie = GlobalVars.gson.fromJson(movies.get(i).toString(), SearchedMovie.class);
                                searchedMovie.setFormat(format);
                                searchedMovies.add(searchedMovie);
                            }
                            callback.call(searchedMovies);
                        }
                    }
                });
        futures.add(future);
    }

    private static boolean error(Exception e, JsonObject result) {
        return !isCancelled(e) && result == null;
    }

    private static boolean isCancelled(final Exception e) {
        return e != null && e instanceof CancellationException;
    }

    public static void logIn(final String username, final String password, final Callback<Bruker> callback) {
        showProgressDialog("Signing in..");
        Ion.with(context).load(API_CONST.LOGIN_URL)
                .setBodyParameter("tag", API_CONST.LOGIN_TAG)
                .setBodyParameter("username", username)
                .setBodyParameter("password", password)
                .setBodyParameter("hash", "true")
                .as(new TypeToken<Bruker>() {
                })
                .setCallback(new FutureCallback<Bruker>() {
                    @Override
                    public void onCompleted(Exception e, Bruker bruker) {
                        if (e != null || !TextUtils.isEmpty(bruker.getErr_msg())) {
                            showToast("Oops! Something went wrong when connecting to the cloud!");
                            hideProgressDialog();
                        } else {
                            callback.call(bruker);
                        }
                    }
                });
    }

    public static void registerNewUser(final String username, final String email, final String password, final Callback<Bruker> callback) {
        Helper.showProgressDialog("Registering..");
        Ion.with(context).load(API_CONST.LOGIN_URL)
                .setBodyParameter("tag", API_CONST.REGISTER_TAG)
                .setBodyParameter("username", username)
                .setBodyParameter("password", password)
                .setBodyParameter("email", email)
                .as(new TypeToken<Bruker>() {
                })
                .setCallback(new FutureCallback<Bruker>() {
                    @Override
                    public void onCompleted(Exception e, Bruker bruker) {
                        if (e != null || !TextUtils.isEmpty(bruker.getErr_msg())) {
                            showToast("Oops! Something went wrong when connecting to the cloud!");
                            hideProgressDialog();
                        } else {
                            callback.call(bruker);
                        }
                    }
                });
    }

    //Kun brukt i forbindelse med debugging
    @SuppressWarnings("unused")
    public static void getResponse(String title) {
        Ion.with(context).load(API_CONST.OmdbAPISearchURL(title))
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
//                             print the response code, ie, 200
                        System.out.println(result.getHeaders().code());
//                             print the String that was downloaded
                        System.out.println(result.getResult());
                    }
                });
    }

    public static void uncheckCheckbox(final CheckBox checkBox) {
        if (checkBox != null) {
            checkBox.setChecked(false);
        }
    }

    public static void abortFutures() {
        for (Future<?> future : new ArrayList<>(futures)) {
            if (future.isDone()) {
                futures.remove(future);
            } else {
                future.cancel();
                futures.remove(future);
            }
        }
    }
}

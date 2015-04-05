package materialtest.vivz.slidenerd.materialtest.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.CheckBox;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import materialtest.vivz.slidenerd.materialtest.JsonPOSTResponse;
import materialtest.vivz.slidenerd.materialtest.Movie;
import materialtest.vivz.slidenerd.materialtest.MovieCardAdapter;
import materialtest.vivz.slidenerd.materialtest.addmovie.MovieSearchCardAdapter;
import materialtest.vivz.slidenerd.materialtest.addmovie.SimpleSearchedMovie;

import static materialtest.vivz.slidenerd.materialtest.utils.GlobalVars.gson;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.hideProgressDialog;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.showToast;
import static materialtest.vivz.slidenerd.materialtest.utils.MovieList.addMovie;
import static materialtest.vivz.slidenerd.materialtest.utils.MovieList.cacheMoviesLocally;
import static materialtest.vivz.slidenerd.materialtest.utils.MovieList.getAllMovies;
import static materialtest.vivz.slidenerd.materialtest.utils.MovieList.getMovieById;
import static materialtest.vivz.slidenerd.materialtest.utils.MovieList.removeMovies;
import static materialtest.vivz.slidenerd.materialtest.utils.MovieList.setSelectedList;
import static materialtest.vivz.slidenerd.materialtest.utils.MovieList.sortMoviesByTitle;
import static materialtest.vivz.slidenerd.materialtest.utils.MovieList.updateMovie;

public class VolleyRequest {

    public static StringRequest getNewPasswordRequest(final String email) {
        return new StringRequest(Request.Method.POST,
                API_CONST.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("NEW PASSWORD RESPONSE", response);
                        final JsonPOSTResponse jsonPOSTResponse = gson.fromJson(response, JsonPOSTResponse.class);
                        if (jsonPOSTResponse.isSuccess()) {
                            showToast("Your password has been reset and sent to '" + email + "'");
                        } else {
                            showToast(jsonPOSTResponse.getErr_msg());
                        }
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("NEW PASSWORD ERROR", "Error: " + error.getMessage());
                showToast("Ops! Something went wrong when connecting to the cloud! Please try again later..");
                // hide the progress dialog
                hideProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tag", API_CONST.FORGOT_PASSWORD_TAG);
                params.put("email", email);
                return params;
            }
        };
    }

    public static StringRequest getAddMovieRequest(final Movie movie, final CheckBox addMovie) {
        return new StringRequest(Request.Method.POST,
                API_CONST.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ADD MOVIE RESPONSE", response);
                        try {
                            final Movie addedMovie = gson.fromJson(response, Movie.class);
                            if (addedMovie.getErr_msg() != null && addedMovie.getErr_msg().isEmpty()) {
                                MovieList.addMovie(addedMovie);
                                MovieList.cacheMoviesLocally();
                                showToast(movie.getTittel() + " on " + movie.getFormat() + " added to the cloud");
                            } else {
                                showToast(addedMovie.getErr_msg());
                            }
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } finally {
                            hideProgressDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                addMovie.setChecked(false);
                VolleyLog.d("ADD MOVIE ERROR", "Error: " + error.getMessage());
                showToast("Ops! Something went wrong when connecting to the cloud! Please try again later..");
                hideProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tag", API_CONST.ADD_MOVIE_TAG);
                params.put("title", movie.getTittel());
                params.put("format", movie.getFormat());
                params.put("brukerID", String.valueOf(movie.getBrukerID()));
                params.put("addDetails", String.valueOf(Settings.addDetails));
//                params.put("movie", GlobalVars.gson.toJson(movie));
                return params;
            }
        };
    }

    public static StringRequest getDeleteMovieRequest(final Movie movie, final CheckBox addMovie) {
        return new StringRequest(Request.Method.POST,
                API_CONST.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("REMOVE MOVIE RESPONSE", response);
                        final JsonPOSTResponse jsonPOSTResponse = gson.fromJson(response, JsonPOSTResponse.class);
                        if (jsonPOSTResponse.isSuccess()) {
                            MovieList.removeMovie(movie);
                            cacheMoviesLocally();
                            showToast(movie.getTittel() + " on " + movie.getFormat() + " deleted from the cloud");
                        } else {
                            showToast(jsonPOSTResponse.getErr_msg());
                        }
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ADD MOVIE ERROR", "Error: " + error.getMessage());
                showToast("Ops! Something went wrong when connecting to the cloud! Please try again later..");
                addMovie.setChecked(true);
                hideProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tag", API_CONST.DELETE_MOVIE_TAG);
                params.put("movieID", String.valueOf(movie.getFilmID()));
                return params;
            }
        };
    }

    public static StringRequest getMoviesRequest(final int brukerID, final MovieCardAdapter adapter, final SwipeRefreshLayout swipeRefreshLayout, final Activity activity) {
        return new StringRequest(Request.Method.POST,
                API_CONST.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d("GET MOVIES RESPONSE", response);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONArray jsonArray = new JSONArray(response);
                                    final ArrayList<Movie> cachedMovies = getAllMovies();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        final Movie movie = gson.fromJson(jsonArray.getString(i), Movie.class);
                                        final Movie existingMovie = getMovieById(movie.getFilmID());
                                        if (existingMovie != null && !existingMovie.equals(movie)) {
                                            updateMovie(movie, adapter, activity);
                                        } else if (existingMovie == null) {
                                            final int index = addMovie(movie);
                                            if (index >= 0) {
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        adapter.notifyItemInserted(index);
                                                    }
                                                });
                                            }
                                        }
                                        if (cachedMovies.contains(movie)) {
                                            cachedMovies.remove(movie);
                                        }
                                    }
                                    if (!cachedMovies.isEmpty()) {
                                        removeMovies(cachedMovies);
                                    }
                                    sortMoviesByTitle();
                                    setSelectedList(MovieList.selectedListType, adapter, activity);
                                    showToast("Movies loaded from the cloud: " + getAllMovies().size());
                                    cacheMoviesLocally();
                                } catch (JSONException e) {
                                    showToast("Ops! Something went wrong when reading movies from the cloud! Please try again later..");
                                    e.printStackTrace();
                                } finally {
                                    stopLoading(swipeRefreshLayout, activity);
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("GET MOVIES ERROR", "Error: " + error.getMessage());
                showToast("Ops! Something went wrong when connecting to the cloud! Please try again later..");
                stopLoading(swipeRefreshLayout, activity);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tag", API_CONST.GET_MOVIES);
                params.put("userID", String.valueOf(brukerID));
                return params;
            }
        };
    }

    private static void stopLoading(final SwipeRefreshLayout swipeRefreshLayout, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // hide the progress dialog
                hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public static StringRequest getMovieSearchRequest(final String title, final String format, final ArrayList<SimpleSearchedMovie> suggestions, final MovieSearchCardAdapter adapter, final FragmentActivity activity) {
        return new StringRequest(Request.Method.POST,
                API_CONST.OmdbAPISearch(title),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("GET SEARCH RESPONSE", response);
                        try {
                            final JSONObject search = new JSONObject(response);
                            if (search.has("Search")) {
                                JSONArray movies = (JSONArray) search.get("Search");
                                for (int i = 0; i < movies.length(); i++) {
                                    Log.d("Found in search", movies.get(i).toString());
                                    final SimpleSearchedMovie searchedMovie = GlobalVars.gson.fromJson(movies.get(i).toString(), SimpleSearchedMovie.class);
                                    searchedMovie.setFormat(format);
                                    suggestions.add(searchedMovie);
                                    final int finalI = i;
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyItemInserted(finalI);
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            showToast("Ops! Something went wrong when reading movies from the cloud! Please try again later..");
                            e.printStackTrace();
                        }
                    }
                }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("GET SEARCH MOVIES ERROR", "Error: " + error.getMessage());
                showToast("Ops! Something went wrong when connecting to the cloud! Please try again later..");
            }
        }

        );
    }
}

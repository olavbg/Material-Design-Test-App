package materialtest.vivz.slidenerd.materialtest.utils;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import materialtest.vivz.slidenerd.materialtest.JsonPOSTResponse;
import materialtest.vivz.slidenerd.materialtest.Movie;
import materialtest.vivz.slidenerd.materialtest.MovieCardAdapter;

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

    public static StringRequest getMoviesRequest(final int brukerID, final MovieCardAdapter adapter, final SwipeRefreshLayout swipeRefreshLayout) {
        return new StringRequest(Request.Method.POST,
                API_CONST.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("GET MOVIES RESPONSE", response);
                        try {
                            final JSONArray jsonArray = new JSONArray(response);
                            final ArrayList<Movie> cachedMovies = getAllMovies();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                final Movie movie = gson.fromJson(jsonArray.getString(i), Movie.class);
                                final Movie existingMovie = getMovieById(movie.getFilmID());
                                if (existingMovie != null && !existingMovie.equals(movie)) {
                                    updateMovie(movie, adapter);
                                } else if (existingMovie == null) {
                                    final int index = addMovie(movie);
                                    if(index >=0){
                                        adapter.notifyItemInserted(index);
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
                            setSelectedList(Types.ChosenListType.Your, adapter);
                            showToast("Movies loaded from the cloud: " + getAllMovies().size());
                            cacheMoviesLocally();
                        } catch (JSONException e) {
                            hideProgressDialog();
                            swipeRefreshLayout.setRefreshing(false);
                            showToast("Ops! Something went wrong when reading movies from the cloud! Please try again later..");
                            e.printStackTrace();
                        }
                        hideProgressDialog();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("GET MOVIES ERROR", "Error: " + error.getMessage());
                showToast("Ops! Something went wrong when connecting to the cloud! Please try again later..");
                // hide the progress dialog
                hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
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
}

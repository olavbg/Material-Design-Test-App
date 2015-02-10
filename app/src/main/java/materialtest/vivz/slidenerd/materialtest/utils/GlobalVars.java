package materialtest.vivz.slidenerd.materialtest.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import materialtest.vivz.slidenerd.materialtest.Bruker;

public class GlobalVars {
    public static Bruker loggedInUser = null;
    public static RequestQueue requestQueue = null;
    public static final Gson gson = new Gson();
    
    public static final String PREF_FILE_NAME = "sharedPrefsFile";
    public static final String PREF_KEY_LOGGED_IN_USER = "loggedInUser";
    

    public static void init(final Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }
}

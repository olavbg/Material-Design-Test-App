package materialtest.vivz.slidenerd.materialtest.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import materialtest.vivz.slidenerd.materialtest.LoginActivity;

import static materialtest.vivz.slidenerd.materialtest.utils.GlobalVars.loggedInUser;

public class Helper {
    static Context context;
    static ProgressDialog pDialog;

    public static void init(final Context context) {
        Helper.context = context;
        pDialog = new ProgressDialog(context);
    }

    public static void showToast(final String text) {
        checkContext();
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showToast(final String text, final int length) {
        checkContext();
        ((Activity)context).runOnUiThread(new Runnable() {
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
}

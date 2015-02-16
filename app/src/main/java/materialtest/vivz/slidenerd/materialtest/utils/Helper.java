package materialtest.vivz.slidenerd.materialtest.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Helper {
    static Context context;
    static ProgressDialog pDialog;

    public static void init(final Context context){
        Helper.context = context;
        pDialog = new ProgressDialog(context);
    } 

    public static void showToast(String text) {
        checkContext();
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    
    public static void showProgressDialog(final String text){
        checkContext();
        pDialog.setMessage(text);
        pDialog.show();
    }
    
    public static void hideProgressDialog(){
        checkContext();
        pDialog.hide();
    }
    
    public static void dismissProgressDialog(){
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
        if (context == null){
            throw new NullPointerException("Method Helper.init(Context) must be called before this method can be called.");
        }
    }
}

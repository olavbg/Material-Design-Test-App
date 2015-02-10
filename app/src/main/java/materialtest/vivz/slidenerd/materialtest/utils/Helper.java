package materialtest.vivz.slidenerd.materialtest.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Helper {
    static Context context;
    static ProgressDialog pDialog;

    public static void init(final Context context){
        Helper.context = context;
        pDialog = new ProgressDialog(context);
    } 

    public static void showToast(String text) {
        if (context == null){
            throw new NullPointerException("Method Helper.init(Context) must be called befor this method can be called.");
        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    
    public static void showProgressDialog(final String text){
        pDialog.setMessage(text);
        pDialog.show();
    }
    
    public static void hideProgressDialog(){
        pDialog.hide();
    }

    public static void saveToPreferences(final String preferenceName, final String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVars.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(final String preferenceName, final String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVars.PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }
}

package materialtest.vivz.slidenerd.materialtest.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    static Context context;

    public static void init(Context context){
        ToastHelper.context = context;
    } 

    public static void showToast(String text) {
        if (context == null){
            throw new NullPointerException("Method ToastHelper.init(Context) must be called befor this method can be called.");
        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}

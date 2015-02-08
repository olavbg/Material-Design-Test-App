package materialtest.vivz.slidenerd.materialtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import materialtest.vivz.slidenerd.materialtest.utils.API_CONST;
import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;
import materialtest.vivz.slidenerd.materialtest.utils.ToastHelper;

import static materialtest.vivz.slidenerd.materialtest.utils.ToastHelper.showToast;


public class LoginActivity extends ActionBarActivity {
    EditText txtUsername;
    EditText txtPassword;
    RequestQueue requestQueue;
    ProgressDialog pDialog;
    final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        
        requestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        ToastHelper.init(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void login(View view) {
        final String username = txtUsername.getText().toString();
        final String password = txtPassword.getText().toString();

        Log.d("Username", username);
        Log.d("Password", password);

        if (TextUtils.isEmpty(username)){
            showToast("Please enter a username");
            txtUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            showToast("Please enter a password");
            txtPassword.requestFocus();
            return;
        }

        pDialog.setMessage("Signing in...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                API_CONST.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LOGIN RESPONSE", response);
                        final Bruker bruker = gson.fromJson(response, Bruker.class);
                        if (TextUtils.isEmpty(bruker.getErr_msg())){
                            GlobalVars.loggedInUser = bruker;
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }else{
                            showToast(bruker.getErr_msg());
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("LOGIN ERROR", "Error: " + error.getMessage());
                showToast("Ops! Something went wrong when connecting to the cloud! Please try again later..");
                // hide the progress dialog
                pDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tag", API_CONST.LOGIN_TAG);
                params.put("username", username);
                params.put("password", password);
                params.put("hash", "true");
                return params;
            }
        };

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjReq);
    }
}

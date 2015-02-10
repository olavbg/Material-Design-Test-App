package materialtest.vivz.slidenerd.materialtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import materialtest.vivz.slidenerd.materialtest.utils.API_CONST;
import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;
import materialtest.vivz.slidenerd.materialtest.utils.Helper;

import static android.text.TextUtils.isEmpty;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.hideProgressDialog;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.saveToPreferences;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.showToast;


public class RegisterActivity extends ActionBarActivity {
    EditText txtUsername;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtRepeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtRepeatPassword = (EditText) findViewById(R.id.txtRepeatPassword);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void goBack(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void registerNewAccount(View view) {
        final String username = txtUsername.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();
        final String repeatPassword = txtRepeatPassword.getText().toString().trim();

        if (isEmpty(username)) {
            showToast("Please enter a username");
            txtUsername.requestFocus();
            return;
        }
        if (isEmpty(email)) {
            showToast("Please enter a email adress");
            txtEmail.requestFocus();
            return;
        }
        if (isEmpty(password)) {
            showToast("Please enter a password");
            txtPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            showToast("Your password must contain at least 6 characters..");
            txtPassword.requestFocus();
            return;
        }
        if (isEmpty(repeatPassword)) {
            showToast("Please repeat your password");
            txtRepeatPassword.requestFocus();
            return;
        }
        if (!password.equals(repeatPassword)){
            showToast("Your password and repeated passwords do not match..");
            return;
        }

        // Adding request to request queue
        Helper.showProgressDialog("Registering..");
        GlobalVars.requestQueue.add(getRegisterRequest(username, email, password));
    }

    public StringRequest getRegisterRequest(final String username, final String email, final String password) {
        return new StringRequest(Request.Method.POST,
                API_CONST.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("REGISTRATION RESPONSE", response);
                        final Bruker bruker = GlobalVars.gson.fromJson(response, Bruker.class);
                        if (isEmpty(bruker.getErr_msg())) {
                            loginUser(bruker);
                        } else {
                            showToast(bruker.getErr_msg());
                        }
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("REGISTRATION ERROR", "Error: " + error.getMessage());
                showToast("Ops! Something went wrong when connecting to the cloud! Please try again later..");
                // hide the progress dialog
                hideProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tag", API_CONST.REGISTER_TAG);
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                return params;
            }
        };
    }

    private void loginUser(final Bruker bruker) {
        GlobalVars.loggedInUser = bruker;
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        saveToPreferences(GlobalVars.PREF_KEY_LOGGED_IN_USER, GlobalVars.gson.toJson(bruker));
        finish();
    }
}

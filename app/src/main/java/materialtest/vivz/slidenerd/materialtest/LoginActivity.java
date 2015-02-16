package materialtest.vivz.slidenerd.materialtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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
import materialtest.vivz.slidenerd.materialtest.utils.VolleyRequest;

import static android.text.TextUtils.isEmpty;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.hideProgressDialog;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.readFromPreferences;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.saveToPreferences;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.showToast;


public class LoginActivity extends ActionBarActivity {
    EditText txtUsername;
    EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        Helper.init(this);
        GlobalVars.init(this);

        final Bruker loggedInUser = getLoggedInUser();
        if (loggedInUser != null) {
            loginUser(loggedInUser);
        }

        txtUsername.setOnEditorActionListener(getOnNextEvent());
        txtPassword.setOnEditorActionListener(getOnGoEvent());
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

    private Bruker getLoggedInUser() {
        final String loggedInUserJsonString = readFromPreferences(GlobalVars.PREF_KEY_LOGGED_IN_USER, "");
        if (!isEmpty(loggedInUserJsonString)) {
            final Bruker loggedInUser = GlobalVars.gson.fromJson(loggedInUserJsonString, Bruker.class);
            if (loggedInUser != null && loggedInUser.getBrukerID() != -1) {
                return loggedInUser;
            }
        }
        return null;
    }

    public void login(View view) {
        login();
    }

    private void login() {
        final String username = txtUsername.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();

        if (isEmpty(username)) {
            showToast("Please enter a username");
            txtUsername.requestFocus();
            return;
        }
        if (isEmpty(password)) {
            showToast("Please enter a password");
            txtPassword.requestFocus();
            return;
        }

        // Adding request to request queue
        Helper.showProgressDialog("Signing in..");
        GlobalVars.requestQueue.add(getLoginRequest(username, password));
    }

    private StringRequest getLoginRequest(final String username, final String password) {
        return new StringRequest(Request.Method.POST, API_CONST.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LOGIN RESPONSE", response);
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
                VolleyLog.d("LOGIN ERROR", "Error: " + error.getMessage());
                showToast("Ops! Something went wrong when connecting to the cloud! Please try again later..");
                // hide the progress dialog
                hideProgressDialog();
            }
        }) {
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
    }

    private void loginUser(final Bruker bruker) {
        GlobalVars.loggedInUser = bruker;
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        saveToPreferences(GlobalVars.PREF_KEY_LOGGED_IN_USER, GlobalVars.gson.toJson(bruker));
        Helper.dismissProgressDialog();
        finish();
    }

    public void forgotPassword(View view) {
        final String email = txtUsername.getText().toString().trim();
        if (isEmpty(email)) {
            showToast("Please enter your email in the email/username field");
            return;
        }

        // Adding request to request queue
        Helper.showProgressDialog("Requesting new password..");
        GlobalVars.requestQueue.add(VolleyRequest.getNewPasswordRequest(email));
    }

    public void registerNewAccount(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public TextView.OnEditorActionListener getOnGoEvent() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    login();
                    handled = true;
                }
                return handled;
            }
        };
    }

    public TextView.OnEditorActionListener getOnNextEvent() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    txtPassword.requestFocus();
                    handled = true;
                }
                return handled;
            }
        };
    }
}

package materialtest.vivz.slidenerd.materialtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import materialtest.vivz.slidenerd.materialtest.utils.API_CONST;
import materialtest.vivz.slidenerd.materialtest.utils.Callback;
import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;
import materialtest.vivz.slidenerd.materialtest.utils.Helper;

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
        Helper.logIn(username, password, new Callback<Bruker>() {
            @Override
            public void call(Bruker bruker) {
                loginUser(bruker);
            }
        });
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
        Ion.with(this).load(API_CONST.LOGIN_URL)
                .setBodyParameter("tag", API_CONST.FORGOT_PASSWORD_TAG)
                .setBodyParameter("email", email)
                .as(new TypeToken<JsonPOSTResponse>() {
                })
                .setCallback(new FutureCallback<JsonPOSTResponse>() {
                    @Override
                    public void onCompleted(Exception e, JsonPOSTResponse response) {
                        if (e != null || !response.getErr_msg().isEmpty()) {
                            showToast("Oops! Something went wrong when connecting to the cloud!");
                        } else {
                            showToast("New password sent to " + email);
                        }
                        hideProgressDialog();
                    }
                });
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

package materialtest.vivz.slidenerd.materialtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import materialtest.vivz.slidenerd.materialtest.utils.Callback;
import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;
import materialtest.vivz.slidenerd.materialtest.utils.Helper;

import static android.text.TextUtils.isEmpty;
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
            showToast(getString(R.string.toast_enterUsername));
            txtUsername.requestFocus();
            return;
        }
        if (isEmpty(email)) {
            showToast(getString(R.string.toast_enterEmail));
            txtEmail.requestFocus();
            return;
        }
        if (isEmpty(password)) {
            showToast(getString(R.string.toast_enterPassword));
            txtPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            showToast(getString(R.string.error_passwordAtLeastSixChars));
            txtPassword.requestFocus();
            return;
        }
        if (isEmpty(repeatPassword)) {
            showToast(getString(R.string.toast_repeatPassword));
            txtRepeatPassword.requestFocus();
            return;
        }
        if (!password.equals(repeatPassword)) {
            showToast(getString(R.string.toast_repeatedPasswordNotMatch));
            return;
        }

        // Adding request to request queue
        Helper.registerNewUser(username, email, password, new Callback<Bruker>() {
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
        finish();
    }
}

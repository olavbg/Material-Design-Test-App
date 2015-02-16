package materialtest.vivz.slidenerd.materialtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;
import materialtest.vivz.slidenerd.materialtest.utils.Helper;
import materialtest.vivz.slidenerd.materialtest.utils.MovieList;

import static materialtest.vivz.slidenerd.materialtest.utils.Helper.isConnected;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.saveToPreferences;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.showToast;
import static materialtest.vivz.slidenerd.materialtest.utils.VolleyRequest.getMoviesRequest;


public class MainActivity extends ActionBarActivity {
    private MovieCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initHelperClasses();
        setUpToolBarAndNavDrawer();
        setUpCardView();

        showToast("Logged in as " + GlobalVars.loggedInUser.getBrukernavn());
        loadMovies();
    }

    private void initHelperClasses() {
        Helper.init(this);
        GlobalVars.init(this);
    }

    private void setUpToolBarAndNavDrawer() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    private void setUpCardView() {
        final RecyclerView cardView = (RecyclerView) findViewById(R.id.cardView);
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cardView.setLayoutManager(llm);
        adapter = new MovieCardAdapter(MovieList.movies);
        cardView.setAdapter(adapter);
    }

    private void loadMovies() {
        MovieList.loadCachedMoviesIfAny();
        if (isConnected()) {
            GlobalVars.requestQueue.add(getMoviesRequest(GlobalVars.loggedInUser.getBrukerID(), adapter));
        }else if (!MovieList.getAllMovies().isEmpty()){
            showToast("No internet connection. Only showing locally stored movies..");
        }else{
            showToast("No internet connection..");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Toast.makeText(this, "Hey you just hit " + item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logOut) {
            saveToPreferences(GlobalVars.PREF_KEY_LOGGED_IN_USER, "");
            GlobalVars.loggedInUser = null;
            Helper.dismissProgressDialog();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

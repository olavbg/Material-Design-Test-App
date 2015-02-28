package materialtest.vivz.slidenerd.materialtest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;
import materialtest.vivz.slidenerd.materialtest.utils.Helper;
import materialtest.vivz.slidenerd.materialtest.utils.MovieList;

import static materialtest.vivz.slidenerd.materialtest.utils.GlobalVars.loggedInUser;
import static materialtest.vivz.slidenerd.materialtest.utils.GlobalVars.requestQueue;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.dismissProgressDialog;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.isConnected;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.saveToPreferences;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.showToast;
import static materialtest.vivz.slidenerd.materialtest.utils.VolleyRequest.getMoviesRequest;


public class MainActivity extends ActionBarActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MovieCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initHelperClasses();
        setUpToolBarAndNavDrawer();
        setUpCardView();

        if (savedInstanceState != null) {
            MovieList.loadCachedMoviesIfAny();
            return;
        }

        showToast("Logged in as " + loggedInUser.getBrukernavn());
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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        loadMovies();
                    }
                });
            }
        });

        final Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        recyclerView = (RecyclerView) findViewById(R.id.cardView);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(rotation == OrientationHelper.HORIZONTAL ? 1 : 2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new MovieCardAdapter(MovieList.movies);
        recyclerView.setAdapter(adapter);
        
        //TODO: Setup fastscroller når klar: https://android-arsenal.com/details/1/1582
//        final VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.fast_scroller);
//        fastScroller.setRecyclerView(recyclerView);
//        recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
    }

    private void loadMovies() {
        MovieList.loadCachedMoviesIfAny();
        if (isConnected()) {
            requestQueue.add(getMoviesRequest(loggedInUser.getBrukerID(), adapter, swipeRefreshLayout));
        } else if (!MovieList.getAllMovies().isEmpty()) {
            showToast("No internet connection. Only showing locally stored movies..");
            swipeRefreshLayout.setRefreshing(false);
        } else {
            showToast("No internet connection..");
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MovieList.cacheMoviesLocally();
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
            showToast("Not implemented yet..");
            return true;
        } else if (id == R.id.action_logOut) {
            saveToPreferences(GlobalVars.PREF_KEY_LOGGED_IN_USER, "");
            loggedInUser = null;
            dismissProgressDialog();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

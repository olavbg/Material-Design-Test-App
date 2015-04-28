package materialtest.vivz.slidenerd.materialtest;

import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.WindowManager;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.List;

import de.greenrobot.event.EventBus;
import materialtest.vivz.slidenerd.materialtest.Events.AddMovieEvent;
import materialtest.vivz.slidenerd.materialtest.Events.MovieDeletedEvent;
import materialtest.vivz.slidenerd.materialtest.Events.SetMovieListEvent;
import materialtest.vivz.slidenerd.materialtest.addmovie.AddMovieActivity;
import materialtest.vivz.slidenerd.materialtest.recyclerview.MovieCardAdapter;
import materialtest.vivz.slidenerd.materialtest.utils.Callback;
import materialtest.vivz.slidenerd.materialtest.utils.Helper;
import materialtest.vivz.slidenerd.materialtest.utils.MovieList;

import static materialtest.vivz.slidenerd.materialtest.utils.Helper.isConnected;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.logOut;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.showToast;


public class MainActivity extends ActionBarActivity {
    private NavigationDrawerFragment drawerFragment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MovieCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initHelperClasses();
        setUpToolBarAndNavDrawer();
        setUpCardView();
        setUpFAB();

        if (savedInstanceState != null) {
            MovieList.loadCachedMoviesIfAny();
            return;
        }
        loadMovies();
    }

    private void setUpFAB() {
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fabMenu);
        FloatingActionButton fabAddMovie = (FloatingActionButton) findViewById(R.id.fabAddMovie);
        FloatingActionButton fabScanMovie = (FloatingActionButton) findViewById(R.id.fabScanMovie);
        FloatingActionButton fabQuickScan = (FloatingActionButton) findViewById(R.id.fabQuickscan);

        fabAddMovie.setSize(FloatingActionButton.SIZE_MINI);
        fabScanMovie.setSize(FloatingActionButton.SIZE_MINI);
        fabQuickScan.setSize(FloatingActionButton.SIZE_MINI);

        fabAddMovie.setImageDrawable(new IconDrawable(this, Iconify.IconValue.fa_film).sizeDp(26));
        fabScanMovie.setImageDrawable(new IconDrawable(this, Iconify.IconValue.fa_camera).sizeDp(26));
        fabQuickScan.setImageDrawable(new IconDrawable(this, Iconify.IconValue.fa_barcode).sizeDp(26));

        final Intent intent = new Intent(getApplicationContext(), AddMovieActivity.class);
        fabAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("index", 0);
                startActivity(intent);
                fabMenu.collapse();
            }
        });
        fabScanMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("index", 1);
                startActivity(intent);
                fabMenu.collapse();
            }
        });
        fabQuickScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("index", 2);
                startActivity(intent);
                fabMenu.collapse();
            }
        });
    }

    private void initHelperClasses() {
        Helper.init(this);
        EventBus.getDefault().register(this);
    }

    private void setUpToolBarAndNavDrawer() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        adapter = new MovieCardAdapter(this);
        drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    private void setUpCardView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Helper.getMovies(new Callback<List<Movie>>() {
                    @Override
                    public void call(List<Movie> movies) {
                        swipeRefreshLayout.setRefreshing(false);
                        MovieList.addMovies(movies);
                        MovieList.refreshList();
                    }
                });
            }
        });

        final Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cardView);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(rotation == OrientationHelper.HORIZONTAL ? 1 : 2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);
//        recyclerView.setItemAnimator(new SlideInRightAnimator());
//        recyclerView.setAdapter(new AlphaInAnimationAdapter(adapter));

        //TODO: Setup fastscroller når klar: https://android-arsenal.com/details/1/1582
//        final VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.fast_scroller);
//        fastScroller.setRecyclerView(recyclerView);
//        recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
    }

    private void loadMovies() {
        MovieList.loadCachedMoviesIfAny();
        if (isConnected()) {
            Helper.getMovies(new Callback<List<Movie>>() {
                @Override
                public void call(List<Movie> movies) {
                    MovieList.clearAll();
                    MovieList.addMovies(movies);
                    MovieList.refreshList();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else if (!MovieList.getAllMovies().isEmpty()) {
            showToast("No internet connection. Only showing locally stored movies..");
            swipeRefreshLayout.setRefreshing(false);
        } else {
            showToast("No internet connection..");
            swipeRefreshLayout.setRefreshing(false);
        }
        drawerFragment.updateDrawer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Helper.init(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MovieList.cacheMoviesLocally();
    }

    //Events
    @SuppressWarnings("unused")
    public void onEventMainThread(AddMovieEvent event) {
        MovieList.add(event.getMovie());
        MovieList.sortMoviesByTitle();
        adapter.addMovie(MovieList.movies.indexOf(event.getMovie()), event.getMovie());
        showToast(event.getMovie().getTittel() + " on " + event.getMovie().getFormat() + " added to the cloud");
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(SetMovieListEvent event) {
        adapter.setData(event.getMovies());
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(MovieDeletedEvent event) {
        adapter.deleteMovie(event.getMovie());
        showToast(event.getMovie().getTittel() + " on " + event.getMovie().getFormat() + " deleted from the cloud");
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
            logOut(this);
        }

        return super.onOptionsItemSelected(item);
    }
}

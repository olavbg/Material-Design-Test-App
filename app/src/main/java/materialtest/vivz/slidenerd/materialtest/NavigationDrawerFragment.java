package materialtest.vivz.slidenerd.materialtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;
import materialtest.vivz.slidenerd.materialtest.utils.Helper;
import materialtest.vivz.slidenerd.materialtest.utils.MovieList;
import materialtest.vivz.slidenerd.materialtest.utils.Types;

import static com.joanzapata.android.iconify.Iconify.IconValue;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.logOut;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.readFromPreferences;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.saveToPreferences;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.showToast;
import static materialtest.vivz.slidenerd.materialtest.utils.MovieList.setSelectedList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private final List<NavDrawerItem> items = new ArrayList<>();
    private TextView navDrawerLoggedInAs;
    private ListView navDrawerList;
    private MovieCardAdapter movieCardAdapter;
    private DrawerLayout drawerLayout;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    public void updateDrawer() {
        items.clear();
        items.add(new NavDrawerItem(IconValue.fa_film.character(), "Movies", MovieList.getYourMovies().size()));
        items.add(new NavDrawerItem(IconValue.fa_mail_reply.character(), "Borrowed movies", MovieList.getBorrowedMovies().size()));
        items.add(new NavDrawerItem(IconValue.fa_mail_forward.character(), "Lent movies", MovieList.getLentMovies().size()));
        items.add(new NavDrawerItem(""));//Separator
        items.add(new NavDrawerItem(IconValue.fa_user_times.character(), "Log out", -1));
        items.add(new NavDrawerItem(IconValue.fa_wrench.character(), "Settings", -1));

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                navDrawerLoggedInAs.setText("Logged in as " + GlobalVars.loggedInUser.getBrukernavn());
                navDrawerList.setItemChecked(0, true);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.init(getActivity());
        GlobalVars.init(getActivity());
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        navDrawerList = (ListView) layout.findViewById(R.id.navDrawerList);
        navDrawerLoggedInAs = (TextView) layout.findViewById(R.id.navDrawerLoggedInAs);
        final VivzAdapter adapter = new VivzAdapter(getActivity(), R.layout.nav_drawer_row, items);
        navDrawerList.setAdapter(adapter);
        navDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items.get(position).title.equals("Log out")) {
                    logOut(getActivity());
                } else if (items.get(position).title.equals("Movies")) {
                    setSelectedList(Types.ChosenListType.Your, movieCardAdapter);
                    drawerLayout.closeDrawers();
                } else if (items.get(position).title.equals("Borrowed movies")) {
                    setSelectedList(Types.ChosenListType.Borrowed, movieCardAdapter);
                    drawerLayout.closeDrawers();
                } else if (items.get(position).title.equals("Lent movies")) {
                    setSelectedList(Types.ChosenListType.Lent, movieCardAdapter);
                    drawerLayout.closeDrawers();
                }else if (items.get(position).title.equals("Settings")){
                    showToast("Not implemented yet");
                }
            }
        });
        updateDrawer();
        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar, MovieCardAdapter movieCardAdapter) {
        View containerView = getActivity().findViewById(fragmentId);
        this.movieCardAdapter = movieCardAdapter;
        this.drawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                updateDrawer();

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(KEY_USER_LEARNED_DRAWER, true + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            drawerLayout.openDrawer(containerView);
        }
        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }
}

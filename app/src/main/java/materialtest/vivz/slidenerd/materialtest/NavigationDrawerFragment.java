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
import materialtest.vivz.slidenerd.materialtest.utils.Types.ChosenListType;

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
    private DrawerLayout drawerLayout;
    private NavDrawerItemAdapter adapter;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    public void updateDrawer() {
        items.clear();
        items.add(new NavDrawerItem(IconValue.fa_film.character(), getActivity().getString(R.string.movies), MovieList.getYourMovies().size()));
        items.add(new NavDrawerItem(IconValue.fa_mail_reply.character(), getActivity().getString(R.string.borrowedMovies), MovieList.getBorrowedMovies().size()));
        items.add(new NavDrawerItem(IconValue.fa_mail_forward.character(), getActivity().getString(R.string.lentMovies), MovieList.getLentMovies().size()));
        items.add(new NavDrawerItem(""));//Separator
        items.add(new NavDrawerItem(IconValue.fa_user_times.character(), getActivity().getString(R.string.logOut), -1));
        items.add(new NavDrawerItem(IconValue.fa_wrench.character(), getActivity().getString(R.string.settings), -1));

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                navDrawerLoggedInAs.setText("Logged in as " + GlobalVars.loggedInUser.getBrukernavn());
                navDrawerList.setItemChecked(0, true);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.init(getActivity());
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        navDrawerList = (ListView) layout.findViewById(R.id.navDrawerList);
        navDrawerLoggedInAs = (TextView) layout.findViewById(R.id.navDrawerLoggedInAs);
        adapter = new NavDrawerItemAdapter(getActivity(), R.layout.nav_drawer_row, items);
        navDrawerList.setAdapter(adapter);
        navDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items.get(position).title.equals(getActivity().getString(R.string.logOut))) {
                    logOut(getActivity());
                } else if (items.get(position).title.equals(getActivity().getString(R.string.movies))) {
                    setSelectedList(ChosenListType.Your);
                    drawerLayout.closeDrawers();
                } else if (items.get(position).title.equals(getActivity().getString(R.string.borrowedMovies))) {
                    setSelectedList(ChosenListType.Borrowed);
                    drawerLayout.closeDrawers();
                } else if (items.get(position).title.equals(getActivity().getString(R.string.lentMovies))) {
                    setSelectedList(ChosenListType.Lent);
                    drawerLayout.closeDrawers();
                } else if (items.get(position).title.equals(getActivity().getString(R.string.settings))) {
                    showToast(getActivity().getString(R.string.notImplementedYet));
                }
            }
        });
        updateDrawer();
        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        View containerView = getActivity().findViewById(fragmentId);
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

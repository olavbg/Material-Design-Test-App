package materialtest.vivz.slidenerd.materialtest.addmovie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import materialtest.vivz.slidenerd.materialtest.R;


public class AddMovieActivity extends ActionBarActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private FragmentClasses.ManualAddFragment manualAddFragment = new FragmentClasses.ManualAddFragment();
    private FragmentClasses.ScanBarcodeFragment scanBarcodeFragment = new FragmentClasses.ScanBarcodeFragment();
    private FragmentClasses.QuickAddFragment quickAddFragment = new FragmentClasses.QuickAddFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        setTopPadding();
        mViewPager.setCurrentItem(getIntent().getIntExtra("index",0), true);

        manualAddFragment = new FragmentClasses.ManualAddFragment();
        scanBarcodeFragment = new FragmentClasses.ScanBarcodeFragment();
        quickAddFragment = new FragmentClasses.QuickAddFragment();
    }

    private void setTopPadding() {
        mViewPager.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    quickAddFragment.disableCamera();
                    return manualAddFragment;
                case 1:
                    quickAddFragment.enableCameraCamera();
                    return scanBarcodeFragment;
                case 2:
                    quickAddFragment.enableCameraCamera();
                    return quickAddFragment;
                default:
                    quickAddFragment.disableCamera();
                    return manualAddFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
}

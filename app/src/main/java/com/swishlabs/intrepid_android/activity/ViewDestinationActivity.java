package com.swishlabs.intrepid_android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.CustomTabContainer;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;

import java.util.ArrayList;
import java.util.Locale;

public class ViewDestinationActivity extends ActionBarActivity {


    SectionsPagerAdapter mSectionsPagerAdapter;
    public static ViewDestinationActivity instance;
    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
//    TextView mToolbarTitle;
    CustomTabContainer mTabContainer;
    ArrayList<String> tabNames = new ArrayList<String>();

    private String[] tabs = { "Overview", "Climate", "Currency" };
    public static ViewDestinationActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_destination);
        instance = this;
        IntrepidMenu.setupMenu(instance, ViewDestinationActivity.this);
        setupTabNames();

//        mToolbarTitle = (TextView)findViewById(R.id.toolbar_title);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);





    }


    private void setOnPageChangeListener(ViewPager viewPager){
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabContainer.slideScrollIndicator(position, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
//                mToolbarTitle.setText(tabNames.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected void setupTabNames(){
        tabNames.add("General");
        tabNames.add("Culture");
        tabNames.add("Currency");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        createTabs();
    }

    private void createTabs(){
        mTabContainer = (CustomTabContainer)findViewById(R.id.tabContainer);
        mTabContainer.createTabs(tabNames, mViewPager);
        setOnPageChangeListener(mViewPager);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_destination, menu);
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


    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
//            switch (position) {
//                case 0:
//                    return "Overview";
//                case 1:
//                    return "Overview";
//                case 2:
//                    return "Overview";
//            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_overview_general, container, false);
            return rootView;
        }
    }

}

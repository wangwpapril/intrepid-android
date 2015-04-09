package com.swishlabs.intrepid_android.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.data.api.model.Trip;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;

import java.util.Locale;

public class TripPagesActivity extends ActionBarActivity implements TripFragment.OnFragmentInteractionListener
{

    public DatabaseManager mDatabaseManager;
    public Database mDatabase;
    public int mTripCount;
    public TripPagesActivity mTripPagesActivity;
    public static TripPagesActivity instance;

    public static TripPagesActivity getInstance(){
        return instance;
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    public Database getDatabase(){
        return mDatabase;
    }

    public void loadDatabase(){
        mDatabaseManager = new DatabaseManager(this.getBaseContext());
        mDatabase = mDatabaseManager.openDatabase("Intrepid.db");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setContentView(R.layout.activity_trip_pages);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mTripPagesActivity = this;
        loadDatabase();
        mTripCount = DatabaseManager.getTripCount(mDatabase);



        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trip_pages, menu);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(ViewPager pager) {
            super(getSupportFragmentManager());

            mViewPager.setAdapter(this);
            this.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            Trip trip = DatabaseManager.getTrip(position, mDatabase);
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return TripFragment.newInstance(trip.getId(), trip.getDestinationName(), trip.getGeneralImage());
        }

        @Override
        public int getCount() {
//            int tripCount = DatabaseManager.getTripCount(mDatabase);
// number of pages to be able to swipe through

            return mTripCount;
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

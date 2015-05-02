package com.swishlabs.intrepid_android.activity;

import android.content.Intent;
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
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

import java.util.List;

public class TripPagesActivity extends ActionBarActivity implements TripFragment.OnFragmentInteractionListener
{

    public DatabaseManager mDatabaseManager;
    public Database mDatabase;
    public int mTripCount;
    public TripPagesActivity mTripPagesActivity;
    public static TripPagesActivity instance;
    public List<Trip> mTripList;

    public static TripPagesActivity getInstance(){
        return instance;
    }

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

        mTripPagesActivity = this;
        loadDatabase();
        mTripList = DatabaseManager.getTripArray(mDatabase, SharedPreferenceUtil.getString(Enums.PreferenceKeys.userId.toString(), null));
        mTripCount = mTripList.size();

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

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent mIntent = new Intent(TripPagesActivity.this, SettingsActivity.class);
            startActivity(mIntent);

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
            if (position == 0){
                return TripFragment.newInstance(-1, "", "", "");
            }else{
//                Trip trip = DatabaseManager.getTrip(position - 1, mDatabase);
                Trip trip = mTripList.get(position -1);

                return TripFragment.newInstance(trip.getId(), trip.getDestinationName(), trip.getGeneralImage(), trip.getCountryId());
            }

        }

        @Override
        public int getCount() {
            // number of pages to be able to swipe through
            return mTripCount+1;
        }

    }

}

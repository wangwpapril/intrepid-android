package com.swishlabs.intrepid_android.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.IndicatorLinearLayout;
import com.swishlabs.intrepid_android.data.api.model.Trip;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.services.LocationService;
import com.swishlabs.intrepid_android.util.AndroidLocationServices;
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

    public static PendingIntent pendingIntent;

    public static TripPagesActivity getInstance(){
        return instance;
    }

    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    IndicatorLinearLayout mIndicator;

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
        MyApplication.getInstance().addActivity(this);
        instance=this;
        setContentView(R.layout.activity_trip_pages);

        startLocationService();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        mTripPagesActivity = this;
        loadDatabase();
        mTripList = DatabaseManager.getTripArray(mDatabase, SharedPreferenceUtil.getString(Enums.PreferenceKeys.userId.toString(), null));
        mTripCount = mTripList.size();


        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                mIndicator.updatePageIndicator(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mIndicator = (IndicatorLinearLayout) findViewById(R.id.indicator);
        mIndicator.initPoints(mTripCount+1, SharedPreferenceUtil.getInt(this, Enums.PreferenceKeys.currentPage.toString(), 0), mViewPager);
        moveToPage();

    }

    private void moveToPage(){
        int page = SharedPreferenceUtil.getInt(this, Enums.PreferenceKeys.currentPage.toString(), 0);
        if (page > 0){
            mViewPager.setCurrentItem(page);
            SharedPreferenceUtil.setInt(TripPagesActivity.getInstance(), Enums.PreferenceKeys.currentPage.toString(), 0);
        }
    }


    public void startLocationService(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, LocationService.class);
        intent.setAction(LocationService.ACTION_REPORT_POSITION);
        pendingIntent = PendingIntent.getService(this,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 10 * 60 * 1000, pendingIntent);
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

}

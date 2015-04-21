package com.swishlabs.intrepid_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.adapter.HealthListAdapter;
import com.swishlabs.intrepid_android.adapter.HealthMedListAdapter;
import com.swishlabs.intrepid_android.customViews.ClearEditText;
import com.swishlabs.intrepid_android.customViews.CustomTabContainer;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.data.api.model.HealthConditionDis;
import com.swishlabs.intrepid_android.data.api.model.HealthMedicationDis;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewHealthActivity extends ActionBarActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    public static ViewHealthActivity instance;
    public static ClearEditText mEditTextSearch;

    public Database mDatabase;
    public DatabaseManager mDatabaseManager;
    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    //    TextView mToolbarTitle;
    CustomTabContainer mTabContainer;
    ArrayList<String> tabNames = new ArrayList<String>();

    public static List<HealthConditionDis> mHealthConList = new ArrayList<HealthConditionDis>();
    public int mHealthCount;

    public static List<HealthMedicationDis> mHealthMedList = new ArrayList<HealthMedicationDis>();
    public int mHealthMedCount;

    public static HealthMedListAdapter mHealthMedListAdapter;
    public static HealthListAdapter mHealthListAdapter;
    public static int index = 0;

    IntrepidMenu mIntrepidMenu;

    public static ViewHealthActivity getInstance(){
        return instance;
    }

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
        setContentView(R.layout.activity_view_health);
        instance = this;
        mIntrepidMenu = (IntrepidMenu)findViewById(R.id.intrepidMenu);
        mIntrepidMenu.setupMenu(instance, ViewHealthActivity.this);
        setupTabNames();
        loadDatabase();

        String countryId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.currentCountryId.toString(),"");

        mHealthConList = DatabaseManager.getHealthConArray(mDatabase, countryId);
        mHealthCount = mHealthConList.size();

        mHealthMedList = DatabaseManager.getHealthMedArray(mDatabase,countryId);
        mHealthMedCount = mHealthMedList.size();

//        mToolbarTitle = (TextView)findViewById(R.id.toolbar_title);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mEditTextSearch = (ClearEditText) findViewById(R.id.search_ed);
        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                    String text = mEditTextSearch.getText().toString().toLowerCase(Locale.getDefault());
                String text = s.toString().toLowerCase(Locale.getDefault());
                switch (index) {
                    case 0:
                        mHealthListAdapter.getFilter(instance).filter(text);
                        break;
                    case 1:
                        mHealthMedListAdapter.getFilter(instance).filter(text);
                        break;
                }

            }
        });



    }

    private void setOnPageChangeListener(final ViewPager viewPager){
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabContainer.slideScrollIndicator(position, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(mIntrepidMenu.mState == 1)
                    mIntrepidMenu.snapToBottom();

            }
        });
    }

    protected void setupTabNames(){
        tabNames.add("CONDITIONS");
        tabNames.add("MEDICATIONS");
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
            // Show 2 total pages.
            return 2;
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

        private ListView list;

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
            Bundle args = getArguments();
            int index = args.getInt(ARG_SECTION_NUMBER);
//            final HealthListAdapter mHealthListAdapter;
  //          final HealthMedListAdapter mHealthMedListAdapter;
            View rootView = inflater.inflate(R.layout.fragment_view_health, container, false);

            switch (index) {
                case 1:
                    list = (ListView) rootView.findViewById(R.id.health_list);

                    mHealthListAdapter = new HealthListAdapter(
                            mHealthConList, instance);
                    list.setAdapter(mHealthListAdapter);
                    list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            HealthConditionDis hcDis = mHealthConList.get(position);
                            Intent mIntent = new Intent( instance, DetailHealthConActivity.class);
                            mIntent.putExtra("name", hcDis.getmConditionName());
                            mIntent.putExtra("description", hcDis.getmDescription());
                            mIntent.putExtra("symptoms", hcDis.getmSymptoms());
                            mIntent.putExtra("prevention", hcDis.getmPrevention());
                            startActivity(mIntent);
                            instance.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                        }
                    });
                    break;
                case 2:
                    list = (ListView) rootView.findViewById(R.id.health_list);

                    mHealthMedListAdapter = new HealthMedListAdapter(
                            mHealthMedList, instance);
                    list.setAdapter(mHealthMedListAdapter);
                    list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            HealthMedicationDis hmDis = mHealthMedList.get(position);
                            Intent mIntent = new Intent( instance, DetailHealthMedActivity.class);
                            mIntent.putExtra("name", hmDis.getmMedicationName());
                            mIntent.putExtra("brand name", hmDis.getmBrandNames());
                            mIntent.putExtra("description", hmDis.getmDescription());
                            mIntent.putExtra("side effects", hmDis.getmSideEffects());
                            mIntent.putExtra("storage", hmDis.getmStorage());
                            mIntent.putExtra("notes", hmDis.getmNotes());
                            startActivity(mIntent);
                            instance.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                        }
                    });
                    break;

            }

            return rootView;
        }
    }
}

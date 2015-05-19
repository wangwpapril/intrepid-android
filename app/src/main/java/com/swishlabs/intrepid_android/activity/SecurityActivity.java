package com.swishlabs.intrepid_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.adapter.EmbassyListAdapter;
import com.swishlabs.intrepid_android.customViews.CustomTabContainer;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.data.api.model.DestinationInformation;
import com.swishlabs.intrepid_android.data.api.model.Embassy;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Locale;

public class SecurityActivity extends ActionBarActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    public static SecurityActivity instance;
    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    public DatabaseManager mDatabaseManager;
    public Database mDatabase;
    ViewPager mViewPager;
    String mDestinationId;
    DestinationInformation mDestinationInformation;
    //    TextView mToolbarTitle;
    CustomTabContainer mTabContainer;
    IntrepidMenu mIntrepidMenu;
    ArrayList<String> tabNames = new ArrayList<String>();

    private String[] tabs = {"Security", "Embassy"};

    public static SecurityActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        loadDatabase();
        mDestinationId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.currentCountryId.toString(), null);
        mDestinationInformation = DatabaseManager.getDestinationInformation(mDatabase, mDestinationId);
        setContentView(R.layout.activity_view_security);
        instance = this;
        mIntrepidMenu = (IntrepidMenu)findViewById(R.id.intrepidMenu);
        mIntrepidMenu.setupMenu(instance, SecurityActivity.this, true);
        setupTabNames();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }

    public void loadDatabase() {
        mDatabaseManager = new DatabaseManager(this.getBaseContext());
        mDatabase = mDatabaseManager.openDatabase("Intrepid.db");
    }


    private void setOnPageChangeListener(ViewPager viewPager) {
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

    protected void setupTabNames() {
        tabNames.add("SAFETY");
        tabNames.add("EMBASSY");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        createTabs();
    }

    private void createTabs() {
        mTabContainer = (CustomTabContainer) findViewById(R.id.tabContainer);
        mTabContainer.createTabs(tabNames, mViewPager);
        setOnPageChangeListener(mViewPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_view_destination, menu);
//        getSupportActionBar().setTitle("");
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
            if (position == 0) {
                return SecurityFragment.newInstance(position + 1);
            } else {
                return EmbassyListFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
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
    public static class SecurityFragment extends Fragment {



        private static final String ARG_SECTION_NUMBER = "section_number";

        public static SecurityFragment newInstance(int sectionNumber) {
            SecurityFragment fragment = new SecurityFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public SecurityFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_security, container, false);
            populateSecurityInfo(rootView);
            return rootView;
        }

        public void populateSecurityInfo(View rootView) {
            DestinationInformation destinationInformation = SecurityActivity.getInstance().mDestinationInformation;
            ImageView generalImage = (ImageView)rootView.findViewById(R.id.overview_image);
            Picasso.with(ViewDestinationActivity.getInstance()).load(destinationInformation.getImageSecurity()).resize(1000, 1000).centerCrop().into(generalImage);
            TextView safetyText = (TextView)rootView.findViewById(R.id.safety_content);
            safetyText.setText(destinationInformation.getSafety());
            TextView otherConcernsText = (TextView)rootView.findViewById(R.id.other_concerns_text);
            otherConcernsText.setText(destinationInformation.getOtherConcerns());
        }
    }

    public static class EmbassyListFragment extends Fragment {
        public String mDesinationId;
        public EmbassyListAdapter mEmbassyListAdapter;
        public ArrayList<Embassy> embassyList;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static EmbassyListFragment newInstance(int sectionNumber) {
            EmbassyListFragment fragment = new EmbassyListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public EmbassyListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_embassy_list, container, false);
            populateEmbassyListFragment(rootView);
            setupListClickListener(rootView);
            return rootView;
        }

        public void populateEmbassyListFragment(View rootView) {
            DestinationInformation destinationInformation = SecurityActivity.getInstance().mDestinationInformation;
            mDesinationId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.currentCountryId.toString(), null);
            embassyList= DatabaseManager.getEmbassyListArray(SecurityActivity.getInstance().mDatabase, mDesinationId);

            mEmbassyListAdapter = new EmbassyListAdapter(
                    embassyList, instance, SecurityActivity.getInstance().mDatabase);
            ListView listView = (ListView)rootView.findViewById(R.id.list);
            listView.setAdapter(mEmbassyListAdapter);
            ImageView embassyImage = (ImageView)rootView.findViewById(R.id.overview_image);
            if (embassyList.size()>0) {
                Picasso.with(ViewDestinationActivity.getInstance()).load(embassyList.get(0).getImage()).resize(1000, 1000).centerCrop().into(embassyImage);
            }else{
                Picasso.with(ViewDestinationActivity.getInstance()).load(destinationInformation.getImageIntro()).resize(1000, 1000).centerCrop().into(embassyImage);
                TextView emptyEmbassyList = (TextView)rootView.findViewById(R.id.emptyEmbassyList);
                emptyEmbassyList.setVisibility(View.VISIBLE);
            }
        }

        public void setupListClickListener(View rootView){
            ListView listView = (ListView) rootView.findViewById(R.id.list);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View view, int position,
                                        long arg3) {
                    Intent mIntent = new Intent(SecurityActivity.getInstance(), ViewEmbassyActivity.class);
                    String embassyId = embassyList.get(position).getId();
                    mIntent.putExtra("embassyId", embassyId);
                    startActivity(mIntent);
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        if(mIntrepidMenu.mState == 1){
            mIntrepidMenu.snapToBottom();
            return;
        }
        super.onBackPressed();
        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

}
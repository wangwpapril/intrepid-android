package com.swishlabs.intrepid_android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.CustomTabContainer;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.data.api.model.Currency;
import com.swishlabs.intrepid_android.data.api.model.DestinationInformation;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.ImageLoader;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Locale;

public class ViewDestinationActivity extends ActionBarActivity {


    SectionsPagerAdapter mSectionsPagerAdapter;
    public static ViewDestinationActivity instance;
    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    public DatabaseManager mDatabaseManager;
    public Database mDatabase;
    ViewPager mViewPager;
    String mDestinationId;
    DestinationInformation mDestinationInformation;

    String baseCurrencyCode, desCurrencyCode;
    Currency baseCurrency, desCurrency;

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
        loadDatabase();
        mDestinationId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.currentCountryId.toString(), null);
        mDestinationInformation = DatabaseManager.getDestinationInformation(mDatabase,mDestinationId);

        baseCurrencyCode = SharedPreferenceUtil.getString(Enums.PreferenceKeys.currencyCode.toString(),null);
        baseCurrency = DatabaseManager.getCurrency(baseCurrencyCode,mDatabase);
        desCurrency = DatabaseManager.getCurrency(mDestinationInformation.getCurrencyCode(),mDatabase);

        setContentView(R.layout.activity_view_destination);
        instance = this;
        IntrepidMenu.setupMenu(instance, ViewDestinationActivity.this);
        setupTabNames();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }

    public void loadDatabase(){
        mDatabaseManager = new DatabaseManager(this.getBaseContext());
        mDatabase = mDatabaseManager.openDatabase("Intrepid.db");
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
            if (position == 0) {
                return OverviewGeneralFragment.newInstance(position + 1);
            }else if (position == 1){
                return OverviewCultureFragment.newInstance(position + 1);
            }else {
                return OverviewCurrencyFragment.newInstance(position + 1);
                //TODO change this to a currency fragment
            }
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
    public static class OverviewGeneralFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static OverviewGeneralFragment newInstance(int sectionNumber) {
            OverviewGeneralFragment fragment = new OverviewGeneralFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public OverviewGeneralFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_overview_general, container, false);
            populateGeneralOverview(rootView);
            return rootView;
        }

        public void populateGeneralOverview(View rootView){
            DestinationInformation destinationInformation = ViewDestinationActivity.getInstance().mDestinationInformation;
            ImageView generalImage = (ImageView)rootView.findViewById(R.id.overview_image);
            Picasso.with(ViewDestinationActivity.getInstance()).load(destinationInformation.getImageOverview()).resize(1000, 1000).centerCrop().into(generalImage);
            TextView locationText = (TextView)rootView.findViewById(R.id.destination_content);
            locationText.setText(destinationInformation.getLocation());
            TextView climateText = (TextView)rootView.findViewById(R.id.destination_content2);
            climateText.setText(destinationInformation.getClimate());
            TextView governmentText = (TextView)rootView.findViewById(R.id.destination_content3);
            governmentText.setText(destinationInformation.getTypeOfGovernment());

        }
    }

    public static class OverviewCultureFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static OverviewCultureFragment newInstance(int sectionNumber) {
            OverviewCultureFragment fragment = new OverviewCultureFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public OverviewCultureFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_overview_culture, container, false);
            populateCultureOverview(rootView);
            return rootView;
        }

        public void populateCultureOverview(View rootView){
            DestinationInformation destinationInformation = ViewDestinationActivity.getInstance().mDestinationInformation;
            ImageView generalImage = (ImageView)rootView.findViewById(R.id.overview_image);
            Picasso.with(ViewDestinationActivity.getInstance()).load(destinationInformation.getImageCulture()).resize(1000, 1000).centerCrop().into(generalImage);
            TextView locationText = (TextView)rootView.findViewById(R.id.destination_content);
            locationText.setText(destinationInformation.getCulturalNorms());
            TextView climateText = (TextView)rootView.findViewById(R.id.destination_content2);
            climateText.setText(destinationInformation.getEthnicMakeup());
            TextView governmentText = (TextView)rootView.findViewById(R.id.destination_content3);
            governmentText.setText(destinationInformation.getLanguageInfo());

        }
    }

    public static class OverviewCurrencyFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        protected com.swishlabs.intrepid_android.util.ImageLoader ImageLoader;

        private TextWatcher baseWatcher, desWatcher;

        public static OverviewCurrencyFragment newInstance(int sectionNumber) {
            OverviewCurrencyFragment fragment = new OverviewCurrencyFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public OverviewCurrencyFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_overview_currency, container, false);
            populateCultureOverview(rootView);
            return rootView;
        }

        public void populateCultureOverview(View rootView){

            if (ImageLoader == null) {
                ImageLoader = new ImageLoader(ViewDestinationActivity.getInstance(), R.drawable.ic_launcher);
            }

            TextView baseCurrencyTv = (TextView)rootView.findViewById(R.id.base_currency_code);
            baseCurrencyTv.setText(ViewDestinationActivity.getInstance().baseCurrency.getCurrencyCode());

            ImageView baseImageIv = (ImageView)rootView.findViewById(R.id.base_currency_icon);
            baseImageIv.setTag(ViewDestinationActivity.getInstance().baseCurrency.getImageUrl());
            ImageLoader.DisplayImage(ViewDestinationActivity.getInstance().baseCurrency.getImageUrl(),
                    ViewDestinationActivity.getInstance(), baseImageIv);


            EditText baseValueEt = (EditText)rootView.findViewById(R.id.base_currency_value);


            TextView desCurrencyTv = (TextView)rootView.findViewById(R.id.des_currency_code);
            desCurrencyTv.setText(ViewDestinationActivity.getInstance().desCurrency.getCurrencyCode());

            final ImageView desImageIv = (ImageView)rootView.findViewById(R.id.des_currency_icon);
            desImageIv.setTag(ViewDestinationActivity.getInstance().desCurrency.getImageUrl());
            ImageLoader.DisplayImage(ViewDestinationActivity.getInstance().desCurrency.getImageUrl(),
                    ViewDestinationActivity.getInstance(), desImageIv);

            final EditText desValueEt = (EditText) rootView.findViewById(R.id.des_currency_value);
            desValueEt.setText(ViewDestinationActivity.getInstance().mDestinationInformation.getCurrencyRate());

            baseWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    desValueEt.removeTextChangedListener(desWatcher);
                    double baseValue = 0, desValue = 0;

                    baseValue = Double.parseDouble(s.toString());
                    desValue = baseValue * Double.parseDouble(ViewDestinationActivity.getInstance().mDestinationInformation.mCurrencyRate);
                    desValueEt.setText(String.valueOf(desValue));

                }

                @Override
                public void afterTextChanged(Editable s) {

                    desValueEt.addTextChangedListener(desWatcher);


                }
            };

            desWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            baseValueEt.addTextChangedListener(baseWatcher);
            desValueEt.addTextChangedListener(desWatcher );


        }
    }

}

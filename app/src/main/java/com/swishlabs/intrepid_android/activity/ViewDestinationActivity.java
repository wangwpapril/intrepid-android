package com.swishlabs.intrepid_android.activity;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.CustomTabContainer;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.data.api.model.Currency;
import com.swishlabs.intrepid_android.data.api.model.DestinationInformation;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.services.LocationService;
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
    IntrepidMenu mIntrepidMenu;

    String baseCurrencyCode, desCurrencyCode;
    Currency baseCurrency, desCurrency;

    boolean firstFlag = false;

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
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        MyApplication.getInstance().addActivity(this);
        loadDatabase();

        mDestinationId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.currentCountryId.toString(), null);
        mDestinationInformation = DatabaseManager.getDestinationInformation(mDatabase, mDestinationId);

        baseCurrencyCode = SharedPreferenceUtil.getString(Enums.PreferenceKeys.currencyCode.toString(), null);
        baseCurrency = DatabaseManager.getCurrency(baseCurrencyCode, mDatabase);
        desCurrency = DatabaseManager.getCurrency(mDestinationInformation.getCurrencyCode(), mDatabase);

        setContentView(R.layout.activity_view_destination);
        instance = this;
        mIntrepidMenu = (IntrepidMenu)findViewById(R.id.intrepidMenu);
        mIntrepidMenu.setupMenu(instance, ViewDestinationActivity.this, false);


        setupTabNames();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        if("1".equals(getIntent().getStringExtra("firstTimeFlag"))){
            firstFlag = true;
        }



    }

    public void checkMenuClick(View v) {
        Log.d("check", "hi");
        if (v != mIntrepidMenu && v.getParent()!=mIntrepidMenu){
            mIntrepidMenu.snapToBottom();
        }
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
                InputMethodManager imm = (InputMethodManager) instance.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mIntrepidMenu.mState == 1)
                    mIntrepidMenu.snapToBottom();

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

    private void openMenu(){
        if(firstFlag){
            firstFlag = false;
            if(mIntrepidMenu.mState == 0){
                mIntrepidMenu.appearTop();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
//        if (mIntrepidMenu!=null){
//            mIntrepidMenu.snapToBottom();
//        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
 //       getMenuInflater().inflate(R.menu.menu_view_destination, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            SharedPreferenceUtil.setString(Enums.PreferenceKeys.userId.toString(), "");
            SharedPreferenceUtil.setString(Enums.PreferenceKeys.token.toString(), "");
            SharedPreferenceUtil.setString(Enums.PreferenceKeys.email.toString(), "");
            SharedPreferenceUtil.setString(Enums.PreferenceKeys.firstname.toString(), "");
            SharedPreferenceUtil.setString(Enums.PreferenceKeys.lastname.toString(), "");
            SharedPreferenceUtil.setString(Enums.PreferenceKeys.username.toString(), "");
            SharedPreferenceUtil.setString(Enums.PreferenceKeys.countryCode.toString(), "");
            SharedPreferenceUtil.setString(Enums.PreferenceKeys.currencyCode.toString(), "");
            SharedPreferenceUtil.setApList(this, null);
            SharedPreferenceUtil.setBoolean(getApplicationContext(), Enums.PreferenceKeys.loginStatus.toString(), false);
            MyApplication.setLoginStatus(false);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(TripPagesActivity.pendingIntent);

            mDatabaseManager.deleteDatabase("Intrepid.db");

            Intent mIntent = new Intent(this, LoginActivity.class);
            startActivity(mIntent);
            this.finish();

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
            ViewDestinationActivity.getInstance().openMenu();
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
            TextView visaText = (TextView)rootView.findViewById(R.id.destination_content4);
            visaText.setText(destinationInformation.getVisaRequirements());
            TextView communication = (TextView)rootView.findViewById(R.id.destination_content5);
            communication.setText(destinationInformation.getCommunicationsInfrastructure());
            TextView electricityText = (TextView)rootView.findViewById(R.id.destination_content6);
            electricityText.setText(destinationInformation.getElectricity());
            TextView developmentText = (TextView)rootView.findViewById(R.id.destination_content7);
            developmentText.setText(destinationInformation.getDevelopment());
            TextView moneyText = (TextView)rootView.findViewById(R.id.destination_content8);
            moneyText.setText(destinationInformation.getCurrency());
            TextView transportationText = (TextView)rootView.findViewById(R.id.destination_content9);
            transportationText.setText(destinationInformation.getTransportation());
            TextView holidayText = (TextView)rootView.findViewById(R.id.destination_content10);
            holidayText.setText(destinationInformation.getmHolidays());

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
            TextView normsText = (TextView)rootView.findViewById(R.id.destination_content);
            normsText.setText(destinationInformation.getCulturalNorms());
            TextView ethnicText= (TextView)rootView.findViewById(R.id.destination_content2);
            ethnicText.setText(destinationInformation.getEthnicMakeup());
            TextView languageText = (TextView)rootView.findViewById(R.id.destination_content3);
            languageText.setText(destinationInformation.getLanguageInfo());
            TextView religionText = (TextView)rootView.findViewById(R.id.destination_content4);
            religionText.setText(destinationInformation.getReligion());

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
            populateCurrencyOverview(rootView);
            return rootView;
        }

        public void populateCurrencyOverview(View rootView){

            if (ImageLoader == null) {
                ImageLoader = new ImageLoader(ViewDestinationActivity.getInstance(), R.drawable.empty_square);
            }

            TextView baseCurrencyTv = (TextView)rootView.findViewById(R.id.base_currency_code);
            baseCurrencyTv.setText(ViewDestinationActivity.getInstance().baseCurrency.getCurrencyCode());

            ImageView baseImageIv = (ImageView)rootView.findViewById(R.id.base_currency_icon);
            baseImageIv.setTag(ViewDestinationActivity.getInstance().baseCurrency.getImageUrl());
            ImageLoader.DisplayImage(ViewDestinationActivity.getInstance().baseCurrency.getImageUrl(),
                    ViewDestinationActivity.getInstance(), baseImageIv);

            final ImageView baseSelector = (ImageView) rootView.findViewById(R.id.currency_selector);
            final ImageView desSelector = (ImageView) rootView.findViewById(R.id.currency_selector2);


            final EditText baseValueEt = (EditText)rootView.findViewById(R.id.base_currency_value);

            baseValueEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        baseSelector.setVisibility(View.INVISIBLE);
                    }else {

                        baseSelector.setVisibility(View.VISIBLE);
                    }
                }
            });

            TextView desCurrencyTv = (TextView)rootView.findViewById(R.id.des_currency_code);
            desCurrencyTv.setText(ViewDestinationActivity.getInstance().desCurrency.getCurrencyCode());

            final ImageView desImageIv = (ImageView)rootView.findViewById(R.id.des_currency_icon);
            desImageIv.setTag(ViewDestinationActivity.getInstance().desCurrency.getImageUrl());
            ImageLoader.DisplayImage(ViewDestinationActivity.getInstance().desCurrency.getImageUrl(),
                    ViewDestinationActivity.getInstance(), desImageIv);

            final EditText desValueEt = (EditText) rootView.findViewById(R.id.des_currency_value);
            desValueEt.setText(reFormat(ViewDestinationActivity.getInstance().mDestinationInformation.getCurrencyRate()));

            desValueEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){

                        desSelector.setVisibility(View.INVISIBLE);
                    } else {

                        desSelector.setVisibility(View.VISIBLE);
                    }

                }
            });

            baseWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    desValueEt.removeTextChangedListener(desWatcher);
                    double baseValue = 0, desValue = 0;

                    if(s.toString().equals("")) {
                        baseValue = 0;
                        desValue = 0;
                    }else {
                        baseValue = Double.parseDouble(s.toString());
                        desValue = baseValue * Double.parseDouble(ViewDestinationActivity.getInstance().mDestinationInformation.mCurrencyRate);
                    }

                    desValueEt.setText(reFormat(String.valueOf(desValue)));

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

                    baseValueEt.removeTextChangedListener(baseWatcher);

/*                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + 3);
                            desValueEt.setText(s);
                            desValueEt.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        desValueEt.setText(s);
                        desValueEt.setSelection(2);
                    }

                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            desValueEt.setText(s.subSequence(0, 1));
                            desValueEt.setSelection(1);
                            return;
                        }
                    }*/


                    double baseValue = 0, desValue =0;

                    if(s.toString().equals("")) {
                        baseValue = 0;
                        desValue = 0;
                    }else {
                        desValue = Double.parseDouble(s.toString());
                        baseValue = desValue / Double.parseDouble(ViewDestinationActivity.getInstance().mDestinationInformation.mCurrencyRate);
                    }

                    baseValueEt.setText(reFormat(String.valueOf(baseValue)));
                }

                @Override
                public void afterTextChanged(Editable s) {

                    baseValueEt.addTextChangedListener(baseWatcher);

                }
            };

            baseValueEt.addTextChangedListener(baseWatcher);
            desValueEt.addTextChangedListener(desWatcher );


        }

        private String reFormat(String input){
            String result = input;

            if (input.contains(".")) {
                if (input.length() - 1 - input.indexOf(".") > 2) {
                    result = input.subSequence(0,
                            input.toString().indexOf(".") + 3).toString();
                }
            }
            if (input.trim().substring(0).equals(".")) {
                result = "0" + input;
            }

            if (input.startsWith("0")
                    && input.trim().length() > 1) {
                if (!input.substring(1, 2).equals(".")) {
                    result= input.subSequence(0,1).toString();
                }
            }

            return result;

        }

    }

    @Override
    public void onBackPressed(){
        if(mIntrepidMenu.mState == 1){
            mIntrepidMenu.snapToBottom();
            return;
        }
        super.onBackPressed();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


}

package com.swishlabs.intrepid_android.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.City;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.fragment.MapFragment;
import com.swishlabs.intrepid_android.fragment.TabFragment;
import com.swishlabs.intrepid_android.util.Enums;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements TabFragment.OnFragmentInteractionListener,
MapFragment.OnFragmentInteractionListener{

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

    List<City> infoList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        getCities();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onFragmentInteraction(int position) {
        mViewPager.setCurrentItem(position);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        return;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
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
            switch (position) {
                case 0:
                    return MapFragment.newInstance(null,null);

            }
            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
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

    private void getCities() {

        if(true) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    try {
                        InputStream is = getAssets().open("data.txt");
                        if(is != null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                            StringBuffer buffer=new StringBuffer();
                            String line;
                            while(null!=(line=bufferedReader.readLine())){
                                buffer.append(line).append("\n");
                            }

                            is.close();
                            String content = buffer.toString();

                            try {
                                JSONObject jsonObject = new JSONObject(content);
                                JSONArray array = jsonObject.getJSONArray("facilities");
                                int len = array.length();
                                infoList = new ArrayList<City>(len);

                                for(int i = 0;i < len; i++) {
                                    City city = new City();
                                    city.setId(array.getJSONObject(i).optString("id"));
                                    city.setName(array.getJSONObject(i).optJSONObject("type").optString("name"));
                                    city.setContent(array.getJSONObject(i).optString("name"));
                                    city.setLongitude(array.getJSONObject(i).optJSONObject("location").optString("long"));
                                    city.setLatitude(array.getJSONObject(i).optJSONObject("location").optString("lat"));

                                    infoList.add(city);
                                }

//                                MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
  //                              mapFragment.markList = infoList;


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }else {

            IControllerContentCallback icc = new IControllerContentCallback() {
                @Override
                public void handleSuccess(String content) throws JSONException {

                }

                @Override
                public void handleError(Exception e) {

                }
            };

            ControllerContentTask cct = new ControllerContentTask(
                    Constants.PPN_BASE_URL + "cities", icc,
                    Enums.ConnMethod.GET, false
            );

            String ss = null;
            cct.execute(ss);

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public List<City> getList() {
        return infoList;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}

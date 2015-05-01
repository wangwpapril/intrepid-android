package com.swishlabs.intrepid_android.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.IndicatorLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class LearnMoreActivity extends ActionBarActivity {

    public static LearnMoreActivity instance;
    ViewPager mViewPager;
    ImageView mCloseIv;
    IndicatorLinearLayout mIndicator;
    private ViewPagerAdapter mViewPagerAdapter;
    public List<LearnMoreContent> mLearnMoreContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        initContent();


        setContentView(R.layout.activity_learn_more);

/*        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                mIndicator.indicator(position);

//                TextView t = (TextView)mViewPager.getChildAt(position).findViewById(R.id.learnMoreContent);
  //              t.setText("test");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mIndicator = (IndicatorLinearLayout) findViewById(R.id.indicator);
        mIndicator.initPoints(mLearnMoreContent.size(), 0, mViewPager);

        mCloseIv = (ImageView) findViewById(R.id.cancel);
        mCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initContent() {
        mLearnMoreContent = new ArrayList<>();
        int resId;
        String des;

        final String packageName = getPackageName();

        resId = getResources().getIdentifier("overview", "drawable", packageName);
        des = "Information about the history, culture, and currency for the country.";
        mLearnMoreContent.add( new LearnMoreContent(resId, des));

        resId = getResources().getIdentifier("health", "drawable", packageName);
        des = "Information about health concerns including prevalent conditions and medications.";
        mLearnMoreContent.add( new LearnMoreContent(resId, des));

        resId = getResources().getIdentifier("security", "drawable", packageName);
        des = "Security information about the country including safety and embassy details.";
        mLearnMoreContent.add( new LearnMoreContent(resId, des));

        resId = getResources().getIdentifier("assistance", "drawable", packageName);
        des = "Get emergency assistance whenever you need it.";
        mLearnMoreContent.add( new LearnMoreContent(resId, des));

    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mLearnMoreContent.size();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        ImageView mImageView ;
        public TextView mTextView;
        private final static String FRAGMENT_INDEX = "index";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance (int position){

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(FRAGMENT_INDEX, position);

            fragment.setArguments(args);
            return fragment;

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int index = getArguments().getInt(FRAGMENT_INDEX);
            View rootView = inflater.inflate(R.layout.fragment_learn_more, container, false);
            mImageView = (ImageView) rootView.findViewById(R.id.learnMoreImage);
            mImageView.setImageResource(instance.mLearnMoreContent.get(index).getRsId());

            mTextView = (TextView) rootView.findViewById(R.id.learnMoreContent);
            mTextView.setText(instance.mLearnMoreContent.get(index).getLearnMoreDes());

            return rootView;
        }
    }

    public class LearnMoreContent {
        int rsId;
        String learnMoreDes;

        public LearnMoreContent(int id, String description) {
            this.rsId = id;
            this.learnMoreDes = description;
        }

        int getRsId() { return rsId; }

        String getLearnMoreDes() { return learnMoreDes; }

    }
}

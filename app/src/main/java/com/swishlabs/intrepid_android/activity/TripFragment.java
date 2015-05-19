package com.swishlabs.intrepid_android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.RoundedTransformation;
import com.swishlabs.intrepid_android.data.api.model.Trip;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.ImageLoader;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TripFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripFragment extends android.support.v4.app.Fragment {

    Trip mTrip;
    int mTripIndex;
    String mDestinationId;
    Database mDatabase;
    TextView mCountryName;
    ImageView mCountryImage;
    protected ImageLoader imageLoader;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static TripFragment newInstance(int id, String destinationName, String imageURL, String destinationId) {
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();

        args.putInt("id", id);
        args.putString("destinationName", destinationName);
        args.putString("destinationId", destinationId);
        args.putString("imageURL", imageURL);
        fragment.setArguments(args);

        return fragment;
    }

    public TripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = new ImageLoader(TripPagesActivity.getInstance(),  R.drawable.abc_item_background_holo_light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        mTripIndex = getArguments().getInt("id");
        mDestinationId = getArguments().getString("destinationId");
        String destinationName = getArguments().getString("destinationName");
        String imageURL = getArguments().getString("imageURL");
//        mTrip = DatabaseManager.getTrip(mTripIndex, mDatabase);

        mCountryImage = (ImageView) view.findViewById(R.id.tripImage);
        mCountryName = (TextView) view.findViewById(R.id.tripName);
        if (mTripIndex == -1) {
            mCountryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(TripPagesActivity.getInstance(), DestinationsListActivity.class);
                    startActivity(mIntent);
                }
            });
        }else{
            Log.d("TripFragment", "Loaded: " + destinationName);
            getCountry(destinationName, imageURL);
            setupSwipe(view, mCountryImage, mTripIndex);
            mCountryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.currentCountryId.toString(), mDestinationId);
                    SharedPreferenceUtil.setInt(TripPagesActivity.getInstance(), Enums.PreferenceKeys.currentPage.toString(), TripPagesActivity.getInstance().mViewPager.getCurrentItem());
                    Intent mIntent = new Intent(TripPagesActivity.getInstance(), ViewDestinationActivity.class);

                    mIntent.putExtra("destinationId", mDestinationId);
                    mIntent.putExtra("firstTimeFlag","1");
                    startActivity(mIntent);
                }
            });
        }

        return view;
    }

    private void setupSwipe(View view, ImageView countryImage, int index){
        final GestureDetector detector = new GestureDetector(new UpSwipeGestureListener(view, countryImage, index));
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    protected class UpSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

        View mView;
        ImageView mCountryImage;
        int mIndex;

        UpSwipeGestureListener(View view, ImageView countryImage, int index){
            mView = view;
            mCountryImage = countryImage;
            mIndex = index;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            try {
                if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH){
                    return false;
                }
                // right to left swipe
                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onUpSwipe(mCountryImage, mIndex);
                }
            } catch (Exception e) {

            }
            return false;
        }

        public void onUpSwipe(final ImageView view, final int index){
            Log.d("TripPages", "Upward swipe detected");
            new AlertDialog.Builder(TripPagesActivity.getInstance())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to remove this trip?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -2000);
                            anim.setDuration(1000);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                   Database db = TripPagesActivity.getInstance().getDatabase();
                                   DatabaseManager.deleteTrip(index, db);
                                    Intent mIntent = new Intent(TripPagesActivity.getInstance(),TripPagesActivity.class);
                                    startActivity(mIntent);

                                    TripPagesActivity.getInstance().finish();

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            anim.setFillAfter(true);
                            view.startAnimation(anim);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    public void getCountry(String destinationName, String imageURL){
        mCountryName.setText(destinationName);
        Picasso.with(TripPagesActivity.getInstance()).load(imageURL).resize(1200, 1200).centerCrop().transform(new RoundedTransformation(50, 4)).into(mCountryImage);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

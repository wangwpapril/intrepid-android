package com.swishlabs.intrepid_android.activity;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.data.api.model.Trip;
import com.swishlabs.intrepid_android.data.store.Database;

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
    Database mDatabase;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static TripFragment newInstance(int id, String destinationName) {
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("destinationName", destinationName);
        fragment.setArguments(args);

        return fragment;
    }

    public TripFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        mTripIndex = getArguments().getInt("id");
        String destinationName = getArguments().getString("destinationName");
//        mTrip = DatabaseManager.getTrip(mTripIndex, mDatabase);
        Log.d("TripFragment", "Loaded: "+destinationName);
        return view;
    }

    public void getCountry(){

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

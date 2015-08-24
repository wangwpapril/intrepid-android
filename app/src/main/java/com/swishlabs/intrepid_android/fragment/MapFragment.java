package com.swishlabs.intrepid_android.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.activity.MainActivity;
import com.swishlabs.intrepid_android.data.api.model.Provider;
import com.swishlabs.intrepid_android.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private OnFragmentInteractionListener mListener;

    private ClusterManager<Place> mClusterManager;

    public List<Provider> markList = new ArrayList<>();

    MainActivity mActivity;

    static public MapFragment mapFragment;

    String currentFilter;

    RelativeLayout infoView;
    TextView mNameTv, mStaffNameTv, mContactTv, mAddressTv, mPostalTv;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
/*        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/

        mapFragment = fragment;

        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        markList.clear();

        if(savedInstanceState != null){
//            markList = savedInstanceState.getParcelableArrayList("List");
  //          markList = (ArrayList<Provider>) savedInstanceState.getSerializable("List2");

            currentFilter = savedInstanceState.getString("filter");
        } else {
            currentFilter = "All";
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private boolean network() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mActiveNet = mConnectivityManager.getActiveNetworkInfo();
        return mActiveNet != null && mActiveNet.isConnectedOrConnecting();
    }

    private void setUpMapIfNeeded() {
        Log.d("MapFragment", "setUpMapIfNeeded");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
//            mMap = view.findViewById(R.id.map).getMap
            // Check if we were successful in obtaining the map.

            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1))
                    .getMap();

            mClusterManager = new ClusterManager<Place>(getActivity(), mMap);
            mMap.setOnCameraChangeListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);

//            markList = mActivity.getList();

            markList = ((MainActivity) getActivity()).getList();
/*            if(markList != null) {
                for(int i = 0; i<markList.size(); i++) {
                    if(!markList.get(i).getLatitude().equals("null")&&!markList.get(i).getLongitude().equals("null")) {
                        mClusterManager.addItem(new Place( i,
                                Double.valueOf(markList.get(i).getLatitude()),
                                Double.valueOf(markList.get(i).getLongitude()),
                                markList.get(i).getType(),
                                markList.get(i).getName()
                        ));
                    }
                }
            }*/

            setupMarkerList(currentFilter);

            mClusterManager.setRenderer(new InfoRender(getActivity(), mMap, mClusterManager));

            mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<Place>() {
                @Override
                public boolean onClusterClick(Cluster<Place> cluster) {
                    return false;
                }
            });

            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Place>() {
                @Override
                public boolean onClusterItemClick(Place place) {
                    infoView.setVisibility(View.VISIBLE);
                    int index = place.getIndex();
                    mNameTv.setText(markList.get(index).getName());
                    mStaffNameTv.setText("Staff Name: " + markList.get(index).getStaffName());
                    mContactTv.setText("Contact: " + markList.get(index).getContact());
                    Linkify.addLinks(mContactTv, Linkify.PHONE_NUMBERS);
                    mAddressTv.setText("Address: " + markList.get(index).getAddress());
                    mPostalTv.setText("Postal: " + markList.get(index).getPostal());
                    return false;
                }
            });

            mClusterManager.setOnClusterInfoWindowClickListener(new ClusterManager.OnClusterInfoWindowClickListener<Place>() {
                @Override
                public void onClusterInfoWindowClick(Cluster<Place> cluster) {
                    return;
                }
            });

            mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<Place>() {
                @Override
                public void onClusterItemInfoWindowClick(Place place) {
                    return;
                }
            });

//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.5, -0.17), 11));
            if(markList != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(markList.get(0).getLatitude()),
                    Double.valueOf(markList.get(0).getLongitude())), 11));

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    return;
                }
            });

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    infoView.setVisibility(View.GONE);
                }
            });

        }

    }

    public void refreshMap(String type) {
//        mClusterManager.clearItems();
        mMap.clear();

        currentFilter= type;

        setupMarkerList(type);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(markList.get(0).getLatitude()),
  //              Double.valueOf(markList.get(0).getLongitude())), 11));

//        MapFragment mf = (MapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        mClusterManager.setRenderer(new InfoRender(getActivity(), mMap, mClusterManager));

    }

    private void setupMarkerList(String type){

        if(markList == null || markList.isEmpty())
            return;

        if(mClusterManager == null)
            return;

        mClusterManager.clearItems();

        for(int i = 0; i<markList.size(); i++) {
            if(type.equals("All") || markList.get(i).getType().equals(type)) {
                if (!markList.get(i).getLatitude().equals("null") && !markList.get(i).getLongitude().equals("null")) {
                    mClusterManager.addItem(new Place(i,
                            Double.valueOf(markList.get(i).getLatitude()),
                            Double.valueOf(markList.get(i).getLongitude()),
                            markList.get(i).getType(),
                            markList.get(i).getName()
                    ));
                }
            }
        }


    }
    private void setUpMap() {
        Log.d("Assi","setUpMap");

        Location currentLocation = getCurrentLocation();
        if (currentLocation != null)
        {
            mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Marker")
                    .snippet("This is the PPN demo"));

            mMap.addMarker(new MarkerOptions()
                    .title("London")
                    .snippet("United Kingdom")
                    .position(new LatLng(51.51, -0.1)));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 9));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(13)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    return;
                }
            });

        }else{
            StringUtil.showAlertDialog("Error", "Could not retrieve your coordinates", getActivity());
        }

    }

    private Location getCurrentLocation(){
        Log.d("MapFragment","getCurrentLocation");

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location;
        if (mGoogleApiClient!=null) {
            location = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }else{
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        }
        //The following code gets the next best location if the current location is unavaiable through standard API
        if (location == null) {
            Criteria criteriaTest = new Criteria();
            criteriaTest.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider = locationManager.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            location = locationManager.getLastKnownLocation(provider);

            if(location == null){
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }

        return location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_map, container, false);
        Log.d("MapFragment","CreateView");
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        infoView = (RelativeLayout) view.findViewById(R.id.infoview);
        mAddressTv = (TextView) view.findViewById(R.id.address);
        mNameTv = (TextView) view.findViewById(R.id.name);
        mContactTv = (TextView) view.findViewById(R.id.contact);
        mPostalTv = (TextView) view.findViewById(R.id.postal);
        mStaffNameTv = (TextView) view.findViewById(R.id.staffname);

//        markList = mActivity.getList();

        if (network()) {
            buildGoogleApiClient();
        }else{
            connectionFailAlert();
            setUpMapIfNeeded();
        }

 //       setUpMapIfNeeded();

        return view;
    }

    private void connectionFailAlert(){
        StringUtil.showAlertDialog("Location Services Failed", "Could not access current location. Using last known location", getActivity());
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
            mActivity = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }


        @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        setUpMapIfNeeded();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        connectionFailAlert();
        setUpMapIfNeeded();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


            SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);

            if(fragment != null)
                getFragmentManager().beginTransaction().remove(fragment).commit();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//
//
//      outState.putParcelableArrayList("List", (ArrayList<? extends Parcelable>) markList);
//        outState.putSerializable("List2", (Serializable) markList);
        outState.putString("filter", currentFilter);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
    }

    private class Place implements ClusterItem {
        private final LatLng mPosition;
        private final String mTitle;
        private final String mSnippet;
        private final int index;

        public Place(int ind, double lat, double lng, String t, String s) {
            index = ind;
            mPosition = new LatLng(lat, lng);
            mTitle = t;
            mSnippet = s;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        public String getTitle(){
            return mTitle;
        }

        public String getSnippet(){
            return mSnippet;
        }

        public int getIndex() { return index; }
    }

    private class InfoRender extends DefaultClusterRenderer<Place> {

        public InfoRender(Context context, GoogleMap map, ClusterManager<Place> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(Place item, MarkerOptions markerOptions) {
//            markerOptions.icon(item.getIcon());
            markerOptions.snippet(item.getSnippet());
            markerOptions.title(item.getTitle());
            super.onBeforeClusterItemRendered(item, markerOptions);
        }
    }
}

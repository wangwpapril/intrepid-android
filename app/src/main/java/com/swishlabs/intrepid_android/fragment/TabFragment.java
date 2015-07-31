package com.swishlabs.intrepid_android.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swishlabs.intrepid_android.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ImageView tab_one;
    ImageView tab_two;
    ImageView tab_three;
    ImageView tab_four;

    ImageView tab_onebg;
    ImageView tab_twobg;
    ImageView tab_threebg;
    ImageView tab_fourbg;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment newInstance(String param1, String param2) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View mainView = inflater.inflate(R.layout.tabsbar, container, false);
        init(mainView);

        return mainView;
    }

    void init(View mainView) {

        this.tab_one = (ImageView) mainView.findViewById(R.id.toolbar_tabone);
        this.tab_onebg = (ImageView) mainView.findViewById(R.id.toolbar_tabonebg);
        this.tab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onFragmentInteraction(0);

                OnTabSelected("tab_one");
            }
        });

        this.tab_two = (ImageView) mainView.findViewById(R.id.toolbar_tabtwo);
        this.tab_twobg = (ImageView) mainView.findViewById(R.id.toolbar_tabtwobg);
        this.tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onFragmentInteraction(1);


                OnTabSelected("tab_two");
            }
        });

        this.tab_three = (ImageView) mainView.findViewById(R.id.toolbar_tabthree);
        this.tab_threebg = (ImageView) mainView.findViewById(R.id.toolbar_tabthreebg);
        this.tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onFragmentInteraction(2);

                OnTabSelected("tab_three");
            }
        });

        this.tab_four = (ImageView) mainView.findViewById(R.id.toolbar_tabfour);
        this.tab_fourbg = (ImageView) mainView.findViewById(R.id.toolbar_tabfourbg);
        this.tab_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onFragmentInteraction(3);

                OnTabSelected("tab_four");
            }
        });

        OnTabSelected("tab_one");
    }

    void OnTabSelected(String tabName) {
        if (tabName == "tab_one") {

/*            if (null == homeFragment)
            {
                homeFragment = new B0_IndexFragment();
            }

            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, homeFragment, "tab_one");
            localFragmentTransaction.commit();

            this.tab_one.setImageResource(R.drawable.footer_home_active_icon);
            this.tab_two.setImageResource(R.drawable.footer_search_icon);
            this.tab_three.setImageResource(R.drawable.footer_shopping_cart_icon);
            this.tab_four.setImageResource(R.drawable.footer_user_icon);*/

 //           this.tab_one.setBackgroundColor(Color.YELLOW);

                this.tab_onebg.setVisibility(View.VISIBLE);
            this.tab_twobg.setVisibility(View.INVISIBLE);
            this.tab_threebg.setVisibility(View.INVISIBLE);
            this.tab_fourbg.setVisibility(View.INVISIBLE);

        }
        else if (tabName == "tab_two")
        {

/*            searchFragment = new D0_CategoryFragment();


            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, searchFragment, "tab_two");
            localFragmentTransaction.commit();


            this.tab_one.setImageResource(R.drawable.footer_home_icon);
            this.tab_two.setImageResource(R.drawable.footer_search_active_icon);
            this.tab_three.setImageResource(R.drawable.footer_shopping_cart_icon);
            this.tab_four.setImageResource(R.drawable.footer_user_icon);*/
            this.tab_onebg.setVisibility(View.INVISIBLE);
            this.tab_twobg.setVisibility(View.VISIBLE);
            this.tab_threebg.setVisibility(View.INVISIBLE);
            this.tab_fourbg.setVisibility(View.INVISIBLE);
        }
        else if (tabName == "tab_three")
        {
/*            shoppingCartFragment = new C0_ShoppingCartFragment();
            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, shoppingCartFragment, "tab_three");
            localFragmentTransaction.commit();

            this.tab_one.setImageResource(R.drawable.footer_home_icon);
            this.tab_two.setImageResource(R.drawable.footer_search_icon);
            this.tab_three.setImageResource(R.drawable.footer_shopping_cart_active_icon);
            this.tab_four.setImageResource(R.drawable.footer_user_icon);*/


            this.tab_onebg.setVisibility(View.INVISIBLE);
            this.tab_twobg.setVisibility(View.INVISIBLE);
            this.tab_threebg.setVisibility(View.VISIBLE);
            this.tab_fourbg.setVisibility(View.INVISIBLE);
        }
        else if (tabName == "tab_four")
        {

/*            profileFragment = new E0_ProfileFragment();
            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, profileFragment, "tab_four");
            localFragmentTransaction.commit();

            this.tab_one.setImageResource(R.drawable.footer_home_icon);
            this.tab_two.setImageResource(R.drawable.footer_search_icon);
            this.tab_three.setImageResource(R.drawable.footer_shopping_cart_icon);
            this.tab_four.setImageResource(R.drawable.footer_user_active_icon);*/


            this.tab_onebg.setVisibility(View.INVISIBLE);
            this.tab_twobg.setVisibility(View.INVISIBLE);
            this.tab_threebg.setVisibility(View.INVISIBLE);
            this.tab_fourbg.setVisibility(View.VISIBLE);

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
/*    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

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
        public void onFragmentInteraction(int position);
    }

}

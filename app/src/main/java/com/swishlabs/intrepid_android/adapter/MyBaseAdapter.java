package com.swishlabs.intrepid_android.adapter;

import com.intrepid.travel.R;
import com.intrepid.travel.ui.activity.BaseActivity;
import com.intrepid.travel.utils.ImageLoader;

import android.view.LayoutInflater;
import android.widget.BaseAdapter;


public abstract class MyBaseAdapter extends BaseAdapter {

	protected BaseActivity context;
	protected LayoutInflater layoutInflater;
	protected ImageLoader ImageLoader;
	protected void init(){
		layoutInflater = LayoutInflater.from(context);
		if (ImageLoader == null) {
			ImageLoader = new ImageLoader(context, R.drawable.ic_launcher);
		}
	}

}

package com.swishlabs.intrepid_android.adapter;


import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import com.swishlabs.intrepid_android.util.ImageLoader;


import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.activity.BaseActivity;

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
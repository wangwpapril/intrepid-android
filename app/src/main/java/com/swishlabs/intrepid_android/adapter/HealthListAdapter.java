package com.swishlabs.intrepid_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.activity.BaseActivity;
import com.swishlabs.intrepid_android.data.api.model.Destination;
import com.swishlabs.intrepid_android.data.api.model.HealthConditionDis;
import com.swishlabs.intrepid_android.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HealthListAdapter extends BaseAdapter {
	private List<HealthConditionDis> datas;
    private List<HealthConditionDis> datas_clone;
    private Filter filter;
    protected Activity context;
    protected ImageLoader ImageLoader;

	public HealthListAdapter(List<HealthConditionDis> datas,
                             Activity activity) {
		this.datas = datas;
        this.datas_clone = datas;
		this.context = activity;
		init();
	}

    protected void init(){
        if (ImageLoader == null) {
            ImageLoader = new ImageLoader(context, R.drawable.empty_square);
        }
    }
	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return datas.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.destination_list_item,
					null);

			holder.countryIcon = (ImageView) convertView
					.findViewById(R.id.country_flag_item_iv);
			holder.countryName = (TextView) convertView
					.findViewById(R.id.country_name);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HealthConditionDis model = datas.get(position);
		holder.countryName.setText(model.getmConditionName());
		final ImageView imageView = holder.countryIcon;
		imageView.setTag(model.getmGeneralImage());
		ImageLoader.DisplayImage(model.getmGeneralImage(), context, imageView);
		convertView.setTag(holder);
		return convertView;
	}

    public Filter getFilter(Activity activity)
    {
        if (filter == null)
        {
            filter = new HealthListFilter(activity, this);
            return filter;
        }

        return filter;
    }

    private class ViewHolder {
		public ImageView countryIcon;
		public TextView countryName;
	}

    private class HealthListFilter extends Filter {

        private Activity mActivity;
        private BaseAdapter mAdapter;

        public HealthListFilter(Activity activity, BaseAdapter adapter)
        {
            mActivity = activity;
            mAdapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase(Locale.getDefault());
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.toString().length() > 0)
            {

                List<HealthConditionDis> filteredDatas = new ArrayList<HealthConditionDis>();

                for (int i=0; i < datas_clone.size(); i++)
                {
                    HealthConditionDis tmpData = datas_clone.get(i);
                    String name = tmpData.getmConditionName();
                    name = name.toLowerCase(Locale.getDefault());

                    if (name.contains(constraint))
                        filteredDatas.add(tmpData);
                }
                results.count = filteredDatas.size();
                results.values = filteredDatas;
            }
            else
            {
                datas = datas_clone;
                synchronized (this) {
                    results.count = datas.size();
                    results.values = datas;
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            datas = (List<HealthConditionDis>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }


        }

    }

}
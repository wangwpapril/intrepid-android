package com.swishlabs.intrepid_android.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import android.app.Activity;
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


public class DestinationsListAdapter extends MyBaseAdapter {
	private List<Destination> datas;
    private List<Destination> datas_clone;
    private Filter filter;

	public DestinationsListAdapter(List<Destination> datas,
                                   BaseActivity context) {
		this.datas = datas;
        this.datas_clone = datas;
		this.context = context;
		super.init();
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

		return datas.get(position).getIndex();

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
		Destination model = datas.get(position);
		holder.countryName.setText(model.name);
		final ImageView imageView = holder.countryIcon;
		imageView.setTag(model.imageFlag.version3.sourceUrl);
		ImageLoader.DisplayImage(model.imageFlag.version3.sourceUrl, context, imageView);
		convertView.setTag(holder);
		return convertView;
	}

    public Filter getFilter(Activity activity)
    {
        if (filter == null)
        {
            filter = new TripListFilter(activity, this);
            return filter;
        }

        return filter;
    }

    private class ViewHolder {
		public ImageView countryIcon;
		public TextView countryName;
	}

    private class TripListFilter extends Filter {

        private Activity mActivity;
        private BaseAdapter mAdapter;

        public TripListFilter(Activity activity, BaseAdapter adapter)
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

                List<Destination> filteredDatas = new ArrayList<Destination>();

                for (int i=0; i < datas_clone.size(); i++)
                {
                    Destination tmpData = datas_clone.get(i);
                    String name = tmpData.name;
                    String altName = tmpData.altName;
                    name = name.toLowerCase(Locale.getDefault());
                    altName = altName.toLowerCase(Locale.getDefault());

                    if (name.contains(constraint)||altName.contains(constraint))
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

            datas = (List<Destination>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }


        }

    }

}
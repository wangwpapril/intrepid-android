package com.swishlabs.intrepid_android.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.data.api.model.HealthConditionDis;
import com.swishlabs.intrepid_android.data.api.model.HealthMedicationDis;
import com.swishlabs.intrepid_android.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HealthMedListAdapter extends BaseAdapter {
	private List<HealthMedicationDis> datas;
    private List<HealthMedicationDis> datas_clone;
    private Filter filter;
    protected Activity context;
    protected ImageLoader ImageLoader;

	public HealthMedListAdapter(List<HealthMedicationDis> datas,
                                Activity activity) {
		this.datas = datas;
        this.datas_clone = datas;
		this.context = activity;
		init();
	}

    protected void init(){
        if (ImageLoader == null) {
            ImageLoader = new ImageLoader(context, R.drawable.ic_launcher);
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
		return position;
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
		HealthMedicationDis model = datas.get(position);
		holder.countryName.setText(model.getmMedicationName());
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

                List<HealthMedicationDis> filteredDatas = new ArrayList<HealthMedicationDis>();

                for (int i=0; i < datas_clone.size(); i++)
                {
                    HealthMedicationDis tmpData = datas_clone.get(i);
                    String name = tmpData.getmMedicationName();
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

            datas = (List<HealthMedicationDis>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }


        }

    }

}
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
import com.swishlabs.intrepid_android.data.api.model.Alert;
import com.swishlabs.intrepid_android.data.api.model.HealthConditionDis;
import com.swishlabs.intrepid_android.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AlertListAdapter extends BaseAdapter {
	private List<Alert> datas;
    protected Activity context;
    protected ImageLoader ImageLoader;

	public AlertListAdapter(List<Alert> datas,
                            Activity activity) {
		this.datas = datas;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.alert_list_item,
					null);

			holder.categoryContent = (TextView) convertView
					.findViewById(R.id.category_content_alert);
			holder.validContent = (TextView) convertView
					.findViewById(R.id.valid_content_alert);
            holder.alertContent = (TextView) convertView
                    .findViewById(R.id.content_alert);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Alert model = datas.get(position);
		holder.categoryContent.setText(model.getCategory());
        holder.validContent.setText(model.getStartDate()+" to "+model.getEndDate());
        holder.alertContent.setText(model.getDescription());
		convertView.setTag(holder);
		return convertView;
	}


    private class ViewHolder {
		public TextView categoryContent;
		public TextView validContent;
        public TextView alertContent;
	}


}
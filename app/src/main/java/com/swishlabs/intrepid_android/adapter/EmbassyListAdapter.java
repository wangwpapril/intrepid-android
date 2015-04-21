package com.swishlabs.intrepid_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.data.api.model.Currency;
import com.swishlabs.intrepid_android.data.api.model.Embassy;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

import java.util.List;

/**
 * Created by ryanracioppo on 2015-04-15.
 */
public class EmbassyListAdapter extends MyBaseAdapter {
    private List<Embassy> mEmbassyList;
    private Context mContext;
    private Database mDatabase;
    private Currency tempCurrency;



    public EmbassyListAdapter(List<Embassy> datas,
                                   Activity context, Database database) {
        this.mEmbassyList = datas;
        this.mContext = context;
        this.mDatabase = database;
        init();
    }

    protected void init(){
        if (ImageLoader == null) {
            ImageLoader = new com.swishlabs.intrepid_android.util.ImageLoader(context, R.drawable.ic_launcher);
        }
        String baseCurrencyCode = SharedPreferenceUtil.getString(Enums.PreferenceKeys.currencyCode.toString(), null);
        tempCurrency = DatabaseManager.getCurrency(baseCurrencyCode, mDatabase);
    }

    @Override
    public int getCount() {
        return mEmbassyList == null ? 0 : mEmbassyList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.destination_list_item,
                    null);

            holder.countryIcon = (ImageView) convertView
                    .findViewById(R.id.country_flag_item_iv);
            holder.countryName = (TextView) convertView
                    .findViewById(R.id.country_name);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Embassy model = mEmbassyList.get(position);
        holder.countryName.setText(model.getCountry());
        final ImageView imageView = holder.countryIcon;

        imageView.setTag(tempCurrency.getImageUrl());
        ImageLoader.DisplayImage(tempCurrency.getImageUrl(), context, imageView);
        convertView.setTag(holder);
        return convertView;
    }


    private class ViewHolder {
        public ImageView countryIcon;
        public TextView countryName;
    }


}

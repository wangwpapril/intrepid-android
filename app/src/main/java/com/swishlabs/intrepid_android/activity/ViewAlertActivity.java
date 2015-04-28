package com.swishlabs.intrepid_android.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.adapter.AlertListAdapter;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.data.api.model.Alert;
import com.swishlabs.intrepid_android.data.api.model.DestinationInformation;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.util.Common;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;


import java.util.List;

public class ViewAlertActivity extends ActionBarActivity {
    public DatabaseManager mDatabaseManager;
    public Database mDatabase;
    String mDestinationId;
    DestinationInformation mDestinationInformation;
    IntrepidMenu mIntrepidMenu;
    protected ListView mAlertListLv;
    protected TextView mListEmpTv;
    protected AlertListAdapter mAlertListAdapter;


    public static ViewAlertActivity instance;
    public List<Alert> mAlertList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        Common.context = this;
        loadDatabase();
        mDestinationId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.currentCountryId.toString(), null);
        mDestinationInformation = DatabaseManager.getDestinationInformation(mDatabase, mDestinationId);

        mAlertList = DatabaseManager.getAlertList(mDatabase, mDestinationInformation.getCountryCode());

        setContentView(R.layout.activity_view_alert);
        mIntrepidMenu = (IntrepidMenu)findViewById(R.id.intrepidMenu);
        mIntrepidMenu.setupMenu(instance,instance);

        mAlertListLv = (ListView) findViewById(R.id.alerts_list);

        mListEmpTv = (TextView) findViewById(R.id.empty_list);

        if(mAlertList.size()>0) {
            mAlertListAdapter = new AlertListAdapter(mAlertList, instance);
            mAlertListLv.setAdapter(mAlertListAdapter);
        }else {
            mAlertListLv.setVisibility(View.GONE);
            mListEmpTv.setVisibility(View.VISIBLE);
        }

    }

    private void loadDatabase() {
        mDatabaseManager = new DatabaseManager(this.getBaseContext());
        mDatabase = mDatabaseManager.openDatabase("Intrepid.db");

    }


}

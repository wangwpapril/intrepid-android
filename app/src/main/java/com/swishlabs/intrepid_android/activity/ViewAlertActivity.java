package com.swishlabs.intrepid_android.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.adapter.AlertListAdapter;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Alert;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.DestinationInformation;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.util.Common;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        fetchAlerts();

        setContentView(R.layout.activity_view_alert);
        mIntrepidMenu = (IntrepidMenu)findViewById(R.id.intrepidMenu);
        mIntrepidMenu.setupMenu(instance,instance);

        mAlertListLv = (ListView) findViewById(R.id.alerts_list);

        mListEmpTv = (TextView) findViewById(R.id.empty_list);

    }

    private void loadDatabase() {
        mDatabaseManager = new DatabaseManager(this.getBaseContext());
        mDatabase = mDatabaseManager.openDatabase("Intrepid.db");

    }

    private void fetchAlerts() {

        IControllerContentCallback icc = new IControllerContentCallback() {

            public void handleSuccess(String content) {
                JSONArray alertArray;
                try {
                    alertArray = new JSONObject(content).getJSONArray("content");
                    int len = alertArray.length();
                    mAlertList = new ArrayList<Alert>(len);
                    SimpleDateFormat to = new SimpleDateFormat("MMM d, yyyy");
                    SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy");

                    for(int i = 0; i < len; i++){
                        JSONObject alertObj = alertArray.getJSONObject(i);
                        String cate = alertObj.optString("category");
                        String desc = alertObj.optString("description");
                        String start = alertObj.optString("start");
                        String end = alertObj.optString("end");
                        try {
                            start = to.format(from.parse(start));
                            end = to.format(from.parse(end));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Alert mAlert = new Alert(cate, desc, start, end);
                        mAlertList.add(mAlert);
                    }

                    if(mAlertList.size()>0) {
                        mAlertListAdapter = new AlertListAdapter(mAlertList, instance);
                        mAlertListLv.setAdapter(mAlertListAdapter);
                    }else {
                        mAlertListLv.setVisibility(View.GONE);
                        mListEmpTv.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void handleError(Exception e){

            }
        };

        String token = SharedPreferenceUtil.getString(Enums.PreferenceKeys.token.toString(), null);

        ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL+"alerts/"+mDestinationInformation.getCountryCode()+"?token="+token, icc,
                Enums.ConnMethod.GET,false);

        String ss = null;
        cct.execute(ss);


    }


}

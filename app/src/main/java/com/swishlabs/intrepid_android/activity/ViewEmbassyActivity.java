package com.swishlabs.intrepid_android.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.data.api.model.Embassy;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;

public class ViewEmbassyActivity extends ActionBarActivity {

    protected Embassy mEmbassy;
    public DatabaseManager mDatabaseManager;
    public Database mDatabase;
    IntrepidMenu mIntrepidMenu;
    ViewEmbassyActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_embassy);
        loadDatabase();
        getEmbassyInfo();
        populateEmbassyInfo();
        instance = this;
        mIntrepidMenu = (IntrepidMenu)findViewById(R.id.intrepidMenu);
        mIntrepidMenu.setupMenu(instance, ViewEmbassyActivity.this);
    }

    public void loadDatabase() {
        mDatabaseManager = new DatabaseManager(this.getBaseContext());
        mDatabase = mDatabaseManager.openDatabase("Intrepid.db");
    }

    protected void getEmbassyInfo(){
        Bundle extras = getIntent().getExtras();
        String embassyId = extras.getString("embassyId");
        mEmbassy = DatabaseManager.getEmbassy(embassyId, mDatabase);
    }

    protected void populateEmbassyInfo(){
        TextView address = (TextView)findViewById(R.id.embassy_address);
        address.setText(mEmbassy.getAddress());
        TextView phone = (TextView)findViewById(R.id.contact_phone);
        phone.setText("Phone: " + mEmbassy.getTelephone());
        TextView fax = (TextView)findViewById(R.id.contact_fax);
        fax.setText("Fax: " + mEmbassy.getFax());
        TextView email = (TextView)findViewById(R.id.contact_email);
        email.setText("E-mail: " + mEmbassy.getEmail());
        TextView hours = (TextView)findViewById(R.id.embassy_hours);
        hours.setText(mEmbassy.getHoursofOperation());
        TextView notes = (TextView)findViewById(R.id.notes_text);
        if (mEmbassy.getNotes().isEmpty()){
            RelativeLayout notesBox = (RelativeLayout)findViewById(R.id.notes);
            notesBox.setVisibility(View.INVISIBLE);
        }else {
            notes.setText(mEmbassy.getNotes());
        }
        TextView services = (TextView)findViewById(R.id.services_text);
        if (mEmbassy.getServicesOffered().isEmpty()){
            RelativeLayout servicesBox = (RelativeLayout)findViewById(R.id.services);
            servicesBox.setVisibility(View.INVISIBLE);
        }else {
            services.setText(mEmbassy.getServicesOffered());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_embassy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

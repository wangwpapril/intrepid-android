package com.swishlabs.intrepid_android.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

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

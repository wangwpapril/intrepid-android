package com.swishlabs.intrepid_android.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.adapter.DestinationsListAdapter;
import com.swishlabs.intrepid_android.data.api.callback.ControlerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControlerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.Destination;
import com.swishlabs.intrepid_android.data.api.model.Image;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LegalActivity extends BaseActivity {

    private String data = null;
    private TextView legal = null;
    private ImageView cancel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal_layout);
/*        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        legal = (TextView) findViewById(R.id.legal_content);
        cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        fetchLegal();
    }

    private void fetchLegal() {
        IControlerContentCallback icc = new IControlerContentCallback() {
            public void handleSuccess(String content){

                JSONObject company;
                try {
                    company = new JSONObject(content).getJSONObject("company");
                    data = company.getString("legal");
                    legal.setText(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void handleError(Exception e){

                return;

            }
        };


        ControlerContentTask cct = new ControlerContentTask(
                Constants.BASE_URL+"companies/52525252/legal", icc,
                Enums.ConnMethod.GET,false);
        String ss = null;
        cct.execute(ss);

    }


    @Override
    public void onClick (View view) {
        if(view == cancel){

            this.finish();
        }
    }

    @Override
    protected void initTitle() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_legal, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_legal, container, false);
            return rootView;
        }
    }
}

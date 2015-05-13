package com.swishlabs.intrepid_android.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.data.api.model.Embassy;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;

public class ViewEmbassyActivity extends ActionBarActivity {

    protected Embassy mEmbassy;
    public DatabaseManager mDatabaseManager;
    public Database mDatabase;
    ViewEmbassyActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_view_embassy);
        loadDatabase();
        getEmbassyInfo();
        populateEmbassyInfo();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        instance = this;
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
        String formattedPhones = formatPhoneNumbers();
        phone.setText(Html.fromHtml("Phone: <u><font color=blue>"+ formattedPhones+"</u></style>"));
        TextView fax = (TextView)findViewById(R.id.contact_fax);
        fax.setText("Fax: " + mEmbassy.getFax());
        TextView email = (TextView)findViewById(R.id.contact_email);
        String formattedEmails = formatEmails();
        email.setText(Html.fromHtml("Email: <u><font color=blue>"+ formattedEmails+"</u></style>"));
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
        setClickListeners(phone, email);

        ImageView backIv = (ImageView) findViewById(R.id.title_back);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                instance.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    protected String formatPhoneNumbers(){
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.BELOW, R.id.below_id);
        String rawPhones = mEmbassy.getTelephone();
        String formattedPhones = rawPhones.replace(", ", "\r\n").replace("; ", "\r\n");
        return formattedPhones;
    }

    protected String formatEmails(){
        String rawEmails = mEmbassy.getEmail();
        String formattedEmails = rawEmails.replace(", ", "\r\n").replace("; ", "\r\n");
        return formattedEmails;
    }

    protected void setClickListeners(TextView phone, TextView email){
        Linkify.addLinks(phone, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(email, Linkify.EMAIL_ADDRESSES);
//        phone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String phoneNumber = mEmbassy.getTelephone();
//                Intent call = new Intent(Intent.ACTION_DIAL);
//                call.setData(Uri.parse("tel:" + phoneNumber));
//                startActivity(call);
//            }
//        });
//        email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmbassy.getEmail()});
//                i.putExtra(Intent.EXTRA_SUBJECT, "");
//                i.putExtra(Intent.EXTRA_TEXT   , "");
//                try {
//                    startActivity(Intent.createChooser(i, "Send E-mail"));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(ViewEmbassyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
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

package com.swishlabs.intrepid_android.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

public class AceInsuranceActivity extends ActionBarActivity implements View.OnClickListener {

    IntrepidMenu mIntrepidMenu;
    Button aceViewBt, pdfViewBt;
    public static AceInsuranceActivity instance;
    private String mVMPdfUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_ace_insurance);
        mIntrepidMenu = (IntrepidMenu) findViewById(R.id.intrepidMenu);
        mIntrepidMenu.setupMenu(this, this);
        aceViewBt = (Button) findViewById(R.id.butAceView);
        aceViewBt.setOnClickListener(this);
        pdfViewBt = (Button) findViewById(R.id.butPdf);
        pdfViewBt.setOnClickListener(this);

        mVMPdfUrl = SharedPreferenceUtil.getString(Enums.PreferenceKeys.virtualWalletPdf.toString(), null);
        if(mVMPdfUrl == null){
            pdfViewBt.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == aceViewBt) {
            Intent mIntent = new Intent(instance, ViewAceActivity.class);
            instance.startActivity(mIntent);

        } else if (v == pdfViewBt) {
            Intent mIntent = new Intent(instance, ViewVMPdfActivity.class);
            mIntent.putExtra("link", mVMPdfUrl);
            instance.startActivity(mIntent);

        }
    }
}

package com.swishlabs.intrepid_android.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;

public class DetailHealthMedActivity extends ActionBarActivity {

    private String mName, mBrandName, mDescription, mSideEffects, mStorage, mNotes;

    private TextView mTitleTv, mBrandTv, mDesTv, mSideETv, mStorageTv, mNotesTv;
    private ImageView mBackIv;

    public DetailHealthMedActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_health_med);
        instance = this;
        mName = getIntent().getStringExtra("name");
        mBrandName = getIntent().getStringExtra("brand name");
        mDescription = getIntent().getStringExtra("description");
        mSideEffects = getIntent().getStringExtra("side effects");
        mStorage = getIntent().getStringExtra("storage");
        mNotes = getIntent().getStringExtra("notes");

        initialView();

    }


    protected void initialView(){
        mTitleTv = (TextView) findViewById(R.id.toolbar_title);
        mTitleTv.setText(mName);

        mBrandTv = (TextView) findViewById(R.id.brand_name_content);
        mBrandTv.setText(mBrandName);

        mDesTv = (TextView)findViewById(R.id.destination_content);
        mDesTv.setText(mDescription);

        mSideETv = (TextView)findViewById(R.id.destination_content2);
        mSideETv.setText(mSideEffects);

        mStorageTv = (TextView)findViewById(R.id.destination_content3);
        mStorageTv.setText(mStorage);

        mNotesTv = (TextView) findViewById(R.id.destination_content4);
        mNotesTv.setText(mNotes);

        mBackIv = (ImageView)findViewById(R.id.title_back);
        mBackIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        instance.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

}

package com.swishlabs.intrepid_android.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;

public class DetailHealthConActivity extends ActionBarActivity {

    private String name, des, symp, prev;

    private TextView title, description,symptoms,prevention;
    private ImageView back;

    public DetailHealthConActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_health_con);
        instance = this;
        name = getIntent().getStringExtra("name");
        des = getIntent().getStringExtra("description");
        symp = getIntent().getStringExtra("symptoms");
        prev = getIntent().getStringExtra("prevention");

        initialView();

    }


    protected void initialView(){
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(name);

        description = (TextView)findViewById(R.id.destination_content);
        description.setText(des);

        symptoms = (TextView)findViewById(R.id.destination_content2);
        symptoms.setText(symp);

        prevention = (TextView)findViewById(R.id.destination_content3);
        prevention.setText(prev);

        back = (ImageView)findViewById(R.id.title_back);
        back.setOnClickListener( new View.OnClickListener() {
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

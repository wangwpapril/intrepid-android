package com.swishlabs.intrepid_android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.util.Enums;
import org.json.JSONException;
import org.json.JSONObject;

public class LegalActivity extends BaseActivity {

    private String data = null;
    private TextView legal = null;
    private ImageView cancel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal_layout);
        legal = (TextView) findViewById(R.id.legal_content);
        cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        fetchLegal();
    }

    private void fetchLegal() {
        IControllerContentCallback icc = new IControllerContentCallback() {
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

        ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL+"companies/52525252/legal", icc,
                Enums.ConnMethod.GET,false);
        String ss = null;
        cct.execute(ss);
    }

    @Override
    public void onClick (View view) {
        if(view == cancel){
            this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            this.finish();
        }
    }

    @Override
    protected void initTitle() {
    }

}
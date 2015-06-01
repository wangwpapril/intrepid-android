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
import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
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

        legal = (TextView) findViewById(R.id.legal_content);
        cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        data = "YOUR USE OF THIS APP IS SUBJECT TO LEGALLY BINDING TERMS AND CONDITIONS. PLEASE READ THE FOLLOWING TERMS AND CONDITIONS CAREFULLY BEFORE USING THIS APP.\n\n The app is owned by Ingle International Inc. operating as Intrepid 24/7™ (collectively, “Intrepid 24/7”). This app is designed to provide health, security, and other travel-related information, as well as access to emergency assistance services offered by Intrepid 24/7.\n\n Disclaimer\n\n Content on the app is provided on an 'as is' and 'as available' basis with no warranties whatsoever. Intrepid 24/7 expressly disclaims all warranties of any kind, whether express or implied, including, but not limited to, the implied warranties of merchantability, fitness for a particular purpose, and noninfringement of third parties' rights. Without limiting the foregoing, Intrepid 24/7 and its licensors make no representations or warranties that: (a) the app will meet your requirements; (b) the app will function on an uninterrupted, timely, secure, or error-free basis; (c) the results that may be obtained from the use of the app (including any results obtained from any diagnosis or assessment tools) will be the desired results, or any information, products, or services offered through the app or otherwise will be accurate, complete, current or reliable; (d) any laws or government regulations requiring disclosure of information on  health care products have been satisfied;  (e) content provided by third parties complies with privacy, official languages or accessibility laws, regulations or requirements; or (f) the products, services, information or other materials obtained through the app or any link or third party website accessed through the app will meet your needs or expectations.\n\n Limitation of Liability\n\n In no event shall Intrepid 24/7 be liable to you or third parties for misuse of the app or reliance on content or other materials accessible on or via the app emanating from external sources or third parties. You should verify information from external sources and third parties directly with the source before relying on it.\n\n In no event shall Intrepid 24/7 be liable to you or any person with respect to damages of any kind arising from or relating to any services or goods advertised on the app or accessed through any links provided in the app should the services or goods be unavailable or inaccessible for any unforeseeable reason.\n\n Intrepid 24/7 has no liability to you or to any person for any personal injury (including wrongful death) purported to have been causedby, or resulting from, your use or misuse of the app or the performance or non-performance of the app.  This app should not be relied upon in emergency or life-threatening or mission critical conditions and Intrepid 24/7 disclaims any liability on its own behalf for damages resulting from such reliance or failure of the app to perform or be available to you under these circumstances.\n\n Refer to the manufacturer’s product warranty for information on repair or replacement of your equipment.  Intrepid 24/7 disclaims liability for and waives any indirect, economic, special, commercial, incidental, exemplary or consequential damages (including, without limitation, lost profits, loss of business, lost data, damages caused by delays or a failure to realize earnings and the like and personal injury or wrongful death damages) arising from or in connection with use or performance or lack of performance of the equipment.\n\nWithout limiting the preceding paragraphs of this section, the limit on the total cumulative liability of Intrepid 24/7 to you or any person for any claims arising out of or relating to your use of the app, the content or this agreement shall not in any event exceed $100.00 CAD. \n\n The limitations specified above shall apply regardless of the causes or circumstances giving rise to the claim, even if the claim is brought in an action or claim (sounding in tort including negligence) or based on breach of contract or under warranty or under any law or form of action.\n\n Privacy\n\n No personal information is collected about you on this app except where you voluntarily disclose it.\n\n When you voluntarily disclose your personal information on this app, it is being collected for the sole purpose of communicating with you to respond to a request or to provide information. Intrepid 24/7 cannot sell, rent or lease your personal information to a third party. Intrepid 24/7 may only access and use your personal information for the purpose for which the personal information is collected or to investigate a breach of this Agreement, but cannot disclose your personal information to an affiliate or a third party without your consent unless required by law or regulation. We may disclose your personal information to law enforcement agencies, government employees, regulators or licensed professionals where we have reasonable grounds to believe that disclosure is necessary to prevent or investigate a contravention of a law or to protect the life, health, or security of you or any person. Intrepid 24/7 may not disclose your personal information to a successor or assignee without your consent.\n\n Intrepid 24/7 is committed to protecting the security of your personal information. Intrepid 24/7 uses a variety of security and information technologies and procedures to protect your personal information from unauthorized access, use or disclosure.\n\n Intellectual Property Notice\n\n The contents of the app, including all information, links, reports, data, databases, tools, code, diagrams, photographs, pictures, video, files, graphics, interfaces, web pages, text, files, software, product names, company names, trademarks, service marks, common law marks including foreign language equivalents, logos, trade names, business names, domain names, slogans, designs, copyrights, patents, business method patents, concepts, innovations and other materials and processes comprising the content (collectively, the 'Content'), including the manner in which the Content is presented or appears and all proprietary know-how relating thereto, are the property of their respective owners as indicated, Intrepid 24/7, or their licensors, as the case may be. Except as expressly provided, you are not granted any rights in or to the Content available on or via the app.  Intellectual property ownership rights remain with, and are reserved by, their respective owners and their licensees, if any.  Permission to use all or part of the Content should be sought directly from the owners of the intellectual property rights for the relevant Content.\n\n You are authorized to view and download the information on the app for your personal, noncommercial use as long as  copyright and other proprietary rights notices are included in the Content downloaded. No other use of the Content (including linking to the Content from an external site) is permitted without Intrepid 24/7's prior written consent.";
//        fetchLegal();
        legal.setText(data);
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

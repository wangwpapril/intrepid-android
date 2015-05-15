package com.swishlabs.intrepid_android.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.Country;
import com.swishlabs.intrepid_android.data.api.model.User;
import com.swishlabs.intrepid_android.services.LocationService;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;
import com.swishlabs.intrepid_android.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends BaseActivity {

	protected static final String TAG = "SettingsActivity";
	private EditText etFirstName;
	private EditText etLastName;
	private EditText etEmail;
	private EditText etUserName;
	private EditText etOldPassword;
	private EditText etNewPassword;
	private Button btnUpdate;
	private TextView signOut;
    private IntrepidMenu mIntrepidMenu;
	
	private String firstName = null;
	private String lastName = null;
	private String email = null;
	private String userName = null;
	private String oldPassword = null;
	private String newPassword = null;
	private String userId = null;
    private String token = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        initUserData();
		this.setContentView(R.layout.settings_layout);
		initView();
        initTitle();
	}

    private void initUserData() {
        firstName = SharedPreferenceUtil.getString(Enums.PreferenceKeys.firstname.toString(),"");
        lastName = SharedPreferenceUtil.getString(Enums.PreferenceKeys.lastname.toString(),"");
        email = SharedPreferenceUtil.getString(Enums.PreferenceKeys.email.toString(),"");
        userName = SharedPreferenceUtil.getString(Enums.PreferenceKeys.username.toString(),"");
        userId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.userId.toString(),"");
        token = SharedPreferenceUtil.getString(Enums.PreferenceKeys.token.toString(),"");

    }


    private void initView() {
		super.initTitleView();
		etFirstName = (EditText) findViewById(R.id.firstNameEditText);
        etFirstName.setText(firstName);
		etLastName = (EditText) findViewById(R.id.lastNameEditText);
        etLastName.setText(lastName);
		etEmail = (EditText) findViewById(R.id.emailEditText);
        etEmail.setText(email);
		etUserName = (EditText) findViewById(R.id.userNameEditText);
        etUserName.setText(userName);
		etOldPassword = (EditText) findViewById(R.id.oldPasswordEditText);
		etNewPassword = (EditText) findViewById(R.id.newPasswordEditText);
		btnUpdate = (Button) findViewById(R.id.butUpdate);
        btnUpdate.setOnClickListener(this);
		signOut = (TextView) findViewById(R.id.sign_out);
		signOut.setOnClickListener(this);
		signOut.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        mIntrepidMenu = (IntrepidMenu) findViewById(R.id.intrepidMenu);
        mIntrepidMenu.setupMenu(this, this, true);

	}
	
	@Override
	protected void initTitle() {

	}

	@Override
	public void onClick(View v) {
		if (v == signOut) {

            signOut();
		} else if (v == btnUpdate) {

			firstName = etFirstName.getText().toString();
			lastName = etLastName.getText().toString();
			email = etEmail.getText().toString();
			userName = etUserName.getText().toString();
			oldPassword = etOldPassword.getText().toString();
			newPassword = etNewPassword.getText().toString();

			if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
					TextUtils.isEmpty(email) ||
					TextUtils.isEmpty(userName)) {
                StringUtil.showAlertDialog(getResources().getString(
                        R.string.settings_title_name), getResources()
                        .getString(R.string.input_empty_error), this);

                return;
			}

            if(!StringUtil.isEmail(email)){
                StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name),
                        getResources().getString(R.string.email_format_error),this);
				return;
			}

            if(userName.length()<3){
                StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name),
                        getResources().getString(R.string.username_length_error),this);
                return;

            }

            if(oldPassword.length()>0 || newPassword.length()>0) {
                if (oldPassword.length() < 7 || newPassword.length() <7) {
                    StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name),
                            getResources().getString(R.string.password_length_error), this);
                    return;

                } else {
                    resetPassword();
                }
            }else {
                updateProfile();
            }

		}

	}

    private void signOut() {

        SharedPreferenceUtil.setString(Enums.PreferenceKeys.userId.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.token.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.email.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.firstname.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.lastname.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.username.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.countryCode.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.currencyCode.toString(), "");
        SharedPreferenceUtil.setBoolean(getApplicationContext(), Enums.PreferenceKeys.loginStatus.toString(), false);
        SharedPreferenceUtil.setApList(getApplicationContext(), null);
        MyApplication.setLoginStatus(false);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(TripPagesActivity.pendingIntent);

        mDatabaseManager.deleteDatabase("Intrepid.db");

        Intent mIntent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(mIntent);
        SettingsActivity.this.finish();


    }

    private void resetPassword() {
        IControllerContentCallback icc = new IControllerContentCallback() {
            public void handleSuccess(String content){

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(content);
                    if(jsonObj.has("error")) {
                        JSONArray errorMessage = jsonObj.getJSONObject("error").getJSONArray("message");
                        String message = String.valueOf((Object) errorMessage.get(0));
                        StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name), message, context);
                        return;

                    }else if(jsonObj.has("success")) {
//                       StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name), jsonObj.getString("success"), context);
                        updateProfile();
                        return;
                    }else {
                        StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name), "Password reset failed, Please try it again", context);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void handleError(Exception e){
                StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name), "Password reset failed, Please try it again", context);

                return;

            }
        };

        ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL+"users/"+userId+"/resetPassword?token="+token, icc,
                Enums.ConnMethod.POST,false);

        JSONObject user = new JSONObject();
        try {
            user.put("new_password", newPassword);
            user.put("old_password", oldPassword);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JSONObject reset = new JSONObject();
        try {
            reset.put("user", user);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        cct.execute(reset.toString());

    }


    private void updateProfile() {
		
		IControllerContentCallback icc = new IControllerContentCallback() {
			public void handleSuccess(String content){

				JSONObject jsonObj = null, userObj = null;
				User user = null;

                try {
					jsonObj = new JSONObject(content);
                    if(jsonObj.has("error")) {
                        JSONArray errorMessage = jsonObj.getJSONObject("error").getJSONArray("message");
                        String message = String.valueOf((Object) errorMessage.get(0));
                        StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name), message, context);
                        return;

                    }else if(jsonObj.has("user")) {
//                        userObj = jsonObj.getJSONObject("user");
//                        user = new User(userObj);

                        StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name), "Your profile has been updated.", context);
                        return;

                    }else {
                        StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name), "Update failed, Please try it again", context);
                        return;
                    }
				} catch (JSONException e) {
					e.printStackTrace();
                    StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name), "Update failed, Please try it again", context);
                    return;
				}

			}

			public void handleError(Exception e){
                StringUtil.showAlertDialog(getResources().getString(R.string.settings_title_name), "Update failed, Please try it again", context);

                return;

			}
		};
		
		ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL+"users/"+userId+"?token="+token, icc,
				Enums.ConnMethod.PUT,false);

		JSONObject user = new JSONObject();
		try {
			user.put("email", email);
			user.put("first_name", firstName);
			user.put("last_name", lastName);
			user.put("username", userName);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JSONObject update = new JSONObject();
		try {
			update.put("user", user);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		cct.execute(update.toString());
        Log.d("updateProfile data", update.toString());

	}

    @Override
    public void onBackPressed(){
        if(mIntrepidMenu.mState == 1){
            mIntrepidMenu.snapToBottom();
            return;
        }
        super.onBackPressed();
        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

}

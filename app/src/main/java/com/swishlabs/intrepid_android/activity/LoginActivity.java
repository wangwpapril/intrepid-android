package com.swishlabs.intrepid_android.activity;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.data.api.callback.ControlerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControlerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.User;
import com.swishlabs.intrepid_android.data.store.beans.UserTable;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends BaseActivity {

	protected static final String TAG = "LoginActivity";
	
	private Button imBtnSignIn;
	private EditText editTextEmail, editTextPassword;
	private TextView signUp,learnMore,termsUse;


    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_layout);
		MyApplication.getInstance().addActivity(this);

		initView();
	}

	private void initView(){
		super.initTitleView();
		imBtnSignIn = (Button) findViewById(R.id.butSignIn);
		editTextEmail = (EditText) findViewById(R.id.signinEmailEditText);
		editTextPassword = (EditText) findViewById(R.id.signinPasswordEditText);
		signUp = (TextView) findViewById(R.id.sign_up);
        signUp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        learnMore = (TextView) findViewById(R.id.learnMore);
        learnMore.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        termsUse = (TextView) findViewById(R.id.termsofuse);
        termsUse.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        imBtnSignIn.setOnClickListener(this);
		editTextPassword.setTransformationMethod(PasswordTransformationMethod
				.getInstance());


	}

	@Override
	protected void initTitle(){
		tvTitleName.setText(R.string.login_title_name);
		ivTitleBack.setVisibility(View.VISIBLE);
//		tvTitleRight.setVisibility(View.VISIBLE);
//		tvTitleRight.setText(R.string.login_title_register);
//		tvTitleRight.setOnClickListener(this);
		ivTitleBack.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v){
		if (v == imBtnSignIn) {			

			String email = editTextEmail.getText().toString();
			String password = editTextPassword.getText().toString();
			if (TextUtils.isEmpty(email)) {
				showAlertDialog(getResources().getString(
						R.string.login_title_name), getResources()
						.getString(R.string.login_email_input_error));
				return;
			}

			if (TextUtils.isEmpty(password)) {
				showAlertDialog(getResources().getString(
						R.string.login_title_name), getResources()
						.getString(R.string.login_password_null));
				return;
			}
			
			IControlerContentCallback icc = new IControlerContentCallback() {
				public void handleSuccess(String content){

					JSONObject jsonObj = null, userObj = null;
					User user = null;
					
					try {
						jsonObj = new JSONObject(content);
						userObj = jsonObj.getJSONObject("user");
						user = new User(userObj);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
		            
		            UserTable.getInstance().saveUser(user);
		            User ww = null;
		            ww = UserTable.getInstance().getUser(user.id);

		            SharedPreferenceUtil.setString(Enums.PreferenceKeys.userId.toString(), user.id);
		    		SharedPreferenceUtil.setString(Enums.PreferenceKeys.token.toString(), user.token);
		    		SharedPreferenceUtil.setBoolean(getApplicationContext(), Enums.PreferenceKeys.loginStatus.toString(), true);
		            
					Intent mIntent = new Intent(LoginActivity.this,DestinationsListActivity.class);
					startActivity(mIntent);
					LoginActivity.this.finish();

				}

				public void handleError(Exception e){
					showAlertDialog(getResources().getString(
							R.string.login_title_name), "Invalid login credentials");
					return;

				}
			};
			
			ControlerContentTask cct = new ControlerContentTask(
					Constants.BASE_URL+"users/login", icc,
					Enums.ConnMethod.POST,false);

			JSONObject user = new JSONObject();
			try {
				user.put("email", email);
				user.put("password", password);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			JSONObject login = new JSONObject();
			try {
				login.put("user", user);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			cct.execute(login.toString());


		} else if (v == signUp) {
			

	} else if (v == ivTitleBack) {
		onBackPressed();
	}

	
	}
	
	private void showAlertDialog(String strTitle, String strMessage) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(strMessage);
		builder.setTitle(strTitle);
		builder.setPositiveButton(R.string.confirm_button,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog alertDialog = builder.create();

		alertDialog.setCancelable(false);

		alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return false; 
				}
			}
		});

		alertDialog.show();
	}

}

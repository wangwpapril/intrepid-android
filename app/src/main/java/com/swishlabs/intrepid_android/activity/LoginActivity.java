package com.swishlabs.intrepid_android.activity;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.User;
import com.swishlabs.intrepid_android.data.store.beans.UserTable;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;
import com.swishlabs.intrepid_android.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends BaseActivity {

	protected static final String TAG = "LoginActivity";
	
	private Button loginBtn;
	private EditText emailTextField, passwordTextField;
	private TextView signUp,learnMore,termsOfUseBtn;


    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_layout);
		MyApplication.getInstance().addActivity(this);

		initView();
	}

	private void initView(){
		super.initTitleView();
        loginBtn = (Button) findViewById(R.id.butSignIn);
        emailTextField = (EditText) findViewById(R.id.signinEmailEditText);
        passwordTextField = (EditText) findViewById(R.id.signinPasswordEditText);
		signUp = (TextView) findViewById(R.id.sign_up);
        signUp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        signUp.setOnClickListener(this);

        learnMore = (TextView) findViewById(R.id.learnMore);
        learnMore.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        learnMore.setOnClickListener(this);

        termsOfUseBtn = (TextView) findViewById(R.id.termsofuse);
        termsOfUseBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        termsOfUseBtn.setOnClickListener(this);

        loginBtn.setOnClickListener(this);
        passwordTextField.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
	}

	@Override
	protected void initTitle(){
	}

    protected void getAssistanceProvider(Object obj){
//        JSONObject jsonData = (JSONObject)obj;
//        String test = "";
    }
	

	@Override
	public void onClick(View v) {
        if (v == loginBtn) {

            String email = emailTextField.getText().toString();
            String password = passwordTextField.getText().toString();
            if (TextUtils.isEmpty(email)) {
                StringUtil.showAlertDialog(getResources().getString(
                        R.string.login_title_name), getResources()
                        .getString(R.string.login_email_input_error), this);
                return;
            } else if (!StringUtil.isEmail(email)) {
                StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name),
                        getResources().getString(R.string.email_format_error), this);
                return;
            }

            if (TextUtils.isEmpty(password)) {
                StringUtil.showAlertDialog(getResources().getString(
                        R.string.login_title_name), getResources()
                        .getString(R.string.login_password_null), this);
                return;
            }

            IControllerContentCallback icc = new IControllerContentCallback() {
                public void handleSuccess(String content) {

                    JSONObject jsonObj = null, userObj = null;
                    User user = null;

                try {
//                        jsonObj = new JSONObject(content);
//                        userObj = jsonObj.getJSONObject("user");
//                        user = new User(userObj);
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }

                    jsonObj = new JSONObject(content);
                    if(jsonObj.has("error")) {
                        JSONArray errorMessage = jsonObj.getJSONObject("error").getJSONArray("message");
                        String message = String.valueOf((Object) errorMessage.get(0));
                        StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name), message, context);
                        return;

                    }else if(jsonObj.has("user")) {
                        userObj = jsonObj.getJSONObject("user");
                        user = new User(userObj);
                        String virtualWalletPdf = null;
                        if(userObj.has("company")){
                            Object temp = userObj.getJSONObject("company").get("content");
                            getAssistanceProvider(temp);
                            if(temp instanceof JSONObject){
                                virtualWalletPdf = ((JSONObject) temp).optString("virtual_wallet_pdf");

                            }
                        }

                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.virtualWalletPdf.toString(), virtualWalletPdf);

                     }else {
                        StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name), getResources().getString(R.string.login_failed), context);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name), getResources().getString(R.string.login_failed), context);
                    return;
                }

                if(user != null) {
                        UserTable.getInstance().saveUser(user);
//                        User ww = null;
//                        ww = UserTable.getInstance().getUser(user.id);

                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.userId.toString(), user.id);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.token.toString(), user.token);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.email.toString(),user.email);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.firstname.toString(),user.firstName);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.lastname.toString(),user.lastName);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.username.toString(),user.userName);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.countryCode.toString(),user.countryCode);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.currencyCode.toString(),user.currencyCode);
                        SharedPreferenceUtil.setBoolean(getApplicationContext(), Enums.PreferenceKeys.loginStatus.toString(), true);

                        MyApplication.setLoginStatus(true);

                        Intent mIntent = new Intent(LoginActivity.this, TripPagesActivity.class);
                        startActivity(mIntent);
                        LoginActivity.this.finish();
                    } else {
                        StringUtil.showAlertDialog(getResources().getString(
                                R.string.login_title_name), "User is empty", context);
                        return;
                    }

                }

                public void handleError(Exception e) {
                    StringUtil.showAlertDialog(getResources().getString(
                            R.string.login_title_name), getResources().getString(R.string.login_failed), context);
                    return;

                }
            };

            ControllerContentTask cct = new ControllerContentTask(
                    Constants.BASE_URL + "users/login", icc,
                    Enums.ConnMethod.POST, false);

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
            Intent mIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(mIntent);

        } else if (v == termsOfUseBtn) {
            Intent mIntent = new Intent(LoginActivity.this, LegalActivity.class);
            startActivity(mIntent);

        } else if (v == learnMore){
            Intent mIntent = new Intent(LoginActivity.this, LearnMoreActivity.class);
            startActivity(mIntent);

        }

    }


}
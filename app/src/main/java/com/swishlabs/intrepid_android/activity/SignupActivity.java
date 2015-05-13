package com.swishlabs.intrepid_android.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
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

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Country;
import com.swishlabs.intrepid_android.data.api.model.User;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.StringUtil;


public class SignupActivity extends BaseActivity {

	protected static final String TAG = "SignupActivity";
	private EditText signupFirstName;
	private EditText signupLastName;
	private EditText signupEmail;
	private AutoCompleteTextView signupCountry;
	private EditText signupUserName;
	private EditText signupPassword;
	private EditText signupPolicyNumber;
	private Button btnSignUp;
	private TextView termsOfUseBtn;
	
	private List<Country> countryList;
	
	private ArrayAdapter countryAdapter = null;
	private String countryCode = null;
	private String companyId = null;
	private String firstName = null;
	private String lastName = null;
	private String email = null;
	private String country = null;
	private String userName = null;
	private String password = null;
	private String policyNumber = null;
	private String activationCode = null;
    private String token = null;
	private List<String> countryNames = null, countryCodes = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fetchCountries();
		this.setContentView(R.layout.signup_layout);
		initView();
	}
	
	private void fetchCountries() {
		
		IControllerContentCallback icc = new IControllerContentCallback() {
			public void handleSuccess(String content) {
				JSONObject countries = null;
				try {
					countries = new JSONObject(content);
					JSONArray array = countries.getJSONArray("countries");
					int len = array.length();
					countryList = new ArrayList<Country>(len);
					for (int i =0;i < len; i++){
						countryList.add(new Country(array.getJSONObject(i)));
					}

					countryNames = new ArrayList<String>();
					countryCodes = new ArrayList<String>();
					for (Country country : countryList) {
						countryNames.add(country.name);
						countryCodes.add(country.countryCode);
					}
					countryAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, countryNames);
//					countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    signupCountry.setAdapter(countryAdapter);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

			public void handleError(Exception e) {
				return;
			}
		};
		ControllerContentTask cct = new ControllerContentTask(
				"https://staging.intrepid247.com/v1/countries", icc,
				Enums.ConnMethod.GET, false);

		String ss = null;
		cct.execute(ss);		
	}


	private void initView() {
		super.initTitleView();
        signupFirstName = (EditText) findViewById(R.id.firstNameEditText);
        signupLastName = (EditText) findViewById(R.id.lastNameEditText);
        signupEmail = (EditText) findViewById(R.id.emailEditText);
        signupCountry = (AutoCompleteTextView) findViewById(R.id.countryEditText);
        signupUserName = (EditText) findViewById(R.id.userNameEditText);
        signupPassword = (EditText) findViewById(R.id.PasswordEditText);
        signupPolicyNumber = (EditText) findViewById(R.id.policyNumberEditText);
        signupUserName = (EditText) findViewById(R.id.userNameEditText);
		btnSignUp = (Button) findViewById(R.id.butSignUp);
		btnSignUp.setOnClickListener(this);
        termsOfUseBtn = (TextView) findViewById(R.id.termsofUse);
        termsOfUseBtn.setOnClickListener(this);
        termsOfUseBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);


        signupCountry.setAdapter(countryAdapter);
        signupCountry.setEnabled(true);
//		etCountry.setDropDownBackgroundResource(R.drawable.login_btn);

        signupCountry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                signupCountry.showDropDown();

			}
			
		});

        signupCountry.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				countryCode = countryAdapter.getItem(position).toString();
				countryCode = countryCodes.get(position);
//				etCountry.setEnabled(false);
				
			}
			
		});
	}
	
	@Override
	protected void initTitle() {
	}

	@Override
	public void onClick(View v) {
		if (v == signupCountry) {
            signupCountry.setEnabled(true);

		} else if (v == btnSignUp) {

			firstName = signupFirstName.getText().toString();
			lastName = signupLastName.getText().toString();
			email = signupEmail.getText().toString();
			country = signupCountry.getText().toString();
			userName =signupUserName.getText().toString();
			password = signupPassword.getText().toString();
			policyNumber = signupPolicyNumber.getText().toString();
			
			if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
					TextUtils.isEmpty(email) || TextUtils.isEmpty(country) ||
					TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) ||
					TextUtils.isEmpty(policyNumber)) {
                StringUtil.showAlertDialog(getResources().getString(
                        R.string.signup_title_name), getResources()
                        .getString(R.string.input_empty_error), this);

                return;
			}

            if(!StringUtil.isEmail(email)){
                StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name),
                        getResources().getString(R.string.email_format_error),this);
				return;
			}

            if(userName.length()<3){
                StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name),
                        getResources().getString(R.string.username_length_error),this);
                return;

            }

            if(password.length()<7){
                StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name),
                        getResources().getString(R.string.password_length_error),this);
                return;

            }
			
			checkGroupNumber();

		} else if (v == termsOfUseBtn) {
			Intent i = new Intent();
			i.setClass(context, LegalActivity.class);
			context.startActivity(i);
			this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		} else if (v == ivTitleBack) {
			this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			context.finish();
		}

	}


	private void checkGroupNumber() {

		IControllerContentCallback icc = new IControllerContentCallback() {
			public void handleSuccess(String content) {
				JSONObject temp = null,company = null;
				try {
                    temp = new JSONObject(content);
                    if(temp.has("company")) {
                        company = new JSONObject(content).getJSONObject("company");
                        companyId = company.getString("id");
                        signUp();
                    }else {
                        StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name), "Policy Number is incorrect!", context);
                        return;

                    }
					
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

			public void handleError(Exception e) {
                StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name), "Policy Number is incorrect!", context);
                return;
			}
		};
		ControllerContentTask cct = new ControllerContentTask(
				"https://staging.intrepid247.com/v1/companies/checkGroupNum", icc,
				Enums.ConnMethod.POST, false);
		
		JSONObject company = new JSONObject();
		try {
			company.put("group_num", policyNumber);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JSONObject check = new JSONObject();
		try {
			check.put("company", company);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		cct.execute(check.toString());		
	}

	private void signUp() {
		
		IControllerContentCallback icc = new IControllerContentCallback() {
			public void handleSuccess(String content){

				JSONObject jsonObj = null, userObj = null;
				User user = null;
                Log.d("signUp user",content);

                try {
					jsonObj = new JSONObject(content);
                    if(jsonObj.has("error")) {
                        JSONArray errorMessage = jsonObj.getJSONObject("error").getJSONArray("message");
                        String message = String.valueOf((Object) errorMessage.get(0));
                        StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name), message, context);
                        return;

                    }else if(jsonObj.has("user")) {
                        userObj = jsonObj.getJSONObject("user");
                        user = new User(userObj);
                    }else {
                        StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name), "Sign Up failed, Please try it again", context);
                        return;
                    }
				} catch (JSONException e) {
					e.printStackTrace();
				}	
	            
	            if(user != null) {
                    activationCode = user.activationCode;
                    token = user.token;

                    sendEmailWithActivationCode();
                }else {
                    StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name), "Sign Up failed, Please try it again", context);
                    return;

                }

			}

			public void handleError(Exception e){
                StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name), "Sign Up failed, Please try it again", context);

                return;

			}
		};
		
		ControllerContentTask cct = new ControllerContentTask(
				"https://staging.intrepid247.com/v1/users", icc,
				Enums.ConnMethod.POST,false);

		JSONObject user = new JSONObject();
		JSONArray roles = null;
		roles = new JSONArray().put("end_user");
		try {
			user.put("email", email);
			user.put("first_name", firstName);
			user.put("last_name", lastName);
			user.put("username", userName);
			user.put("password", password);
			user.put("roles", roles);
			user.put("locale_code", "en_CA");
			user.put("country_code", countryCode);
			user.put("company_id", policyNumber);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JSONObject signUp = new JSONObject();
		try {
			signUp.put("user", user);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		cct.execute(signUp.toString());
        Log.d("signUp data",signUp.toString());

	}
	
	private void sendEmailWithActivationCode(){
		
		IControllerContentCallback icc = new IControllerContentCallback() {
			public void handleSuccess(String content){

				JSONArray jsonObj = null;
                Log.d("Email sent",content);
				
				try {
					jsonObj = new JSONArray(content);
					if ("sent".equals(jsonObj.getJSONObject(0).getString("status"))){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Thank you for signing up. Please check your email to activate your account.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent mIntent = new Intent(SignupActivity.this,LoginActivity.class);
                                startActivity(mIntent);
                                SignupActivity.this.finish();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

					}else if("error".equals(jsonObj.getJSONObject(0).getString("status"))){
                        StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name), jsonObj.getJSONObject(0).getString("message"), context);
                        return;

                    }

				} catch (JSONException e) {
					e.printStackTrace();
                    StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name), "Please try again later.", context);
                    return;

                }
	            
			}

			public void handleError(Exception e){
                StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name), "Please try again later.", context);
				return;

			}
		};
		
		ControllerContentTask cct = new ControllerContentTask(
				"https://mandrillapp.com/api/1.0/messages/send.json", icc,
				Enums.ConnMethod.POST,false);


		String text = String.format("Hi %s,\n\nThank you for signing up with ACE Travel Smart.\n"
				+ "Please click on the confirmation link below to activate your account.\n"
				+ "https://app-staging.intrepid247.com/users/activate/%s", firstName, activationCode);
		
		JSONObject message = new JSONObject();
		JSONObject to = new JSONObject();
		JSONArray toArray = null;
		try {
            to.put("email", email);
			to.put("name",firstName+" "+lastName);
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		
		toArray = new JSONArray().put(to);
		try {
			message.put("from_email", "do-not-reply@acetravelsmart.com");
			message.put("from_name", "ACE Travel Smart");
			message.put("subject", "Thank you for signing up");
			message.put("text", text);
			message.put("to", toArray);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JSONObject send = new JSONObject();
		try {
			send.put("key", "2Hw47otRRKIaEQ3sQwoXAg");
			send.put("message", message);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		cct.execute(send.toString());
        Log.d("email send.json", send.toString());
	}
}
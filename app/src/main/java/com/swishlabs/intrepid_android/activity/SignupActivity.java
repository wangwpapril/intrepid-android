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
import com.swishlabs.intrepid_android.data.api.callback.ControlerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControlerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Country;
import com.swishlabs.intrepid_android.data.api.model.User;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.StringUtil;


public class SignupActivity extends BaseActivity {

	protected static final String TAG = "SignupActivity";
	private EditText etFirstName;
	private EditText etLastName;
	private EditText etEmail;
	private AutoCompleteTextView etCountry;
	private EditText etUserName;
	private EditText etPassword;
	private EditText etPolicyNumber;
	private Button btnSignUp;
	private TextView termsUse;
	
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
		
		IControlerContentCallback icc = new IControlerContentCallback() {
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
					etCountry.setAdapter(countryAdapter);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

			public void handleError(Exception e) {
				return;
			}
		};
		ControlerContentTask cct = new ControlerContentTask(
				"https://staging.intrepid247.com/v1/countries", icc,
				Enums.ConnMethod.GET, false);

		String ss = null;
		cct.execute(ss);		
	}


	private void initView() {
		super.initTitleView();
		etFirstName = (EditText) findViewById(R.id.firstNameEditText);
		etLastName = (EditText) findViewById(R.id.lastNameEditText);
		etEmail = (EditText) findViewById(R.id.emailEditText);
		etCountry = (AutoCompleteTextView) findViewById(R.id.countryEditText);
		etUserName = (EditText) findViewById(R.id.userNameEditText);
		etPassword = (EditText) findViewById(R.id.PasswordEditText);
		etPolicyNumber = (EditText) findViewById(R.id.policyNumberEditText);
		etUserName = (EditText) findViewById(R.id.userNameEditText);
		btnSignUp = (Button) findViewById(R.id.butSignUp);
		btnSignUp.setOnClickListener(this);
		termsUse = (TextView) findViewById(R.id.termsofUse);
		termsUse.setOnClickListener(this);
		termsUse.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		
		etCountry.setAdapter(countryAdapter);
		etCountry.setEnabled(true);
//		etCountry.setDropDownBackgroundResource(R.drawable.login_btn);

		etCountry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				etCountry.showDropDown();

			}
			
		});
		
		etCountry.setOnItemClickListener(new OnItemClickListener() {

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
		tvTitleName.setText(getResources().getString(R.string.signup_title_name));
		ivTitleBack.setVisibility(View.VISIBLE);
        ivTitleBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == etCountry) {
			etCountry.setEnabled(true);

		} else if (v == btnSignUp) {

/*			String firstName = etFirstName.getText().toString();
			String lastName = etLastName.getText().toString();
			String email = etEmail.getText().toString();
			String country = etCountry.getText().toString();
			String userName = etUserName.getText().toString();
			String password = etPassword.getText().toString();
			String policyNumber = etPolicyNumber.getText().toString();
*/			
			firstName = etFirstName.getText().toString();
			lastName = etLastName.getText().toString();
			email = etEmail.getText().toString();
			country = etCountry.getText().toString();
			userName = etUserName.getText().toString();
			password = etPassword.getText().toString();
			policyNumber = etPolicyNumber.getText().toString();
			
			if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
					TextUtils.isEmpty(email) || TextUtils.isEmpty(country) ||
					TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) ||
					TextUtils.isEmpty(policyNumber)) {
//				ToastHelper.showToast("All fields are required.", 2000);
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

		} else if (v == termsUse) {
			Intent i = new Intent();
			i.setClass(context, LegalActivity.class);
			context.startActivity(i);
		} else if (v == ivTitleBack) {
			context.finish();
		}

	}


	private void checkGroupNumber() {

		IControlerContentCallback icc = new IControlerContentCallback() {
			public void handleSuccess(String content) {
				JSONObject company = null;
				try {
					company = new JSONObject(content).getJSONObject("company");
					companyId = company.getString("id");	
					signUp();
					
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

			public void handleError(Exception e) {
				return;
			}
		};
		ControlerContentTask cct = new ControlerContentTask(
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
		
		IControlerContentCallback icc = new IControlerContentCallback() {
			public void handleSuccess(String content){

				JSONObject jsonObj = null, userObj = null;
				User user = null;
                Log.d("signUp user",content);

                try {
					jsonObj = new JSONObject(content);
					userObj = jsonObj.getJSONObject("user");
					user = new User(userObj);
				} catch (JSONException e) {
					e.printStackTrace();
				}	
	            
	            activationCode = user.activationCode;
                token = user.token;

	            sendEmailWithActivationCode();

			}

			public void handleError(Exception e){
                StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name), "Sign Up failed, Please try it again", context);

                return;

			}
		};
		
		ControlerContentTask cct = new ControlerContentTask(
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
		
		IControlerContentCallback icc = new IControlerContentCallback() {
			public void handleSuccess(String content){

				JSONArray jsonObj = null;
			    JSONObject jo = null;
                Log.d("Email sent",content);
				
				try {
					jsonObj = new JSONArray(content);
					jo = jsonObj.getJSONObject(0);
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

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}	
	            
			}

			public void handleError(Exception e){
//				showAlertDialog(getResources().getString(
	//					R.string.login_title_name), "Invalid login credentials");
				return;

			}
		};
		
		ControlerContentTask cct = new ControlerContentTask(
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
//            to.put("email", "wwang@peakcontact.com");
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

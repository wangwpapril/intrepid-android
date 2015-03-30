package com.intrepid.travel.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.intrepid.travel.Enums.ConnMethod;
import com.intrepid.travel.utils.Common;

import android.os.AsyncTask;
import android.os.Handler;


public class ControlerContentTask extends
		AsyncTask<String, Void, ResultHolder> {

	private IControlerContentCallback icc;
	private String url;
	private ConnMethod connMethod;
	private boolean isHideLoading;
	private static int NORMAL_TIMEOUT = 6000;				


	public ControlerContentTask(String url, IControlerContentCallback icc,
			ConnMethod connMethod,boolean isHideLoading){
		this.icc = icc;
		this.url = url;
		this.connMethod = connMethod;
		this.isHideLoading = isHideLoading;
	}
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(!isHideLoading){
			new Handler().post(new Runnable() {
				public void run() {
						Common.showLoading("Loading");
				}
			});
		}
	}

	private List<NameValuePair> getParams(IContentParms[] params){
		List<NameValuePair> connParams = null;
		try {
			if (params != null && params.length > 0) {
				connParams = new ArrayList<NameValuePair>();
				connParams.add(new BasicNameValuePair("json", params[0].getparmStr()));
			}
		} catch (Exception e) {
			return null;
		}
		return connParams;
	}

	protected ResultHolder doInBackground(String... params){

		ResultHolder rh = new ResultHolder();
		String json = null;
		if(params[0] != null)
			json = params[0];
		
		try {
			switch (connMethod) {
			case GET:
				rh.setResult(SimpleHttpClient.doGet(url, NORMAL_TIMEOUT));
				break;
			case POST:
//				rh.setResult(SimpleHttpClient.post(url, getParams(params)));
				rh.setResult(SimpleHttpClient.post(json, url, NORMAL_TIMEOUT));
					
				break;
			}
			rh.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			rh.setSuccess(false);
			rh.setResult(e.getMessage());
		}
		return rh;
	}

	protected void onPostExecute(ResultHolder result){
		super.onPostExecute(result);
		Common.cancelLoading();
		if (result.isSuccess()) {
			icc.handleSuccess(result.getResult());
		} else {
			icc.handleError(new Exception(result.getResult()));
		}
	}
	
	
}

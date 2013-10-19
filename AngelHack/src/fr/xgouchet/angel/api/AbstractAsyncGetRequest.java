package fr.xgouchet.angel.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public abstract class AbstractAsyncGetRequest extends
		AsyncTask<Void, Void, Void> {

	private final String mPath;
	private final String mToken;

	private Exception mException;
	private Object mResult;

	private APIRequestListener mListener;

	public AbstractAsyncGetRequest(String path, String token) {
		mPath = path;
		mToken = token;
	}

	public void setListener(APIRequestListener listener) {
		this.mListener = listener;
	}

	@Override
	protected Void doInBackground(Void... params) {

		// optional additional parameters
		List<NameValuePair> getParams = new ArrayList<NameValuePair>();
		fillRequestParams(getParams);
		getParams.add(new BasicNameValuePair("access_token", mToken));
		String paramString = URLEncodedUtils.format(getParams, "utf-8");

		// create get request
		HttpGet get = new HttpGet(GithubAPIHelper.BASE_URL + mPath + "?"
				+ paramString);
		get.setHeader("Accept", "application/json");

		// Launch request
		HttpClient client = new DefaultHttpClient();

		try {
			HttpResponse response = client.execute(get);
			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == HttpStatus.SC_OK) {
				mResult = parseResponse(EntityUtils.toString(response
						.getEntity()));
			} else {
				mException = new HttpResponseException(status.getStatusCode(),
						status.getReasonPhrase());
			}

		} catch (ClientProtocolException e) {
			mException = e;
		} catch (IOException e) {
			mException = e;
		} catch (ParseException e) {
			mException = e;
		} catch (Exception e) {
			mException = e;
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		if (mListener != null) {
			if (mException == null) {
				mListener.onRequestComplete(mResult);
			} else {
				mListener.onRequestException(mException);
			}
		}
	}

	protected abstract void fillRequestParams(List<NameValuePair> params);

	protected abstract Object parseResponse(String response) throws Exception;
}

package fr.xgouchet.angel.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

public abstract class AbstractAsyncPostRequest extends
		AsyncTask<Void, Void, Void> {

	private final String mPath;
	private final String mToken;

	private Exception mException;
	private Object mResult;

	private APIRequestListener mListener;

	public AbstractAsyncPostRequest(String path, String token) {
		mPath = path;
		mToken = token;
	}

	public void setListener(APIRequestListener listener) {
		mListener = listener;
	}

	@Override
	protected Void doInBackground(Void... params) {

		// create post request
		HttpPost post = new HttpPost(GithubAPIHelper.BASE_URL + mPath
				+ "?access_token=" + mToken);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-Type", "application/json");

		// Create Http client
		HttpClient client = new DefaultHttpClient();

		try {
			// set Content
			HttpEntity entity = new StringEntity(getJsonContent());
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == getExpectedStatus()) {
				Log.i("Yeah", "YERAH!!!!");
				mResult = parseResponse(EntityUtils.toString(response
						.getEntity()));
			} else {
				mException = new HttpResponseException(status.getStatusCode(),
						status.getReasonPhrase());
			}

		} catch (UnsupportedEncodingException e) {
			mException = e;
		} catch (ClientProtocolException e) {
			mException = e;
		} catch (IOException e) {
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

	protected abstract String getJsonContent() throws JSONException;

	protected abstract int getExpectedStatus();

	protected abstract Object parseResponse(String response) throws Exception;

}

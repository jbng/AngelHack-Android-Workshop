package fr.xgouchet.angel.oauth;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import fr.xgouchet.angel.api.AbstractAsyncRequest;

public class AsyncRequestAccessToken extends AbstractAsyncRequest {

	private final String mCode;

	public AsyncRequestAccessToken(String code) {
		mCode = code;
	}

	@Override
	protected Void doInBackground(Void... params) {

		HttpPost post = GithubOauth.getAccessTokenPost(mCode);

		HttpClient client = new DefaultHttpClient();

		try {
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				JSONObject json = new JSONObject(EntityUtils.toString(entity));

				mResult = json.getString("access_token");
			}
		} catch (ClientProtocolException e) {
			mException = e;
		} catch (IOException e) {
			mException = e;
		} catch (ParseException e) {
			mException = e;
		} catch (JSONException e) {
			mException = e;
		}

		return null;
	}

}

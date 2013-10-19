package fr.xgouchet.angel.oauth;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import fr.xgouchet.angel.api.AbstractAsyncRequest;
import fr.xgouchet.angel.api.GithubAPIHelper;

public class AsyncRequestVerifyToken extends AbstractAsyncRequest {

	private String mToken;

	public AsyncRequestVerifyToken(String token) {
		mToken = token;
	}

	@Override
	protected Void doInBackground(Void... params) {

		// Create get request
		HttpGet get = new HttpGet(GithubAPIHelper.BASE_URL + "/applications/"
				+ GithubOauth.CLIENT_ID + "/tokens/" + mToken);
		get.setHeader("Accept", "application/json");
		get.setHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(
				"user", "password"), "UTF-8", false));

		// create client
		HttpClient client = new DefaultHttpClient();

		HttpResponse response;
		try {
			response = client.execute(get);

			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == HttpStatus.SC_OK) {

			} else {
				mException = new HttpResponseException(status.getStatusCode(),
						status.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			mException = e;
		} catch (IOException e) {
			mException = e;
		}

		return null;
	}
}

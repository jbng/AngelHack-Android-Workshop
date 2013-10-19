package fr.xgouchet.angel.oauth;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;

public class GithubOauth {

	public static final String OAUTH_LOGIN = "https://github.com/login/oauth/authorize";
	public static final String OAUTH_ACCESS = "https://github.com/login/oauth/access_token";

	public static final String CLIENT_ID = "543fa5fa61b5b87e48c8";
	public static final String SECRET_KEY = "b25cfe0a0847f318228f2b5494be603fced4c315";

	public static final String REDIRECT = "http://www.xgouchet.fr/angel/oauth/login_success";

	public static final String PREF_TOKEN = "oauth_token";

	public static String getAccessToken(Context context) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		return prefs.getString(PREF_TOKEN, null);
	}

	public static void setAccessToken(Context context, String token) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		Editor editor = prefs.edit();
		editor.putString(PREF_TOKEN, token);
		editor.commit();
	}

	public static Uri getRequestTokenUri() {

		Uri.Builder builder = Uri.parse(OAUTH_LOGIN).buildUpon();

		builder.appendQueryParameter("client_id", CLIENT_ID);
		builder.appendQueryParameter("scope", "public_repo,repo");
		builder.appendQueryParameter("redirect_uri", REDIRECT);

		return builder.build();
	}

	public static HttpPost getAccessTokenPost(String code) {
		HttpPost post = new HttpPost(OAUTH_ACCESS);

		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setHeader("Accept", "application/json");

		List<NameValuePair> pairs = new ArrayList<NameValuePair>(8);

		pairs.add(new BasicNameValuePair("client_id", CLIENT_ID));
		pairs.add(new BasicNameValuePair("client_secret", SECRET_KEY));
		pairs.add(new BasicNameValuePair("code", code));

		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e) {
			return null;
		}

		return post;
	}

}

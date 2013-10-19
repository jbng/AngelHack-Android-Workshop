package fr.xgouchet.angel.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import fr.xgouchet.angel.api.APIRequestListener;
import fr.xgouchet.angel.oauth.AsyncRequestAccessToken;
import fr.xgouchet.angel.oauth.GithubOauth;
import fr.xgouchet.angel.oauth.OAuthListener;

public class OAuthFragment extends Fragment implements APIRequestListener {

	private OAuthListener mListener;

	public void setOauthListener(OAuthListener listener) {
		mListener = listener;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// setup webview
		WebView webview = new WebView(getActivity());
		webview.setWebViewClient(new OauthWebViewClient());

		// settings
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);

		// Load 1st OAuth page
		webview.loadUrl(GithubOauth.getRequestTokenUri().toString());

		return webview;
	}

	private class OauthWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);

			if (url.startsWith(GithubOauth.REDIRECT)) {
				String code = Uri.parse(url).getQueryParameter("code");

				AsyncRequestAccessToken request = new AsyncRequestAccessToken(
						code);
				request.setListener(OAuthFragment.this);
				request.execute();

				view.stopLoading();
			}

		}
	}

	@Override
	public void onRequestComplete(Object result) {
		if (mListener != null) {
			mListener.onConnected(result.toString());
		}
	}

	@Override
	public void onRequestException(Exception exception) {
		if (mListener != null) {
			mListener.onException(exception);
		}
	}
}

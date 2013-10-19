package fr.xgouchet.angel.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import fr.xgouchet.angel.R;
import fr.xgouchet.angel.oauth.GithubOauth;
import fr.xgouchet.angel.oauth.OAuthListener;
import fr.xgouchet.angel.ui.Toaster;
import fr.xgouchet.angel.ui.fragment.OAuthFragment;

public class LoginActivity extends FragmentActivity implements OAuthListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void onStart() {
		super.onStart();

		String token = GithubOauth.getAccessToken(this);

		if (token == null) {
			displayOAuthFragment();
		} else {
			startActivity(new Intent(getBaseContext(), MainActivity.class));
			finish();
		}
	}

	private void displayOAuthFragment() {
		OAuthFragment fragment = new OAuthFragment();
		fragment.setOauthListener(this);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.main_container, fragment);
		transaction.commit();
	}

	@Override
	public void onConnected(String token) {
		GithubOauth.setAccessToken(this, token);
		startActivity(new Intent(getBaseContext(), MainActivity.class));
		finish();
	}

	@Override
	public void onException(Exception exception) {
		Toaster.toastException(this, exception);
	}
}

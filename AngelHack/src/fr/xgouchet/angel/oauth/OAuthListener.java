package fr.xgouchet.angel.oauth;

public interface OAuthListener {
	void onConnected(String token);

	void onException(Exception mException);
}
package fr.xgouchet.angel.api;

public interface APIRequestListener {

	void onRequestComplete(Object result);
	
	void onRequestException(Exception exception);
	
}

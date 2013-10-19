package fr.xgouchet.angel.api;

import android.os.AsyncTask;

public abstract class AbstractAsyncRequest extends AsyncTask<Void, Void, Void> {

	protected Exception mException;
	protected Object mResult;

	private APIRequestListener mListener;

	public void setListener(APIRequestListener listener) {
		mListener = listener;
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
}

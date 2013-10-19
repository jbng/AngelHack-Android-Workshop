package fr.xgouchet.angel.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Toaster {

	public static void toastException(Context context, Exception exception) {
		Log.e("Toaster", "Exception", exception);

		Toast toast = Toast.makeText(context, exception.getMessage(),
				Toast.LENGTH_LONG);
		((TextView) toast.getView().findViewById(android.R.id.message))
				.setTextColor(Color.RED);
		toast.show();
	}
}

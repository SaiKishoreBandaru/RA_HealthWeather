 package com.example.healthweather; 

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

	/*Network check*/
	public static boolean haveNetworkConnection(Context context) {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}

		// NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if (networkInfo != null && networkInfo.isConnected()) {
		// haveConnectedWifi = true;
		// haveConnectedMobile = true;
		// } else {
		// haveConnectedWifi = false;
		// haveConnectedMobile = false;
		// }
		return haveConnectedWifi || haveConnectedMobile;
	}

	// Custom alert to show no netwok
		@SuppressWarnings("deprecation")
		public static void noNetworkAlert(final Context context, String message) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
//			Looper.prepare();
			final AlertDialog dlg = builder.create();
			dlg.setCancelable(false);
			dlg.setMessage(message);
			dlg.setButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dlg.dismiss();
//					((Activity) context).finish();
					
				}
			});
			dlg.show();

//			Looper.loop();
		}
}

package example.com.videofly.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by madhavchhura on 5/19/15.
 */
public class NetworkStatusReceiver extends BroadcastReceiver {
    public static boolean isOnline = true;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TEST", "Received the intent: " + intent.getAction());

        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = (networkInfo != null) && networkInfo.isConnected();

            if(isConnected)
                isOnline = true;
            else{
                isOnline = false;
            }
            Log.i("TEST", "Network status: " + isConnected);
        }
    }

}
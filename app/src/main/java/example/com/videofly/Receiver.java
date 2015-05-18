package example.com.videofly;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by madhavchhura on 5/11/15.
 */
public class Receiver extends ParsePushBroadcastReceiver {

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        Intent i = new Intent(context, VideoCallActivity.class);
        JSONObject json = null;

        try
        {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");

            json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.d("Reciever", "got action " + action + " on channel " + channel + " with:");
            Iterator itr = json.keys();
            while (itr.hasNext())
            {
                String key = (String) itr.next();
                Log.d("Reciever", "..." + key + " => " + json.getString(key));
            }

            String sessionId = json.getString("sessionId");
            String token = json.getString("publisherToken");
            i.putExtra("sessionId",sessionId);
            i.putExtra("publisherToken",token);
            if(sessionId != null && token != null){
                MainActivity.sessionId = sessionId;
                MainActivity.token = token;
            }
        }
        catch (JSONException e)
        {
            Log.d("Reciever", "JSONException: " + e.getMessage());
        }


        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
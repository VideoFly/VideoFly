package example.com.videofly;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


import example.com.videofly.fragments.FriendsFragment;
import example.com.videofly.fragments.HomeFragment;
import example.com.videofly.fragments.MessagesFragment;
import example.com.videofly.fragments.SettingsFragment;
import example.com.videofly.slidingmenu.FragmentDrawer;

public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    public  User user;
    private JSONObject usr;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        user = new User();
        makeMeRequest();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void createDisplay() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = new FragmentDrawer(bitmap);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
    }

    class getFriendsAsync extends AsyncTask<JSONArray, Void, Void>
    {
        protected Void doInBackground(JSONArray... arg0) {
            Log.d("DoINBackGround","On doInBackground...");
            user.setUserFriends(arg0[0]);
            return null;
        }
    }

    private void makeMeRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        usr = object;
                        updateUserData();
                        fbFriendsRequest();
                        bitmap = userPic();
                        user.setUserProfilePicture(bitmap);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(300)");
        request.setParameters(parameters);
        request.executeAsync();
    }
    private void fbFriendsRequest(){
        GraphRequest request = GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(final JSONArray jsonArray, GraphResponse graphResponse) {
                        new getFriendsAsync().execute(jsonArray);
                        createDisplay();
                    }
                });
        request.executeAsync();
    }

    private void updateUserData() {
        user.setUser_fb_id(usr.optString("id"));
        user.setUserName(usr.optString("name"));
        user.setUserEmail(usr.optString("email"));

        ParseUser.getCurrentUser().saveInBackground();
    }

    private Bitmap userPic() {

        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        try {
            URL url = new URL("http://graph.facebook.com/"
                    + Profile.getCurrentProfile().getId()+ "/picture");
            HttpGet request = new HttpGet(String.valueOf(url));
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
            InputStream inputStream = bufferedEntity.getContent();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment(user);
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new FriendsFragment(user.getUserFriends());
                title = getString(R.string.title_friends);
                break;
            case 2:
                fragment = new MessagesFragment();
                title = getString(R.string.title_messages);
                break;
            case 3:
                fragment = new SettingsFragment();
                title = getString(R.string.tittle_settings);
                break;
            case 4:
                logout();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    private void logout(){
        if(ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())){

            ParseUser.logOut();

            Toast.makeText(getApplicationContext(), "Log Out Successful", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, login.class);
            startActivity(i);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}

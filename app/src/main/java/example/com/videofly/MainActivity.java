package example.com.videofly;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.parse.FunctionCallback;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import bolts.AppLinks;
import example.com.videofly.fragments.FriendsFragment;
import example.com.videofly.fragments.HomeFragment;
import example.com.videofly.fragments.MessagesFragment;
import example.com.videofly.fragments.SettingsFragment;
import example.com.videofly.slidingmenu.FragmentDrawer;

public class MainActivity extends AppCompatActivity implements
        FragmentDrawer.FragmentDrawerListener,
        FriendsFragment.CallFriendListener{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private  User user;
    private JSONObject usr;
    private Bitmap imgBitmap = null;
    private ParseObject broadcastObject;
    public static String sessionId;
    public static String token;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMG = 0;
    final int MAX_IMAGE_DIMENSION = (int) (96 * Resources.getSystem().getDisplayMetrics().density);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new User();
        makeMeRequest();
    }
    private void createDisplay() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = new FragmentDrawer();
        Bundle args = new Bundle();
        args.putParcelable("imgBitmap", imgBitmap);

        drawerFragment.setArguments(args);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
    }

    private void updateUserData() {
        user.setUser_fb_id(usr.optString("id"));
        user.setUserName(usr.optString("name"));
        Log.d("THIS DOESNT MAKE SENSE", user.getUserName());
        user.setUserEmail(usr.optString("email"));

        if(ParseUser.getCurrentUser().isNew()){
            imgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
            user.setUserImage(uploadImageFile(imgBitmap));
            ParseUser.getCurrentUser().saveInBackground();
        }
        else
            user.setUserImage(ParseUser.getCurrentUser().getParseFile("userImage"));

        saveSessionToParse();
        ParseUser.getCurrentUser().saveInBackground();
        user.saveUserToParse();
    }

    private ParseFile uploadImageFile(Bitmap image) {
        imgBitmap = image;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile parseFile = new ParseFile(user.getUser_fb_id()+".png", byteArray);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getApplicationContext(), "Image Changed", Toast.LENGTH_LONG).show();
                if (e != null) {
                    Log.d("Error Saving", e.getMessage());
                    Toast.makeText(getApplicationContext(), "Error Saving" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return parseFile;
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }




    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case -1:
                changeProfilePicture();
            case 0:
                fragment = new HomeFragment(user);
                title = getString(R.string.title_home);
                break;
            case 1:
                FriendsFragment friendsFragment = new FriendsFragment(user.getUserFriends());
                friendsFragment.setCallFriendListener(this);
                fragment = friendsFragment;
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

    private void changeProfilePicture() {
        new MaterialDialog.Builder(this)
                .callback(new MaterialDialog.ButtonCallback(){
                    @Override
                    public void onPositive(MaterialDialog dialog){
                        dispatchTakePictureIntent();
                    }
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        loadImagefromGallery();
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        removeProfileImage();
                    }
                })
                .title("SET A PROFILE PICTURE")
                .content("Select one of the following")
                .positiveText("Take Photo")
                .negativeText("Choose From Library")
                .neutralText("Remove Current Photo")
                .autoDismiss(true)
                .show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    private void removeProfileImage() {
        imgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
        user.setUserImage(uploadImageFile(imgBitmap));
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                drawerFragment.setProfileImageView(imgBitmap);
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_search:
                return true;
            case R.id.invite_friends:
                inviteFriends();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void inviteFriends() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }

        String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://fb.me/747689235350801";
        previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("In on Acitivity Result", "creating new Fragment Drawer");
            Bundle extras = data.getExtras();
            imgBitmap = (Bitmap) extras.get("data");
            user.setUserImage(uploadImageFile(imgBitmap));
            ParseUser.getCurrentUser().saveInBackground();
            drawerFragment.setProfileImageView(imgBitmap);
        }
        else if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            try {
                imgBitmap = scaleImage(this,selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            user.setUserImage(uploadImageFile(imgBitmap));
            ParseUser.getCurrentUser().saveInBackground();
            drawerFragment.setProfileImageView(imgBitmap);
        }
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap scaleImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
    }

    private int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    /*
    * Methods below are used to make Facebook API graph requests.
    **/

    /**
     * makeMeRequest is used to get current logged in users
     * profile information.
     */
    private void makeMeRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        usr = object;
                        updateUserData();
                        fbFriendsRequest();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }
    /**
     *  This method is used to get get current logged in users
     *  friends from facebook in a AsyncTask.
     **/
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

    /** Class Description of getFriendsAsync
     *  AsyncTask to get users friends from facebook.
     **/
    class getFriendsAsync extends AsyncTask<JSONArray, Void, Void> {
        protected Void doInBackground(JSONArray... arg0) {
            Log.d("DoINBackGround","On doInBackground...");
            user.setUserFriends(arg0[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    /*
    * Methods below are used to generate OpenTok Configurations
    * using Parse Cloud.
    **/

    /**
     * saveSessionToParse creates a new Broadcast object related to the
     * Parse database class Broadcast. Also sets up a relation between
     * the current logged in user and the Broadcast Object.
     */
    private void saveSessionToParse(){

        broadcastObject = ParseObject.create("Broadcast");
        broadcastObject.put("owner",ParseUser.getCurrentUser());
        ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
        acl.setPublicReadAccess(true);
        broadcastObject.setACL(acl);
        broadcastObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                isBroadcastOwner();
                saveTokeToCloud();
            }
        });
    }

    /**
     * saveToken is used to make a request to the ParseCloud. The ParseCloud
     * is used to generate unique OpenTokek sessionId, and tokens for the
     * current logged in user.
     */
    private void saveTokeToCloud(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("broadcast", broadcastObject.getObjectId());
        ParseCloud.callFunctionInBackground("getBroadcastToken", params, new FunctionCallback<String>() {
            public void done(String response, ParseException e) {
                if (e == null) {
                    Log.d("Cloud Response", "There were no exceptions! " + response);
                    token = response;
                    broadcastObject.put("publisherToken", response);
                    broadcastObject.put("subscriberToken", response);
                    broadcastObject.saveInBackground();
                    sessionId = broadcastObject.getString("sessionId");
                } else {
                    Log.d("Cloud Response", "Exception: " + response + e);
                }
            }
        });
    }

    /**
     * isBroadcastOwner is used to check if the broadcast object owner is
     * the current logged in user.
     * @return true if current logged in user is the owner of the object
     * Broadcast in Parse database.
     */
    private boolean isBroadcastOwner (){
        if(broadcastObject.get("owner").equals(ParseUser.getCurrentUser())){
            Log.d("Check Owner", "This is the owner");
            return true;
        }
        return false;
    }

    @Override
    public void onCallFriend(String friendId) {
        Log.d("MainActivity", "onCallFriend Called " + friendId);
        JSONObject data = new JSONObject();
        Log.d("MainActivity", "sessionId: " + sessionId);
        try {
            data.put("sessionId",sessionId);
            data.put("publisherToken",token);
            data.put("alert", user.getUserName() + " is Video Calling You");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush parsePush = new ParsePush();
        parsePush.setData(data);
        parsePush.setChannel("channel");
        ParseQuery pQuery = ParseInstallation.getQuery();
        pQuery.whereEqualTo("fb_id", friendId);
        final Intent i = new Intent(this, VideoCallActivity.class);
        i.putExtra("sessionId",sessionId);
        i.putExtra("publisherToken",token);
        parsePush.sendDataInBackground(data, pQuery, new SendCallback() {
            @Override
            public void done(ParseException e) {
                startActivity(i);
            }
        });
        //parsePush.sendMessageInBackground(user.getUserName() + " is Video Calling You",pQuery);
        //Intent i = new Intent(this, VideoCallActivity.class);
        //startActivity(i);
    }
}

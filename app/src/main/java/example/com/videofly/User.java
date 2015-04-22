package example.com.videofly;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.facebook.AccessToken;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by madhavchhura on 4/21/15.
 */
public class User {

    private ProfilePictureView userProfilePictureView;
    private String userName;
    private String userEmail;
    private ArrayList userFriends;
    private String user_fb_id;
    private JSONObject usr;

    ParseUser parseUser = ParseUser.getCurrentUser();


    public User() {
        makeMeRequest();
        fbFriendsRequest();
    }

    public ProfilePictureView getUserProfilePictureView() {
        return userProfilePictureView;
    }

    public void setUserProfilePictureView(ProfilePictureView userProfilePictureView) {
        this.userProfilePictureView = userProfilePictureView;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        parseUser.setUsername(this.userName);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        parseUser.setEmail(this.userEmail);
    }

    public String getUser_fb_id() {
        return user_fb_id;
    }

    public void setUser_fb_id(String user_fb_id) {
        this.user_fb_id = user_fb_id;
        parseUser.put("fb_id",user_fb_id);
    }

    public ArrayList getUserFriends() {
        return userFriends;
    }
    public void setUserFriends(JSONArray userFriends){
        this.userFriends = new ArrayList();
        if(userFriends!=null){
            for(int i = 0; i < userFriends.length(); i++){
                try {
                    this.userFriends.add(userFriends.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        parseUser.put("fb_friends", this.userFriends);
    }

    private void fbFriendsRequest(){
        GraphRequest request = GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                        setUserFriends(jsonArray);
                        ParseUser.getCurrentUser().saveEventually();
                    }
                });
        request.executeAsync();
    }


    private void makeMeRequest() {

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        usr = object;
                        updateUserData();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void updateUserData() {
        setUser_fb_id(usr.optString("id"));
        setUserName(usr.optString("name"));
        setUserEmail(usr.optString("email"));
        ParseUser.getCurrentUser().saveEventually();
    }

}

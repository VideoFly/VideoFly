package example.com.videofly;

import android.graphics.Bitmap;
import android.util.Log;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by madhavchhura on 4/21/15.
 */
public class User {

    private Bitmap userProfilePicture;
    private String userName;
    private String userEmail;
    private ArrayList userFriends;
    private String user_fb_id;


    ParseUser parseUser = ParseUser.getCurrentUser();


    public User() {

    }

    public Bitmap getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(Bitmap userProfilePicture) {
        this.userProfilePicture = userProfilePicture;

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

        parseUser.put("fb_id", user_fb_id);
        Log.d("loidfsdafsa", "" + Profile.getCurrentProfile() + "");
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

}

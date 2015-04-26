package example.com.videofly;

import android.graphics.Bitmap;
import android.util.Log;

import com.facebook.Profile;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by madhavchhura on 4/21/15.
 */
public class User {

    private Bitmap userProfilePicture;
    private String userName;
    private String userEmail;
    private ArrayList<Friends> userFriends;
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
    }

    public ArrayList<Friends> getUserFriends() {
        return userFriends;
    }
    public void setUserFriends(JSONArray userFriends){
        this.userFriends = new ArrayList();
        if(userFriends!=null){
            for(int i = 0; i < userFriends.length(); i++){
                try {
                    JSONObject childObject = userFriends.getJSONObject(i);
                    this.userFriends.add(new Friends(childObject.getString("name"),
                            childObject.getString("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

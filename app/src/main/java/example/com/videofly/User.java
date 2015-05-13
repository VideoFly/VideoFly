package example.com.videofly;

import android.graphics.Bitmap;

import com.parse.ParseFile;
import com.parse.ParseInstallation;
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
    private ParseFile userImage;
    private ArrayList<Friends> userFriends;
    private String user_fb_id;


    ParseUser parseUser = ParseUser.getCurrentUser();
    ParseInstallation installation = new ParseInstallation().getCurrentInstallation();

    public User() {

    }

    public Bitmap getUserImageBitmap() {
        return userProfilePicture;
    }

    public void setUserImageBitmap(Bitmap userProfilePicture) {
        this.userProfilePicture = userProfilePicture;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        parseUser.setUsername(this.userName);
        installation.put("username",userName);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        parseUser.setEmail(this.userEmail);
        installation.put("email",userName);
    }

    public String getUser_fb_id() {
        return user_fb_id;
    }

    public void setUser_fb_id(String user_fb_id) {
        this.user_fb_id = user_fb_id;
        parseUser.put("fb_id", user_fb_id);
        installation.put("fb_id",user_fb_id);
    }

    public ParseFile getUserImage(){
        return userImage;
    }

    public void setUserImage(ParseFile profileImage) {
        this.userImage = profileImage;
        parseUser.put("userImage", profileImage);
    }
    public ArrayList<Friends> getUserFriends() {
        return userFriends;
    }
    public void setUserFriends(final JSONArray userFriends) {
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
        //put("friends",userFriends);
    }
    public void saveUserToParse(){
        installation.saveInBackground();
    }
}

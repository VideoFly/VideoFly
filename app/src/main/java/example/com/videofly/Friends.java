package example.com.videofly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

/**
 * Created by madhavchhura on 4/22/15.
 */
public class Friends {
    private String name;
    private String id;
    private ParseFile friendImage;
    private Bitmap bitmap;

    Friends(String name, String id) {
        this.name = name;
        this.id = id;
        this.setFriendImage(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getFriendImage() {
        return bitmap;
    }
    public void setFriendImage(String id){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("fb_id", id);
        try {
            List<ParseUser> list = query.find();
            for(ParseObject user : list){
                this.friendImage = user.getParseFile("userImage");
                Log.d("URL OF THE FRIEND IMAGE", friendImage.getUrl());
                URL url = new URL(friendImage.getUrl());
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Comparator<Friends> NameComparator = new Comparator<Friends>() {

        public int compare(Friends f1, Friends f2) {
            String StudentName1 = f1.getName().toUpperCase();
            String StudentName2 = f2.getName().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
}

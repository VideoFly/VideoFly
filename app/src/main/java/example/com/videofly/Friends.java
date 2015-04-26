package example.com.videofly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.Profile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by madhavchhura on 4/22/15.
 */
public class Friends {
    private String name;
    private String id;
    private Bitmap profilePic;
    private ArrayList<Friends> friendsArrayList;

    Friends(String name, String id) {
        this.name = name;
        this.id = id;
        this.setProfilePic(id);
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

    public Bitmap getProfilePic() {

        return profilePic;
    }

    public void setProfilePic(String id) {
        this.profilePic = userPic(id);
    }

    private Bitmap userPic(String id) {
        Bitmap bitmap = null;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        try {
            URL url = new URL("http://graph.facebook.com/"
                    + id + "/picture");
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


}

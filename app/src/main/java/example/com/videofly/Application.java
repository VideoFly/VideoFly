package example.com.videofly;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by madhavchhura on 4/25/15.
 */
public class Application extends android.app.Application {

    public void onCreate(){
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "I7zuvwYhjC8TglLLHeLkUPEbW5Ppx2XS67MUCRDm", "MRXLaui8yk4oBJZoTYjCMsB06n5BVk9iZYot10v6");
        ParseFacebookUtils.initialize(this);
        //ParseObject.registerSubclass(User.class);
    }
}

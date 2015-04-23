package example.com.videofly;

import java.util.ArrayList;

/**
 * Created by madhavchhura on 4/22/15.
 */
public class Friends {
    private String name;
    private String id;
    private int photoId;
    private ArrayList<Friends> friendsArrayList;

    Friends(String name, String id) {
        this.name = name;
        this.id = id;
    }

    Friends(String name, String id, int photoId) {
        this.name = name;
        this.id = id;
        this.photoId = photoId;
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

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }


}

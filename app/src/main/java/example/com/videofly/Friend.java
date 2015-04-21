package example.com.videofly;

public class Friend {

    private String name;
    private int numMutFriends;
    private int photoRes;

    public Friend(String name, int numMutFriends, int photoRes) {
        this.name = name;
        this.numMutFriends = numMutFriends;
        this.photoRes = photoRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumMutFriends() {
        return numMutFriends;
    }

    public void setNumMutFriends(int numMutFriends) {
        this.numMutFriends = numMutFriends;
    }

    public int getPhotoRes() {
        return photoRes;
    }

    public void setPhotoRes(int photoRes) {
        this.photoRes = photoRes;
    }
}
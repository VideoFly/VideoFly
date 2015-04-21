package example.com.videofly.slidingmenu;

/**
 * Created by madhavchhura on 4/20/15.
 */
public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private int icon;

    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    public NavDrawerItem(boolean showNotify, String title, int icon){
        this.showNotify = showNotify;
        this.title = title;
        this.icon = icon;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //ImageIcon fields are added to add icons next to each items. (TO DO)

    public int getIcon(){
        return icon;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }
}
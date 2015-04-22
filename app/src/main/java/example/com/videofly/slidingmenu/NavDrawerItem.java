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

    /**
     * @return true if notifications for the current listItem is being displayed.
     */
    public boolean isShowNotify() {
        return showNotify;
    }

    /**
     * @param showNotify sets notifications for the current listItem to display.
     */
    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }
    /**
     * @return the tittle of the list Item.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title - the name of the listItem you want to display on the sidebar.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the image icon of the list Item.
     */
    public int getIcon(){
        return icon;
    }

    /**
     * @param icon - the icon associated with the list item.
     */
    public void setIcon(int icon){
        this.icon = icon;
    }
}
package materialtest.vivz.slidenerd.materialtest;

public class NavDrawerItem {
    public String iconString;
    public String title;
    public int numMovies = 0;
    public boolean isGroupHeader = false;

    public NavDrawerItem(String dividerTitle) {
        this.iconString = "";
        this.title = dividerTitle;
        this.numMovies = -1;
        isGroupHeader = true;
    }

    public NavDrawerItem(char icon, String title, int size) {
        this.iconString = String.valueOf(icon);
        this.title = title;
        this.numMovies = size;
    }
}

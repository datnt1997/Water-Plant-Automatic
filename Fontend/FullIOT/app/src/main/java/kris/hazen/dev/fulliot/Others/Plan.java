package kris.hazen.dev.fulliot.Others;

/**
 * Created by quyen on 5/14/2017.
 */

public class Plan {

    private String name;
    private int type;
    private int thumbnail;

    public Plan() {
    }

    public Plan(String name, int type, int thumbnail) {
        this.name = name;
        this.type = type;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

}

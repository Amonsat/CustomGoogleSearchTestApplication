package ua.com.websat.viewpagertest;

/**
 * Created by Sat on 23.09.2014.
 */
public class SearchItem {
    private String cacheId;
    private String title;
    private String image;
    private String thumbnail;
    private boolean favorite;

    public void setCacheId (String cacheId) {
        this.cacheId = cacheId;
    }
    public String getCacheId() {
        return this.cacheId;
    }

    public void setTitle (String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return this.image;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    public boolean getFavorite() {
        return this.favorite;
    }

    public SearchItem(String cacheId, String title, String image, String thumbnail, boolean favorite) {
        this.setCacheId(cacheId);
        this.setTitle(title);
        this.setImage(image);
        this.setThumbnail(thumbnail);
        this.favorite = favorite;
    }
}

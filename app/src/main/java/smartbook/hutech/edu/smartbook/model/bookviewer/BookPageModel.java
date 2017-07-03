package smartbook.hutech.edu.smartbook.model.bookviewer;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;

/**
 * Created by hienl on 7/2/2017.
 */

public class BookPageModel {
    @SerializedName("pageIndex")
    private int pageIndex;
    @SerializedName("imageResourcePath")
    private String imageResourcePath;
    @SerializedName("trackResourcePath")
    private String trackResourcePath;
    @SerializedName("items")
    private List<BookItemModel> items = null;
    @SerializedName("pageImage")
    private String imageFileName;

    public String getImagePath() {
        return imageResourcePath + File.separator + imageFileName;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getImageResourcePath() {
        return imageResourcePath;
    }

    public void setImageResourcePath(String imageResourcePath) {
        this.imageResourcePath = imageResourcePath;
    }

    public String getTrackResourcePath() {
        return trackResourcePath;
    }

    public void setTrackResourcePath(String trackResourcePath) {
        this.trackResourcePath = trackResourcePath;
    }

    public List<BookItemModel> getItems() {
        return items;
    }

    public void setItems(List<BookItemModel> items) {
        this.items = items;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}

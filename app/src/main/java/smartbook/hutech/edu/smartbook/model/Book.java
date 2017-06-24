package smartbook.hutech.edu.smartbook.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hienl on 6/24/2017.
 */

public class Book {
    private String mTitle;
    private String mAuthor;
    private String mDescription;
    private String mCover;
    private String mDownload;
    private List<Page> mPageList;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getCover() {
        return mCover;
    }

    public void setCover(String cover) {
        mCover = cover;
    }

    public String getDownload() {
        return mDownload;
    }

    public void setDownload(String download) {
        mDownload = download;
    }

    public List<Page> getPageList() {
        if (mPageList == null) {
            mPageList = new ArrayList<>();
        }
        return mPageList;
    }

    public void setPageList(List<Page> pageList) {
        mPageList = pageList;
    }
}

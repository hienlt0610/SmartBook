package smartbook.hutech.edu.smartbook.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import smartbook.hutech.edu.smartbook.common.BaseModel;

/**
 * Created by hienl on 6/24/2017.
 */

public class Book extends BaseModel {
    @SerializedName("id")
    private Integer id;

    @SerializedName("bookTitle")
    private String mTitle;

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("bookCover")
    private String mCover;

    @SerializedName("download")
    private String mDownload;

    @SerializedName("pageList")
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

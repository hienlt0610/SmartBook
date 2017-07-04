package smartbook.hutech.edu.smartbook.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import smartbook.hutech.edu.smartbook.common.BaseModel;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookListPageModel;

/**
 * Created by hienl on 6/24/2017.
 */

public class Book extends BaseModel {
    @SerializedName("id")
    private Integer mBookId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("cover")
    private String mCover;

    @SerializedName("download")
    private String mDownload;

    @SerializedName("demoPage")
    private List<String> mDemoPage;

    private BookListPageModel mPageList;

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

    public BookListPageModel getPageList() {
        if (mPageList == null) {
            mPageList = new BookListPageModel();
        }
        return mPageList;
    }

    public void setPageList(BookListPageModel pageList) {
        mPageList = pageList;
    }

    public Integer getBookId() {
        return mBookId;
    }

    public List<String> getmDemoPage() {
        return mDemoPage;
    }

    public void setmDemoPage(List<String> mDemoPage) {
        this.mDemoPage = mDemoPage;
    }

    public void setBookId(Integer bookId) {
        mBookId = bookId;
    }
}

package smartbook.hutech.edu.smartbook.model.bookviewer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hienl on 7/2/2017.
 */

public class BookInfoModel {
    @SerializedName("bookId")
    private int bookId;
    @SerializedName("bookName")
    private String bookName;
    @SerializedName("categoryId")
    private int categoryId;
    @SerializedName("categoryName")
    private String categoryName;
    @SerializedName("cover")
    private String cover;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}

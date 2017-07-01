package smartbook.hutech.edu.smartbook.model;
/*
 * Created by Nhat Hoang on 24/06/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import smartbook.hutech.edu.smartbook.common.BaseModel;

public class Category extends BaseModel {
    @SerializedName("id")
    private Integer mCategoryId;
    @SerializedName("books")
    private List<Book> mListBooks;
    @SerializedName("title")
    private String mTitle;

    public List<Book> getListBooks() {
        if(mListBooks == null)
            mListBooks = new ArrayList<>();
        return mListBooks;
    }

    public void setListBooks(List<Book> mListBooks) {
        this.mListBooks = mListBooks;
    }

    public String getTitle() {
        return mTitle != null ? mTitle : "";
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Integer getCategoryId() {
        return mCategoryId;
    }
}

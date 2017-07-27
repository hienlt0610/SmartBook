package smartbook.hutech.edu.smartbook.model;

import com.google.gson.annotations.SerializedName;

import smartbook.hutech.edu.smartbook.common.BaseModel;

/**
 * Created by hienl on 7/27/2017.
 */

public class BookResponse extends BaseModel {
    @SerializedName("book")
    private Book mBook;

    public Book getBook() {
        return mBook;
    }

    public void setBook(Book book) {
        mBook = book;
    }
}

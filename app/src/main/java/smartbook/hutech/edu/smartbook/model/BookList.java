package smartbook.hutech.edu.smartbook.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import smartbook.hutech.edu.smartbook.common.BaseModel;

/**
 * Created by hienl on 7/27/2017.
 */

public class BookList extends BaseModel {
    @SerializedName("books")
    private List<Book> mBooks;

    public List<Book> getBooks() {
        return mBooks;
    }

    public void setBooks(List<Book> books) {
        mBooks = books;
    }
}

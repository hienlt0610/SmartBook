package smartbook.hutech.edu.smartbook.model.eventbus;
/*
 * Created by Nhat Hoang on 09/07/2017.
 */

import smartbook.hutech.edu.smartbook.model.Book;

public class MessageEvent {
    Book book;

    public MessageEvent(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}

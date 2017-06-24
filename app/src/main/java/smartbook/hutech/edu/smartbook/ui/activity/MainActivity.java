package smartbook.hutech.edu.smartbook.ui.activity;

import android.os.Bundle;

import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.model.Page;
import smartbook.hutech.edu.smartbook.ui.fragment.StoreFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * Init activity
     */
    private void init() {
        addFragment(new StoreFragment(), R.id.actMain_flContainer, true);
        Book book = new Book();
        for (int i = 0; i < 100; i++) {
            book.getPageList().add(new Page());
        }
        BookReaderActivity.start(this, book);
    }


    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }

}

package smartbook.hutech.edu.smartbook.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.BookReaderPagerAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.common.view.ExtendedViewPager;
import smartbook.hutech.edu.smartbook.ui.fragment.PageFragment;

/**
 * Created by hienl on 6/22/2017.
 */

public class BookReaderActivity extends BaseActivity {
    @BindView(R.id.activityBookViewer_viewpager)
    ExtendedViewPager mViewPager;

    private BookReaderPagerAdapter mBookDetailPageAdapter;

//    @BindView(R.id.img_book)
//    BookImageView mBookImageView;

    @Override
    protected int getResId() {
        return R.layout.activity_book_viewer;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mBookDetailPageAdapter = new BookReaderPagerAdapter(getSupportFragmentManager());
        mBookDetailPageAdapter.addPage(new PageFragment());
        mBookDetailPageAdapter.addPage(new PageFragment());
        mBookDetailPageAdapter.addPage(new PageFragment());
        mBookDetailPageAdapter.addPage(new PageFragment());
        mBookDetailPageAdapter.addPage(new PageFragment());
        mBookDetailPageAdapter.addPage(new PageFragment());
        mBookDetailPageAdapter.addPage(new PageFragment());
        mViewPager.setAdapter(mBookDetailPageAdapter);
    }
}

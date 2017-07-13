package smartbook.hutech.edu.smartbook.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.PhotoPagerAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;

/**
 * Created by hienl on 7/12/2017.
 */

public class PagePreviewActivity extends BaseActivity {

    private static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";
    private static final String EXTRA_IMAGE_URL_LIST = "EXTRA_IMAGE_URL_LIST";

    @BindView(R.id.act_page_preview_img)
    ImageView mImgView;
    @BindView(R.id.act_page_preview_viewpage)
    ViewPager mViewPage;

    private String mImgUrl;
    private ArrayList<String> mListImage = new ArrayList<>();

    public static Intent newIntent(Context context, String imgUrl) {
        Intent intent = new Intent(context, PagePreviewActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, imgUrl);
        return intent;
    }

    public static Intent newIntent(Context context, List<String> imgUrl) {
        Intent intent = new Intent(context, PagePreviewActivity.class);
        intent.putStringArrayListExtra(EXTRA_IMAGE_URL_LIST, (ArrayList<String>) imgUrl);
        return intent;
    }

    @Override
    protected int getResId() {
        return R.layout.activity_image_viewer;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mImgUrl = intent.getStringExtra(EXTRA_IMAGE_URL);
        mListImage = intent.getStringArrayListExtra(EXTRA_IMAGE_URL_LIST);
        if (mImgUrl != null) {
            setVisibilityViewPager(false);
            Glide.with(this).load(mImgUrl).into(mImgView);
        } else if (mListImage != null) {
            setVisibilityViewPager(true);
            PhotoPagerAdapter pagerAdapter = new PhotoPagerAdapter(this, mListImage);
            mViewPage.setAdapter(pagerAdapter);
        }
    }

    private void setVisibilityViewPager(boolean enable) {
        mImgView.setVisibility(enable ? View.GONE : View.VISIBLE);
        mViewPage.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.act_page_preview_close)
    public void onViewClicked() {
        onBackPressed();
    }

    //@Override
    //public boolean dispatchTouchEvent(MotionEvent ev) {
    //    if (ev.getAction() == MotionEvent.ACTION_UP) {
    //        this.finish();
    //    }
    //    return super.dispatchTouchEvent(ev);
    //}
}

package smartbook.hutech.edu.smartbook.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.BaseActivity;

/**
 * Created by hienl on 7/12/2017.
 */

public class PagePreviewActivity extends BaseActivity {

    private static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";

    @BindView(R.id.act_page_preview_img)
    ImageView mImgView;

    private String mImgUrl;

    public static Intent newIntent(Context context, String imgUrl) {
        Intent intent = new Intent(context, PagePreviewActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, imgUrl);
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
        //Load image
        Glide.with(this).load(mImgUrl).into(mImgView);
    }

    private void initData() {
        Intent intent = getIntent();
        mImgUrl = intent.getStringExtra(EXTRA_IMAGE_URL);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            this.finish();
        }
        return super.dispatchTouchEvent(ev);
    }
}

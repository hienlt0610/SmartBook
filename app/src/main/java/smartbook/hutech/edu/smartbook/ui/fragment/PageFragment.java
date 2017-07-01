package smartbook.hutech.edu.smartbook.ui.fragment;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import java.io.File;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.BaseFragment;
import smartbook.hutech.edu.smartbook.common.view.bookview.BookImageView;
import smartbook.hutech.edu.smartbook.model.Page;

/**
 * Created by hienl on 6/24/2017.
 */
@RuntimePermissions
public class PageFragment extends BaseFragment {
    private static final String EXTRA_PAGE = "EXTRA_PAGE";
    @BindView(R.id.fragmentPage_imgBook)
    BookImageView mBookImageView;
    private Page mPage;

    public static PageFragment newInstance(Page page) {
        Bundle args = new Bundle();
        String json = new Gson().toJson(page);
        args.putString(EXTRA_PAGE, json);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_page;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Init data
        Bundle bundle = getArguments();
        boolean hasPageData = bundle != null && bundle.containsKey(EXTRA_PAGE);
        if (hasPageData) {
            String string = bundle.getString(EXTRA_PAGE);
            mPage = new Gson().fromJson(string, Page.class);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PageFragmentPermissionsDispatcher.loadImageWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void loadImage() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "iHHd0zf.jpg");
        Glide.with(this).load(file)
                .asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mBookImageView.setImageBitmap(resource);
            }
        });
    }

    /**
     * Check if Image is Zooming
     *
     * @return
     */
    public boolean isZoomed() {
        if (mBookImageView != null) {
            return mBookImageView.isZoomed();
        }
        return false;
    }

    /**
     * Zoom image to normal
     */
    public void zoomToNormal() {
        if (mBookImageView != null) {
            mBookImageView.resetZoom();
        }
    }

    public boolean isHighlight() {
        if (mBookImageView != null) {
            return mBookImageView.isHighlightMode();
        }
        return false;
    }

    public BookImageView getBookImageView() {
        return mBookImageView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PageFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}

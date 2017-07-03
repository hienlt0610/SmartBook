package smartbook.hutech.edu.smartbook.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.BaseFragment;
import smartbook.hutech.edu.smartbook.common.interfaces.IBookViewAction;
import smartbook.hutech.edu.smartbook.common.interfaces.ISwipePage;
import smartbook.hutech.edu.smartbook.common.view.bookview.BookImageView;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookItemModel;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookPageModel;
import smartbook.hutech.edu.smartbook.utils.FileUtils;
import timber.log.Timber;

/**
 * Created by hienl on 6/24/2017.
 */
public class PageFragment extends BaseFragment implements IBookViewAction {
    private static final String EXTRA_PAGE = "EXTRA_PAGE";
    private static final String EXTRA_ROOT_FOLDER = "EXTRA_ROOT_FOLDER";
    @BindView(R.id.fragmentPage_imgBook)
    BookImageView mBookImageView;
    private BookPageModel mPage;
    private String mRootFolder;

    public static PageFragment newInstance(BookPageModel page, String rootFolder) {
        Bundle args = new Bundle();
        String json = new Gson().toJson(page);
        args.putString(EXTRA_PAGE, json);
        args.putString(EXTRA_ROOT_FOLDER, rootFolder);
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
            mPage = new Gson().fromJson(string, BookPageModel.class);
        }
        boolean hasRootFolder = bundle != null && bundle.containsKey(EXTRA_ROOT_FOLDER);
        if (hasRootFolder) {
            mRootFolder = bundle.getString(EXTRA_ROOT_FOLDER);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadImage();
        loadListItem();

        //Set listener
        mBookImageView.setBookActionListener(this);
    }

    private void loadListItem() {
        mBookImageView.setListBookItem(mPage.getItems());
    }

    public void loadImage() {
        File bookFile = FileUtils.getFileByPath(mRootFolder);
        File imageFile = FileUtils.separatorWith(bookFile, mPage.getImagePath());
        Timber.d(imageFile.getAbsolutePath());
        Glide.with(this).load(imageFile)
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
    public void onInputClick(final int position) {
        BookItemModel item = mPage.getItems().get(position);
        String answer = mBookImageView.getItemResult(position);
        new MaterialDialog.Builder(getActivity())
                .title("Nhập câu trả lời")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(null, answer, true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        mBookImageView.setItemResult(input.toString(), position);
                    }
                }).show();
    }

    @Override
    public void onAudioClick(int position) {
        BookItemModel item = mPage.getItems().get(position);
        Toast.makeText(getContext(), "Play audio: " + item.getData().get(0), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectBoxClick(final int position) {
        BookItemModel item = mPage.getItems().get(position);
        new MaterialDialog.Builder(getActivity())
                .title("Chọn câu trả lời đúng")
                .items(item.getData())
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int pos, CharSequence text) {
                        mBookImageView.setItemResult(text.toString(), position);
                    }
                }).show();
    }

    @Override
    public void onNavigationClick(int position) {
        BookItemModel item = mPage.getItems().get(position);
        List<Object> data = item.getData();
        if (data.size() > 0) {
            int page = (int) Math.round((double) data.get(0));
            if (getActivity() instanceof ISwipePage) {
                ((ISwipePage) getActivity()).swipePage(page);
            }
        }
    }
}

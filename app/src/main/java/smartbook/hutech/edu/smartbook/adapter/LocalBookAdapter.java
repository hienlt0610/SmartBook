package smartbook.hutech.edu.smartbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.Common;
import smartbook.hutech.edu.smartbook.common.Constant;
import smartbook.hutech.edu.smartbook.database.Download;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookInfoModel;
import smartbook.hutech.edu.smartbook.utils.FileUtils;
import smartbook.hutech.edu.smartbook.utils.StringUtils;

/**
 * Created by hienl on 7/13/2017.
 */

public class LocalBookAdapter extends RecyclerArrayAdapter<Download> {

    private TypeShow mTypeShow = TypeShow.Grid;

    public LocalBookAdapter(Context context, TypeShow typeShow) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        if (mTypeShow == TypeShow.Grid) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_local_grid_book, parent, false);
            return new GridViewHolder(view);
        } else {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_local_list_book, parent, false);
            return new ListViewHolder(view);
        }
    }

    class GridViewHolder extends BaseViewHolder<Download> {
        @BindView(R.id.itemBook_imgBook)
        RoundedImageView mImvBook;
        @BindView(R.id.itemBook_tvBookTitle)
        TextView mTvTitle;

        public GridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Download data) {
            super.setData(data);
            String title = data.getTitle();
            if (StringUtils.isEmpty(title)) {
                title = "No title";
            }
            mTvTitle.setText(title);
            Glide.clear(mImvBook);
            Glide.with(getContext())
                    .load(getCoverPath(data.getBid()))
                    .placeholder(R.drawable.default_book)
                    .fitCenter()
                    .into(mImvBook);
        }
    }

    class ListViewHolder extends BaseViewHolder<Download> {
        @BindView(R.id.itemBook_imgBook)
        RoundedImageView mImvBook;
        @BindView(R.id.itemBook_tvBookTitle)
        TextView mTvTitle;
        @BindView(R.id.itemBook_tvBookAuthor)
        TextView mTvAuthor;
        @BindView(R.id.itemBook_tvBookDescription)
        TextView mTvDescription;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Download data) {
            super.setData(data);
            //Set title
            String title = data.getTitle();
            if (StringUtils.isEmpty(title)) {
                title = "No title";
            }
            mTvTitle.setText(title);
            //Set image
            Glide.clear(mImvBook);
            Glide.with(getContext())
                    .load(getCoverPath(data.getBid()))
                    .placeholder(R.drawable.default_book)
                    .fitCenter()
                    .into(mImvBook);
            //Set author
            mTvAuthor.setText("Tác giả: " + data.getAuthor());
            //Set description
            mTvDescription.setText(data.getDescription());
        }
    }

    private File getCoverPath(int bookId) {
        BookInfoModel infoBook = Common.getInfoOfBook(String.valueOf(bookId));
        File folderOfBook = Common.getFolderOfBook(String.valueOf(bookId));
        File coverFile;
        if (infoBook != null) {
            coverFile = FileUtils.separatorWith(folderOfBook, infoBook.getCover());
        } else {
            coverFile = FileUtils.separatorWith(folderOfBook, Constant.COVER_FILE_NAME);
        }
        return coverFile;
    }

    public void setTypeShow(TypeShow typeShow) {
        mTypeShow = typeShow;
    }

    public TypeShow getTypeShow() {
        return mTypeShow;
    }

    public enum TypeShow {
        Grid, List
    }
}

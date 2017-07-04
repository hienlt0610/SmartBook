package smartbook.hutech.edu.smartbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.Common;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookPageModel;
import smartbook.hutech.edu.smartbook.utils.FileUtils;

/**
 * Created by hienl on 6/26/2017.
 */

public class TableOfContentAdapter extends RecyclerArrayAdapter<BookPageModel> {

    File bookFolder;

    public TableOfContentAdapter(Context context, String bookId, List<BookPageModel> objects) {
        super(context, objects);
        bookFolder = Common.getFolderOfBook(bookId);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_table_of_content_page, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<BookPageModel> {
        @BindView(R.id.itemTableContent_img_page)
        ImageView mImagePage;
        @BindView(R.id.itemTableContent_tv_page_number)
        TextView mTvPage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(BookPageModel data) {
            super.setData(data);
            String pageNum = String.valueOf(data.getPageIndex());
            mTvPage.setText(pageNum);
            Glide.with(getContext())
                    .load(FileUtils.separatorWith(bookFolder, data.getImagePath()))
                    .fitCenter()
                    .into(mImagePage);
        }
    }
}

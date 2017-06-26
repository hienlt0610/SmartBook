package smartbook.hutech.edu.smartbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.model.Page;

/**
 * Created by hienl on 6/26/2017.
 */

public class TableOfContentAdapter extends RecyclerArrayAdapter<Page> {

    public TableOfContentAdapter(Context context, List<Page> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_table_of_content_page, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<Page> {
        @BindView(R.id.itemTableContent_img_page)
        ImageView mImagePage;
        @BindView(R.id.itemTableContent_tv_page_number)
        TextView mTvPage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Page data) {
            super.setData(data);
            String pageNum = String.valueOf(getAdapterPosition() + 1);
            mTvPage.setText(pageNum);
        }
    }
}

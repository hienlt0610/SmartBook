package smartbook.hutech.edu.smartbook.adapter;
/*
 * Created by Nhat Hoang on 24/06/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.model.Book;

public class BookAdapter extends RecyclerArrayAdapter<Book> {


    public BookAdapter(Context context) {
        super(context);
    }

    public BookAdapter(Context context, List<Book> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book,parent,false);
        return new BookViewHolder(view);
    }

    public class BookViewHolder extends BaseViewHolder<Book> {

        @BindView(R.id.itemBook_imgBook)
        RoundedImageView imgBook;
        @BindView(R.id.itemBook_tvBookTitle)
        TextView tvBookTitle;

        @Override
        public void setData(Book data) {
            super.setData(data);
            Glide.with(getContext()).load(data.getCover()).into(imgBook);
            tvBookTitle.setText(data.getTitle());
        }

        public BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}

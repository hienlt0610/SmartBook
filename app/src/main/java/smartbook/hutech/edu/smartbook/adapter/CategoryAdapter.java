package smartbook.hutech.edu.smartbook.adapter;
/*
 * Created by Nhat Hoang on 24/06/2017.
 */

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.interfaces.BookListener;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.model.Category;

public class CategoryAdapter extends RecyclerArrayAdapter<Category> {

    private BookListener onBookListener;

    public CategoryAdapter(Context context) {
        super(context);
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_category,parent,false);
        return new CategoryViewHolder(view);
    }
    public Book getBook(int posCate, int posBook){
        Category category = getItem(posCate);
        if(category != null){
            List<Book> bookList = category.getListBooks();
            if(posBook < bookList.size()){
                return bookList.get(posBook);
            }
        }
        return null;
    }

    public void setOnBookListener(BookListener onBookListener) {
        this.onBookListener = onBookListener;
    }

    public class CategoryViewHolder extends BaseViewHolder<Category> {

        @BindView(R.id.itemCategory_tvCategoryName)
        TextView tvCategoryName;
        @BindView(R.id.itemCategory_rvBooks)
        EasyRecyclerView rvBooks;
        BookAdapter bookAdapter;

        @Override
        public void setData(Category data) {
            super.setData(data);
            tvCategoryName.setText(data.getTitle());
            bookAdapter.clear();
            bookAdapter.addAll(data.getListBooks());
        }

        CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            rvBooks.setHorizontalScrollBarEnabled(false);
            bookAdapter = new BookAdapter(getContext());
            rvBooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvBooks.setAdapter(bookAdapter);
            bookAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (onBookListener != null) {
                        onBookListener.onBookItemClick(getAdapterPosition(), position);
                    }
                }
            });
        }

        @OnClick(R.id.itemCategory_tvMore)
        public void onClickMore(View view){
            if (onBookListener != null) {
                onBookListener.onClickMore(getAdapterPosition());
            }
        }
    }
}

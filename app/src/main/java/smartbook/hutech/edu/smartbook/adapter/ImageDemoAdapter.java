package smartbook.hutech.edu.smartbook.adapter;
/*
 * Created by Nhat Hoang on 07/07/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartbook.hutech.edu.smartbook.R;

public class ImageDemoAdapter extends RecyclerArrayAdapter<String> {


    public ImageDemoAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_demo,parent,false);
        return new ImageDemoAdapter.ImageDemoHolder(view);
    }

    class ImageDemoHolder extends BaseViewHolder<String> {
        @BindView(R.id.itemImageDemo_imgBook)
        RoundedImageView imgBook;

        @Override
        public void setData(String data) {
            super.setData(data);
            Glide.with(getContext()).load(data).into(imgBook);
        }

        ImageDemoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}

package smartbook.hutech.edu.smartbook.adapter;
/*
 * Created by Nhat Hoang on 12/07/2017.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.utils.StringUtils;

public class PhotoPagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<String> data = new ArrayList<>();

    public PhotoPagerAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String url = data.get(position);
        View itemView = layoutInflater.inflate(R.layout.item_view_pager, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.itemViewPager_imgPhoto);
        if (StringUtils.isURL(url)) {
            imageView.setImageDrawable(null);
            Glide.with(context).load(url).into(imageView);
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof LinearLayout)
            container.removeView((LinearLayout) object);
    }
}

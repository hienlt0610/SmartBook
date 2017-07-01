package smartbook.hutech.edu.smartbook.common.view.bookview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.CircleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.model.HighlightConfig;

/**
 * Created by hienl on 7/1/2017.
 */

public class HighlightConfigDialog implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MaterialDialog.SingleButtonCallback {

    private static final int MIN_STROKE_WIDTH = 5;

    @BindView(R.id.dialogHighligh_gridview)
    GridView mGridView;
    @BindView(R.id.dialogHighligh_tv_size)
    TextView mTvSize;
    @BindView(R.id.dialogHighligh_seek_size)
    SeekBar mSeekBarSize;


    private int[] mColors;
    private int mCircleSize;
    private int mSelectedIndex = 0;
    private int mStrokeWidth = MIN_STROKE_WIDTH;
    private MaterialDialog mMaterialDialog;
    private OnClickListener mOkCallback;

    public HighlightConfigDialog(@NonNull Context context) {
        mMaterialDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_highlight_config, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(this)
                .build();
        initColorList();
        mCircleSize = context.getResources().getDimensionPixelSize(R.dimen._32dp);
        initDialog(mMaterialDialog.getCustomView());
    }

    private void initDialog(View view) {
        ButterKnife.bind(this, view);

        //Init view
        mGridView.setAdapter(new ColorAdapter());
        mSeekBarSize.setMax(100 - MIN_STROKE_WIDTH);
        mTvSize.setText(mStrokeWidth + "px");
        //Set listener
        mSeekBarSize.setOnSeekBarChangeListener(this);
    }

    private void initColorList() {
        mColors = new int[]{Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.GREEN, Color.YELLOW};
    }

    @Override
    public void onClick(View v) {
        if (v instanceof CircleView) {
            mSelectedIndex = (int) v.getTag();
            ((BaseAdapter) mGridView.getAdapter()).notifyDataSetChanged();
        }
    }

    /**
     * Call when seekbar was changed
     *
     * @param seekBar  Seekbar
     * @param progress Current progress changed
     * @param fromUser N/A
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mStrokeWidth = progress + MIN_STROKE_WIDTH;
        mTvSize.setText(mStrokeWidth + "px");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setOkCallback(OnClickListener okCallback) {
        mOkCallback = okCallback;
    }

    /**
     * Dialog onclick event
     *
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        if (which == DialogAction.POSITIVE && mOkCallback != null) {
            mOkCallback.onClick(this);
        }
    }

    /**
     * Get stroke with slide in dialog
     *
     * @return
     */
    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    /**
     * Get the selected color in dialog
     *
     * @return color value
     */
    public int getSelectedColor() {
        return mColors[mSelectedIndex];
    }

    /**
     * Show dialog
     */
    public void show() {
        mMaterialDialog.show();
    }

    public void setDefaultConfig(HighlightConfig config) {
        if (config != null) {
            mStrokeWidth = config.getStoreWidth();
            setColor(config.getColor());

            mSeekBarSize.setProgress(mStrokeWidth - MIN_STROKE_WIDTH);
        }
    }

    private void setColor(int color) {
        mSelectedIndex = 0;
        for (int i = 0; i < mColors.length; i++) {
            if (mColors[i] == color) {
                mSelectedIndex = i;
                break;
            }
        }
    }

    public interface OnClickListener {
        void onClick(HighlightConfigDialog dialog);
    }

    private class ColorAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mColors.length;
        }

        @Override
        public Object getItem(int position) {
            return mColors[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new CircleView(mMaterialDialog.getContext());
                convertView.setLayoutParams(new GridView.LayoutParams(mCircleSize, mCircleSize));
            }
            CircleView child = (CircleView) convertView;
            boolean isSelected = (position == mSelectedIndex);
            child.setSelected(isSelected);
            child.setBackgroundColor(mColors[position]);
            child.setTag(position);
            child.setOnClickListener(HighlightConfigDialog.this);
            return child;
        }
    }
}

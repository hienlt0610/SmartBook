package smartbook.hutech.edu.smartbook.common.view.bookview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.Constant;
import smartbook.hutech.edu.smartbook.common.interfaces.IBookViewAction;
import smartbook.hutech.edu.smartbook.model.HighlightConfig;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookItemModel;
import smartbook.hutech.edu.smartbook.model.bookviewer.CoordinateModel;
import smartbook.hutech.edu.smartbook.utils.BitmapUtils;
import smartbook.hutech.edu.smartbook.utils.StringUtils;

/**
 * Created by hienl on 6/24/2017.
 */

public class BookImageView extends TouchImageView implements IBookViewAction {
    float[] values = new float[9];
    private List<Rect> mRectList;
    private PointF last = new PointF();
    private BrushType mBrushType = BrushType.NONE;
    private Paint mLinePaint;
    private Paint mBitmapPaint;
    private Paint mEarsePaint;
    private HighlightConfig mHighlightConfig;
    private Canvas mCanvasHighlight;
    private Bitmap mBitmapHighlight;
    private Path mPathTemp;
    private Path mPath;
    private boolean mIsCleared;
    private List<BookItemModel> mListBookItem;
    private Bitmap mBmpAudio;
    private Bitmap mBmpPlaying;
    private IBookViewAction mIBookViewAction;
    private Paint mCornerPaint;
    private Paint mFieldBgPaint;
    private Paint mTextPaint;

    private int mCornerWidth = 7;
    private int mCornerRound = 10;
    private int mTextSize = 55;
    private float mScale;
    private SparseArray<String> mAnswers;
    private Rect mBound;
    private int mItemFocus;
    private boolean mIsHighlightSaveAble;
    private int mPlayingPos = -1;

    public BookImageView(Context context) {
        super(context);
        init();
    }

    public BookImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mRectList = new ArrayList<>();
        mAnswers = new SparseArray<>();
        mBound = new Rect();
        initResourceDraw();
    }


    private void initResourceDraw() {
        mHighlightConfig = new HighlightConfig();
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setDither(true);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setFilterBitmap(true);
        mBitmapPaint.setDither(true);

        mEarsePaint = new Paint();
        mEarsePaint.setAlpha(0);
        mEarsePaint.setStrokeJoin(Paint.Join.ROUND);
        mEarsePaint.setStrokeCap(Paint.Cap.ROUND);
        mEarsePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mEarsePaint.setStyle(Paint.Style.STROKE);
        mEarsePaint.setAntiAlias(true);

        mCornerPaint = new Paint();
        mCornerPaint.setAntiAlias(true);
        mCornerPaint.setColor(ContextCompat.getColor(getContext(), R.color.material_color_amber_800));
        mCornerPaint.setStyle(Paint.Style.STROKE);

        mFieldBgPaint = new Paint();
        mFieldBgPaint.setAntiAlias(true);
        mFieldBgPaint.setColor(Color.WHITE);
        mFieldBgPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);

        /* Set the options */
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opts.inScaled = false; /* Flag for no scalling */
        mBmpAudio = BitmapFactory.decodeResource(getResources(), R.drawable.ic_audio, opts);
        mBmpPlaying = BitmapFactory.decodeResource(getResources(), R.drawable.ic_playing, opts);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF curr = new PointF(event.getX(), event.getY());
        PointF imgPoint = transformCoordTouchToBitmap(event.getX(), event.getY(), false);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last.set(curr);
                if (mBrushType == BrushType.HIGHLIGHT) {
                    mIsHighlightSaveAble = true;
                    mIsCleared = false;
                    mPathTemp = new Path();
                    mPath = new Path();
                    mPathTemp.moveTo(event.getX(), event.getY());
                    mPath.moveTo(imgPoint.x, imgPoint.y);
                } else if (mBrushType == BrushType.EARSE) {
                    mIsHighlightSaveAble = true;
                    mPath = new Path();
                    mPath.moveTo(imgPoint.x, imgPoint.y);
                } else {
                    //Detect item click
                    for (int i = 0; i < mRectList.size(); i++) {
                        if (mRectList.get(i).contains((int) imgPoint.x, (int) imgPoint.y)) {
                            BookItemModel item = mListBookItem.get(i);
                            boolean isFocusAble = item.getItemType().equals(Constant.SELECT_TYPE) || item.getItemType().equals(Constant.INPUT_TYPE);
                            if (isFocusAble) {
                                mItemFocus = i;
                                invalidate();
                            }
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mBrushType == BrushType.HIGHLIGHT) {
                    mPathTemp.lineTo(event.getX(), event.getY());
                    mPath.lineTo(imgPoint.x, imgPoint.y);
                    invalidate();
                } else if (mBrushType == BrushType.EARSE) {
                    mPath.lineTo(imgPoint.x, imgPoint.y);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mBrushType == BrushType.NONE) {
                    //Remove focus
                    mItemFocus = -1;
                    invalidate();
                    boolean isMoved = (Math.abs(last.x - curr.x) > 10 && Math.abs(last.y - curr.y) > 10);
                    if (isMoved) {
                        break;
                    }

                    //Detect item click
                    for (int i = 0; i < mRectList.size(); i++) {
                        if (mRectList.get(i).contains((int) imgPoint.x, (int) imgPoint.y)) {
                            BookItemModel item = mListBookItem.get(i);
                            switch (item.getItemType()) {
                                case Constant.INPUT_TYPE:
                                    onInputClick(i);
                                    break;
                                case Constant.AUDIO_TYPE:
                                    onAudioClick(i);
                                    break;
                                case Constant.SELECT_TYPE:
                                    onSelectBoxClick(i);
                                    break;
                                case Constant.NAVIGATE_TYPE:
                                    onNavigationClick(i);
                                    break;
                            }
                            break;
                        }
                    }
                } else if (mBrushType == BrushType.HIGHLIGHT) {
                    //Draw path to image
                    mLinePaint.setStrokeWidth(mHighlightConfig.getStoreWidth());
                    mLinePaint.setColor(mHighlightConfig.getColor());
                    mLinePaint.setAlpha(alpha(50));
                    mCanvasHighlight.drawPath(mPath, mLinePaint);
                    mPathTemp = null;
                    invalidate();
                }
                break;

        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mScale = getScale();
        onDrawItem(canvas);
        if (mBitmapHighlight != null) {
            canvas.drawBitmap(mBitmapHighlight, matrix, mBitmapPaint);
        }
        onDrawHighlight(canvas);
        onEarseHighlight();
    }

    private void onEarseHighlight() {
        if (!isEarseMode()) {
            return;
        }
        if (mCanvasHighlight != null) {
            mEarsePaint.setStrokeWidth(mHighlightConfig.getStoreWidth());
            mCanvasHighlight.drawPath(mPath, mEarsePaint);
        }
    }

    private void onDrawHighlight(Canvas canvas) {
        if (isHighlightMode() && mPathTemp != null) {
            mLinePaint.setStrokeWidth(mHighlightConfig.getStoreWidth() * getScale());
            mLinePaint.setColor(mHighlightConfig.getColor());
            mLinePaint.setAlpha(alpha(50));
            canvas.drawPath(mPathTemp, mLinePaint);
        }
    }

    private void onDrawItem(Canvas canvas) {
        int i = 0;
        for (Rect rect : mRectList) {
            BookItemModel item = mListBookItem.get(i);
            PointF from = pointFromBitmapPixel(rect.left, rect.top);
            PointF to = pointFromBitmapPixel(rect.right, rect.bottom);
            switch (item.getItemType()) {
                case Constant.INPUT_TYPE:
                    drawFieldInputItem(canvas, from, to, i);
                    break;
                case Constant.AUDIO_TYPE:
                    drawAudioItem(canvas, from, to, i);
                    break;
                case Constant.SELECT_TYPE:
                    drawSelectItem(canvas, from, to, i);
                    break;
                case Constant.NAVIGATE_TYPE:
                    drawNavigateItem(canvas, from, to, i);
                    break;
            }
            i++;
        }
    }

    /**
     * Draw navigate item
     *
     * @param canvas   Canvas
     * @param from     Draw from position (Calculated according to the original image)
     * @param to       Draw to position (Calculated according to the original image)
     * @param position Index of item in list item
     */
    private void drawNavigateItem(Canvas canvas, PointF from, PointF to, int position) {

    }

    /**
     * Draw select item
     *
     * @param canvas   Canvas
     * @param from     Draw from position (Calculated according to the original image)
     * @param to       Draw to position (Calculated according to the original image)
     * @param position Index of item in list item
     */
    private void drawSelectItem(Canvas canvas, PointF from, PointF to, int position) {
        RectF rectF = new RectF(from.x, from.y, to.x, to.y);
        int strokeWidth = (int) (mCornerWidth * mScale);
        int cornerRound = (int) (mCornerRound * mScale);
        mCornerPaint.setStrokeWidth(strokeWidth);

        int cornerColor;
        if (mItemFocus == position) {
            cornerColor = ContextCompat.getColor(getContext(), R.color.material_color_indigo_400);
        } else {
            cornerColor = ContextCompat.getColor(getContext(), R.color.material_color_amber_800);
        }
        mCornerPaint.setColor(cornerColor);

        canvas.drawRoundRect(rectF, cornerRound, cornerRound, mFieldBgPaint);
        canvas.drawRoundRect(rectF, cornerRound, cornerRound, mCornerPaint);

        //Draw result text
        String result = mAnswers.get(position);
        if (StringUtils.isNotEmpty(result)) {
            int textSize = (int) (mTextSize * mScale);
            mTextPaint.setTextSize(textSize);
            mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            mTextPaint.getTextBounds(result, 0, result.length(), mBound);

            float fromTextX = from.x + ((rectF.width() - mBound.width()) / 2);
            float fromTextY = from.y + mBound.height() + ((rectF.height() - mBound.height()) / 2);
            canvas.drawText(result, fromTextX, fromTextY, mTextPaint);
        }
    }

    /**
     * Draw audio item
     *
     * @param canvas   Canvas
     * @param from     Draw from position (Calculated according to the original image)
     * @param to       Draw to position (Calculated according to the original image)
     * @param position Index of item in list item
     */
    private void drawAudioItem(Canvas canvas, PointF from, PointF to, int position) {
        RectF rect = new RectF(from.x, from.y, to.x, to.y);
        Bitmap bitmap = position == mPlayingPos ? mBmpPlaying : mBmpAudio;
        canvas.drawBitmap(bitmap, null, rect, mBitmapPaint);
    }

    /**
     * Draw field input item
     *
     * @param canvas   Canvas
     * @param from     Draw from position (Calculated according to the original image)
     * @param to       Draw to position (Calculated according to the original image)
     * @param position Index of item in list item
     */
    private void drawFieldInputItem(Canvas canvas, PointF from, PointF to, int position) {
        RectF rectF = new RectF(from.x, from.y, to.x, to.y);
        int strokeWidth = (int) (mCornerWidth * mScale);
        int cornerRound = (int) (mCornerRound * mScale);
        mCornerPaint.setStrokeWidth(strokeWidth);

        int cornerColor;
        if (mItemFocus == position) {
            cornerColor = ContextCompat.getColor(getContext(), R.color.material_color_indigo_400);
        } else {
            cornerColor = ContextCompat.getColor(getContext(), R.color.material_color_amber_800);
        }
        mCornerPaint.setColor(cornerColor);

        canvas.drawRoundRect(rectF, cornerRound, cornerRound, mFieldBgPaint);
        canvas.drawRoundRect(rectF, cornerRound, cornerRound, mCornerPaint);

        //Draw result text
        String result = mAnswers.get(position);
        if (StringUtils.isNotEmpty(result)) {
            int textSize = (int) (mTextSize * mScale);
            mTextPaint.setTextSize(textSize);
            mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            mTextPaint.getTextBounds(result, 0, result.length(), mBound);

            float fromTextX = from.x + 10 * mScale;
            float fromTextY = from.y + mBound.height() + ((rectF.height() - mBound.height()) / 2);
            canvas.drawText(result, fromTextX, fromTextY, mTextPaint);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (mBitmapHighlight == null) {
            mBitmapHighlight = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
            mCanvasHighlight = new Canvas(mBitmapHighlight);
            mCanvasHighlight.drawColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    /**
     * Get point from px and py int bitmap
     *
     * @param pX Px
     * @param pY Py
     * @return Point
     */
    private PointF pointFromBitmapPixel(float pX, float pY) {
        return transformCoordBitmapToTouch(pX, pY);
    }

    public BrushType getBrushType() {
        return mBrushType;
    }

    public void setBrushType(BrushType brushType) {
        mBrushType = brushType;
        isEnable = (brushType == BrushType.NONE);
    }

    public boolean isHighlightMode() {
        return mBrushType == BrushType.HIGHLIGHT;
    }

    public boolean isEarseMode() {
        return mBrushType == BrushType.EARSE;
    }

    public HighlightConfig getHighlightConfig() {
        return mHighlightConfig;
    }

    public void setHighlightConfig(HighlightConfig highlightConfig) {
        mHighlightConfig = highlightConfig;
    }

    private float getScale() {
        matrix.getValues(values);
        int width = (int) (values[Matrix.MSCALE_X] * mBitmapHighlight.getWidth());
        return width / (float) mBitmapHighlight.getWidth();
    }

    /**
     * Clear old highlight
     */
    public void clearHighlight() {
        if (mCanvasHighlight != null) {
            mCanvasHighlight.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mIsCleared = true;
            mIsHighlightSaveAble = true;
            invalidate();
        }
    }

    /**
     * Check if is empty highlight
     *
     * @return true if highligh empty
     */
    public boolean isEmptyHighlight() {
        return mIsCleared || BitmapUtils.isTransparentOrEmpty(mBitmapHighlight);
    }

    public Bitmap getHighlightBitmap() {
        return mBitmapHighlight;
    }

    /**
     * Calculator alpha with percent
     *
     * @param percent   Percent alpha
     * @return alpha    alpha
     */
    private int alpha(int percent) {
        return ((percent * 255) / 100);
    }

    public void setListBookItem(List<BookItemModel> bookItem) {
        mListBookItem = bookItem;
        mapListItem();
        invalidate();
    }

    public void setItemResult(String result, int position) {
        mAnswers.put(position, result);
        invalidate();
    }

    public String getItemResult(int position) {
        if (mAnswers.indexOfKey(position) != -1) {
            return mAnswers.get(position);
        }
        return null;
    }

    private void mapListItem() {
        if (mListBookItem == null) {
            return;
        }
        mRectList.clear();
        for (int i = 0; i < mListBookItem.size(); i++) {
            BookItemModel item = mListBookItem.get(i);
            CoordinateModel c = item.getCoordinate();
            if (c != null) {
                Rect rect = new Rect(c.getFromX(), c.getFromY(), c.getToX(), c.getToY());
                mRectList.add(rect);
            }
        }
    }

    @Override
    public void onInputClick(int position) {
        if (mIBookViewAction != null) {
            mIBookViewAction.onInputClick(position);
        }
    }

    @Override
    public void onAudioClick(int position) {
        if (mIBookViewAction != null) {
            mIBookViewAction.onAudioClick(position);
        }
    }

    @Override
    public void onSelectBoxClick(int position) {
        if (mIBookViewAction != null) {
            mIBookViewAction.onSelectBoxClick(position);
        }
    }

    @Override
    public void onNavigationClick(int position) {
        if (mIBookViewAction != null) {
            mIBookViewAction.onNavigationClick(position);
        }
    }

    public void setBookActionListener(IBookViewAction IBookViewAction) {
        mIBookViewAction = IBookViewAction;
    }

    public void setBitmapHighlight(Bitmap bitmapHighlight) {
        mBitmapHighlight = bitmapHighlight;
        if (mBitmapHighlight != null) {
            mCanvasHighlight = new Canvas(mBitmapHighlight);
            invalidate();
        }
    }

    public void setAnswers(SparseArray<String> answers) {
        if (answers != null) {
            mAnswers = answers;
        }
        invalidate();
    }

    public boolean isHighlightSaveAble() {
        return mIsHighlightSaveAble;
    }

    public void setPlayingPos(int pos) {
        this.mPlayingPos = pos;
        invalidate();
    }

    public enum BrushType {
        HIGHLIGHT, EARSE, NONE
    }
}

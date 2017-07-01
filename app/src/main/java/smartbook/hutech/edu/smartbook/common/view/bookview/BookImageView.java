package smartbook.hutech.edu.smartbook.common.view.bookview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import smartbook.hutech.edu.smartbook.model.HighlightConfig;
import smartbook.hutech.edu.smartbook.utils.BitmapUtils;
import smartbook.hutech.edu.smartbook.utils.SystemUtils;

/**
 * Created by hienl on 6/24/2017.
 */

public class BookImageView extends TouchImageView {
    float[] values = new float[9];
    private List<RectF> mRectFs;
    private List<String> mMessage;
    private int selected = -1;
    private PointF last = new PointF();
    private BrushType mBrushType = BrushType.NONE;
    private Paint mPaintLine;
    private Paint mPaintHighlight;
    private Paint mPaintEarse;
    private HighlightConfig mHighlightConfig;
    private float mDensityScreen;
    private Canvas mCanvasHighlight;
    private Bitmap mBitmapHighlight;
    private Path mPathTemp;
    private Path mPath;
    private boolean mIsCleared;

    public BookImageView(Context context) {
        super(context);
        init();
    }

    public BookImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDensityScreen = getContext().getResources().getDisplayMetrics().density;
        mRectFs = new ArrayList<>();
        mDensityScreen = SystemUtils.getDensityScreen(getContext());
        initResourceDraw();
        initDummyData();
    }

    private void initResourceDraw() {
        mHighlightConfig = new HighlightConfig();
        mPaintLine = new Paint();
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setDither(true);
        mPaintLine.setStrokeCap(Paint.Cap.ROUND);

        mPaintHighlight = new Paint();
        mPaintHighlight.setAntiAlias(true);
        mPaintHighlight.setFilterBitmap(true);
        mPaintHighlight.setDither(true);

        mPaintEarse = new Paint();
        mPaintEarse.setAlpha(0);
        mPaintEarse.setStrokeJoin(Paint.Join.ROUND);
        mPaintEarse.setStrokeCap(Paint.Cap.ROUND);
        mPaintEarse.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaintEarse.setStyle(Paint.Style.STROKE);
        mPaintEarse.setAntiAlias(true);
    }

    private void initDummyData() {
        mRectFs.add(new RectF(597, 569, 703, 632));
        mRectFs.add(new RectF(758, 536, 853, 592));
        mRectFs.add(new RectF(702, 649, 796, 697));
        mRectFs.add(new RectF(707, 722, 823, 764));
        mRectFs.add(new RectF(528, 1272, 706, 1458));
        mRectFs.add(new RectF(847, 1272, 1026, 1451));
        mRectFs.add(new RectF(646, 1872, 835, 2078));

        mMessage = new ArrayList<>();
        mMessage.add("Mắt trái");
        mMessage.add("Mắt phải");
        mMessage.add("Mũi");
        mMessage.add("Miệng");
        mMessage.add("Vếu trái");
        mMessage.add("Vếu phải");
        mMessage.add("aHihi!!!");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF curr = new PointF(event.getX(), event.getY());
        PointF imgPoint = transformCoordTouchToBitmap(event.getX(), event.getY(), false);
        float scale = getScale();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last.set(curr);
                if (mBrushType == BrushType.HIGHLIGHT) {
                    mIsCleared = false;
                    mPathTemp = new Path();
                    mPath = new Path();
                    if (imgPoint.x >= 0 && imgPoint.y >= 0) {
                        mPathTemp.moveTo(event.getX(), event.getY());
                        mPath.moveTo(imgPoint.x, imgPoint.y);
                    }
                } else if (mBrushType == BrushType.EARSE) {
                    mPath = new Path();
                    mPath.moveTo(imgPoint.x, imgPoint.y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mBrushType == BrushType.HIGHLIGHT) {
                    if (imgPoint.x > 0 && imgPoint.y > 0) {
                        mPathTemp.lineTo(event.getX(), event.getY());
                        mPath.lineTo(imgPoint.x, imgPoint.y);
                        invalidate();
                    }
                } else if (mBrushType == BrushType.EARSE) {
                    mPath.lineTo(imgPoint.x, imgPoint.y);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mBrushType == BrushType.NONE) {
                    boolean isMoved = (Math.abs(last.x - curr.x) > 10 && Math.abs(last.y - curr.y) > 10);
                    if (isMoved) {
                        break;
                    }

                    //Detect item click
                    for (int i = 0; i < mRectFs.size(); i++) {
                        if (mRectFs.get(i).contains(imgPoint.x, imgPoint.y)) {
                            Toast.makeText(getContext(), mMessage.get(i), Toast.LENGTH_SHORT).show();
                            selected = i;
                            invalidate();
                            break;
                        }
                    }
                } else if (mBrushType == BrushType.HIGHLIGHT) {
                    //Draw path to image
                    mPaintLine.setStrokeWidth(mHighlightConfig.getStoreWidth());
                    mPaintLine.setColor(mHighlightConfig.getColor());
                    mPaintLine.setAlpha(alpha(50));
                    mCanvasHighlight.drawPath(mPath, mPaintLine);
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
//        onDrawItem(canvas);
        if (mBitmapHighlight != null) {
            canvas.drawBitmap(mBitmapHighlight, matrix, mPaintHighlight);
        }
        onDrawHighlight(canvas);
        onEarseHighlight(canvas);
    }

    private void onEarseHighlight(Canvas canvas) {
        if (!isEarseMode()) {
            return;
        }
        mPaintEarse.setStrokeWidth(mHighlightConfig.getStoreWidth());
        mCanvasHighlight.drawPath(mPath, mPaintEarse);
    }

    private void onDrawHighlight(Canvas canvas) {
        if (isHighlightMode() && mPathTemp != null) {
            mPaintLine.setStrokeWidth(mHighlightConfig.getStoreWidth() * getScale());
            mPaintLine.setColor(mHighlightConfig.getColor());
            mPaintLine.setAlpha(alpha(50));
            canvas.drawPath(mPathTemp, mPaintLine);
        }
    }

    private void onDrawItem(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        for (RectF rectF : mRectFs) {
            if (selected != -1 && mRectFs.get(selected) == rectF) {
                paint.setColor(Color.GREEN);
            }
            PointF from = pointFromBitmapPixel(rectF.left, rectF.top);
            PointF to = pointFromBitmapPixel(rectF.right, rectF.bottom);
            canvas.drawRect(from.x, from.y, to.x, to.y, paint);
            paint.setColor(Color.RED);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmapHighlight = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        mCanvasHighlight = new Canvas(mBitmapHighlight);
        mCanvasHighlight.drawColor(Color.TRANSPARENT);
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
        float valueX = values[Matrix.MTRANS_X];
        int width = (int) (values[Matrix.MSCALE_X] * mBitmapHighlight.getWidth());
        float scale = width / (float) mBitmapHighlight.getWidth();
        return scale;
    }

    /**
     * Clear old highlight
     */
    public void clearHighlight() {
        if (mCanvasHighlight != null) {
            mCanvasHighlight.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mIsCleared = true;
            invalidate();
        }
    }

    public boolean isEmptyHighlight() {
        if (mIsCleared) {
            return true;
        }
        return BitmapUtils.isTransparentOrEmpty(mBitmapHighlight);
    }

    public Bitmap getHighlightBitmap() {
        return mBitmapHighlight;
    }

    /**
     * Calculator alpha with percent
     *
     * @param percent
     * @return alpha
     */
    private int alpha(int percent) {
        return ((percent * 255) / 100);
    }

    public enum BrushType {
        HIGHLIGHT, EARSE, NONE
    }
}

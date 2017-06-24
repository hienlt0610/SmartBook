package smartbook.hutech.edu.smartbook.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by hienl on 6/24/2017.
 */

public class BookImageView extends TouchImageView {
    private float density;
    private List<RectF> mRectFs;
    private List<String> mMessage;

    private int selected = -1;
    private PointF last = new PointF();

    public BookImageView(Context context) {
        super(context);
        init();
    }

    public BookImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        density = getContext().getResources().getDisplayMetrics().density;
        mRectFs = new ArrayList<>();
        initDummyData();
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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last.set(curr);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                boolean isMoved = (Math.abs(last.x - curr.x) > 10 && Math.abs(last.y - curr.y) > 10);
                if (isMoved) {
                    break;
                }
                float density = getContext().getResources().getDisplayMetrics().density;
                PointF pointF = transformCoordTouchToBitmap(event.getX(), event.getY(), true);
                //Update click pos
//                pointF.set(pointF.x / density, pointF.y / density);

                //Detect item click
                for (int i = 0; i < mRectFs.size(); i++) {
                    if (mRectFs.get(i).contains(pointF.x, pointF.y)) {
                        Toast.makeText(getContext(), mMessage.get(i), Toast.LENGTH_SHORT).show();
                        selected = i;
                        invalidate();
                        break;
                    }
                }
                break;

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawItem(canvas);
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
        Timber.d(getDrawable().getIntrinsicWidth() + " - " + getDrawable().getIntrinsicHeight());
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
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
}

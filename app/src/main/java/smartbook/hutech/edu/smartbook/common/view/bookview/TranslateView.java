package smartbook.hutech.edu.smartbook.common.view.bookview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import smartbook.hutech.edu.smartbook.model.bookviewer.Word;
import smartbook.hutech.edu.smartbook.utils.DimenUtils;

/**
 * Created by hienl on 7/8/2017.
 */

public class TranslateView extends ImageView {

    private List<Word> mWordList;
    private List<Word> mSelectedWords;
    private Paint mWordLinePaint;
    private Paint mDrawPaint;
    private int mWordLineWidth = 2; //dp
    private int mDrawLineWidth = 15; //dp
    private Path mPath;
    private OnTextTranslateSelectListener mTranslateSelectListener;


    public TranslateView(Context context) {
        super(context);
        init();
    }

    public TranslateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TranslateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initResource();
    }

    private void initResource() {
        initDimen();
        mWordLinePaint = new Paint();
        mWordLinePaint.setColor(Color.GREEN);
        mWordLinePaint.setStrokeWidth(5);
        mWordLinePaint.setStyle(Paint.Style.STROKE);
        mWordLinePaint.setAntiAlias(true);

        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeWidth(mDrawLineWidth);
        mDrawPaint.setColor(Color.BLACK);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setAlpha(alpha(40));
    }

    private void initDimen() {
        mWordLineWidth = DimenUtils.dpToPx(getContext(), mWordLineWidth);
        mDrawLineWidth = DimenUtils.dpToPx(getContext(), mDrawLineWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWordList != null && mWordList.size() > 0) {
            mWordLinePaint.setColor(Color.GREEN);
            for (int i = 0; i < mWordList.size(); i++) {
                Word word = mWordList.get(i);
                canvas.drawRect(word.getRect(), mWordLinePaint);
            }
        }

        if (mSelectedWords != null && mSelectedWords.size() > 0) {
            mWordLinePaint.setColor(Color.RED);
            for (int i = 0; i < mSelectedWords.size(); i++) {
                Word word = mSelectedWords.get(i);
                canvas.drawRect(word.getRect(), mWordLinePaint);
            }
        }

        if (mPath != null) {
            canvas.drawPath(mPath, mDrawPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                mSelectedWords = new ArrayList<>();
                mPath.moveTo(touchX, touchY);
                for (Word word : mWordList) {
                    int posX = (int) touchX;
                    int posY = (int) touchY;

                    boolean isSelected =
                            word.getRect().contains(posX - (mDrawLineWidth / 2), posY - (mDrawLineWidth / 2))
                                    || word.getRect().contains(posX + (mDrawLineWidth / 2), posY + (mDrawLineWidth / 2));
                    boolean isExist = mSelectedWords.contains(word);
                    if (isSelected && !isExist) {
                        mSelectedWords.add(word);
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(touchX, touchY);
                for (Word word : mWordList) {
                    int posX = (int) touchX;
                    int posY = (int) touchY;

                    boolean isSelected =
                            word.getRect().contains(posX - (mDrawLineWidth / 2), posY - (mDrawLineWidth / 2))
                                    || word.getRect().contains(posX + (mDrawLineWidth / 2), posY + (mDrawLineWidth / 2));
                    boolean isExist = mSelectedWords.contains(word);
                    if (isSelected && !isExist) {
                        mSelectedWords.add(word);
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                handleSelectedWord();
                mPath = null;
                mSelectedWords = null;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    //Handle selected word from screen
    private void handleSelectedWord() {
        if (mSelectedWords.size() > 0 && mTranslateSelectListener != null) {
            //Sort list text
            Collections.sort(mSelectedWords, new Comparator<Word>() {
                @Override
                public int compare(Word w1, Word w2) {
                    if (w1.getIndex() == w2.getIndex()) {
                        return 0;
                    }
                    return w2.getIndex() < w1.getIndex() ? 1 : -1;
                }
            });
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mSelectedWords.size(); i++) {
                if (i != 0) {
                    builder.append(" ");
                }
                builder.append(mSelectedWords.get(i).getValue());
            }
            mTranslateSelectListener.onTextSelected(builder.toString());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setListWord(List<Word> wordList) {
        mWordList = wordList;
        invalidate();
    }

    /**
     * Calculator alpha with percent
     *
     * @param percent Percent alpha
     * @return alpha    alpha
     */
    private int alpha(int percent) {
        return ((percent * 255) / 100);
    }

    public void setTranslateSelectListener(OnTextTranslateSelectListener translateSelectListener) {
        mTranslateSelectListener = translateSelectListener;
    }

    public interface OnTextTranslateSelectListener {
        void onTextSelected(String text);
    }
}

package smartbook.hutech.edu.smartbook.model.bookviewer;

import android.graphics.Rect;

/**
 * Created by hienl on 7/8/2017.
 */

public class Word {
    private int index;
    private String value;
    private Rect rect;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}

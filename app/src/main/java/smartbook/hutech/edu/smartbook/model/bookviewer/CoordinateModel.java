package smartbook.hutech.edu.smartbook.model.bookviewer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hienl on 7/2/2017.
 */

public class CoordinateModel {
    @SerializedName("fromX")
    private int fromX;
    @SerializedName("fromY")
    private int fromY;
    @SerializedName("toX")
    private int toX;
    @SerializedName("toY")
    private int toY;

    public int getFromX() {
        return fromX;
    }

    public void setFromX(int fromX) {
        this.fromX = fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public void setFromY(int fromY) {
        this.fromY = fromY;
    }

    public int getToX() {
        return toX;
    }

    public void setToX(int toX) {
        this.toX = toX;
    }

    public int getToY() {
        return toY;
    }

    public void setToY(int toY) {
        this.toY = toY;
    }
}

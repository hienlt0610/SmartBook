package smartbook.hutech.edu.smartbook.model;

import android.graphics.Color;

/**
 * Created by hienl on 7/1/2017.
 */

public class HighlightConfig {
    private int color = Color.BLACK;
    private int storeWidth = 50;

    public HighlightConfig color(int color) {
        this.color = color;
        return this;
    }

    public HighlightConfig storeWidth(int width) {
        this.storeWidth = width;
        return this;
    }


    public int getColor() {
        return color;
    }

    public int getStoreWidth() {
        return storeWidth;
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}

package smartbook.hutech.edu.smartbook.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by hienl on 7/2/2017.
 */

public class BitmapUtils {
    public static boolean isTransparentOrEmpty(Bitmap bitmap) {
        if (bitmap == null || (bitmap.getWidth() == 0 || bitmap.getHeight() == 0)) {
            return false;
        }
        bitmap = resize(bitmap, 100, 100);
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                if (bitmap.getPixel(x, y) != Color.TRANSPARENT) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}

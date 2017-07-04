package smartbook.hutech.edu.smartbook.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hienl on 7/2/2017.
 */

public class BitmapUtils {
    public static boolean isTransparentOrEmpty(Bitmap bitmap) {
        if (bitmap == null || (bitmap.getWidth() == 0 || bitmap.getHeight() == 0)) {
            return false;
        }
        bitmap = scale(bitmap, 100, 100);
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                if (bitmap.getPixel(x, y) != Color.TRANSPARENT) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Bitmap scale(Bitmap image, int maxWidth, int maxHeight) {
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

    public static boolean saveBitmap(Bitmap bitmap, File file) {
        if (bitmap == null || file == null) {
            return false;
        }
        boolean isSaveSuccess = false;
        try {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file, false); //here is set your file path where you want to save or also here you can set file object directly
                isSaveSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                isSaveSuccess = false;
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSaveSuccess;
    }
}

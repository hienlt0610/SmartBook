package smartbook.hutech.edu.smartbook.utils;

import android.content.Context;

/**
 * Created by hienl on 6/26/2017.
 */

public class DimenUtils {

    /**
     * Convert px to sp
     *
     * @param context Context
     * @param pxValue px
     * @return sp
     */
    public static int pxToSp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * Convert sp to px
     *
     * @param context Context
     * @param spValue sp value
     * @return px value
     */
    public static int spToPx(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Convert px to dp
     *
     * @param context Context
     * @param pxValue Px value
     * @return Dp value
     */
    public static int pxToDp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Convert dp to px
     *
     * @param context Context
     * @param dpValue dp value
     * @return px value
     */
    public static int dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

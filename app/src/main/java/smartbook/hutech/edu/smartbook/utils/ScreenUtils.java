package smartbook.hutech.edu.smartbook.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by hienl on 7/2/2017.
 */

public class ScreenUtils {
    /**
     * Get screen rotation
     *
     * @param activity Activity
     * @return Screen rotation angle
     */
    public static int getScreenRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            default:
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
    }

    /**
     * Detect vertical screen
     *
     * @param context Context
     * @return true if screen is vertical
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Detect horizontal screen
     *
     * @param context Context
     * @return true if screen is horizontal
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Get screen width
     *
     * @param context Context
     * @return screen width
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * Get screen height
     *
     * @param context Context
     * @return screen height
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * Get density screen
     *
     * @param context Context
     * @return density screen
     */
    public static float getDensityScreen(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}

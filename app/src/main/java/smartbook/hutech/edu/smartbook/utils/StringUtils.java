package smartbook.hutech.edu.smartbook.utils;

import android.util.Patterns;

/**
 * Created by hienl on 6/26/2017.
 */

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static boolean isURL(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }
}

package smartbook.hutech.edu.smartbook.common;

/**
 * Created by hienl on 7/2/2017.
 */

public class Common {

    public static String getUUIDFromBookId(int bookId) {
        int uuid = System.identityHashCode(bookId);
        return String.valueOf(uuid);
    }

}

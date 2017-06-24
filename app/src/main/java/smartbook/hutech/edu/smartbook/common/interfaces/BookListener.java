package smartbook.hutech.edu.smartbook.common.interfaces;
/*
 * Created by Nhat Hoang on 24/06/2017.
 */

public interface BookListener {
    void onBookItemClick(int categoryPos, int bookPos);
    void onClickMore(int categoryPos);
}

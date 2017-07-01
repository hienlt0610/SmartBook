package smartbook.hutech.edu.smartbook.common.interfaces;
/*
 * Created by Nhat Hoang on 01/07/2017.
 */

public interface BaseListener<T> {
    void onSuccess(int reqCode, T data);
    void onFailed(int reqCode, String errorMessage);
}

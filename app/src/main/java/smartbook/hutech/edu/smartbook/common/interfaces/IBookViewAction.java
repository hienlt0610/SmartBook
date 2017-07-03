package smartbook.hutech.edu.smartbook.common.interfaces;

/**
 * Created by hienl on 7/3/2017.
 */

public interface IBookViewAction {
    void onInputClick(int position);

    void onAudioClick(int position);

    void onSelectBoxClick(int position);

    void onNavigationClick(int position);
}

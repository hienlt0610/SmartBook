package smartbook.hutech.edu.smartbook.common;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.File;

import smartbook.hutech.edu.smartbook.model.bookviewer.BookInfoModel;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookListPageModel;
import smartbook.hutech.edu.smartbook.utils.FileUtils;
import timber.log.Timber;

/**
 * Created by hienl on 7/2/2017.
 */

public class Common {

    public static String getUUIDFromBookId(int bookId) {
        int uuid = System.identityHashCode(bookId);
        return String.valueOf(uuid);
    }

    public static File getFolderOfRootBook() {
        if (!FileUtils.hasSDCard())
            return null;
        File external = Environment.getExternalStorageDirectory();
        return FileUtils.separatorWith(external, Constant.ROOT_FOLDER_NAME);
    }

    public static File getFolderOfBook(String folderName) {
        File folderRootBook = getFolderOfRootBook();
        return FileUtils.separatorWith(folderRootBook, folderName);
    }

    public static BookInfoModel getInfoOfBook(String folderName) {
        BookInfoModel bookInfo = null;
        File folderBook = getFolderOfBook(folderName);
        File fileInfo = FileUtils.separatorWith(folderBook, Constant.BOOK_INFO_FILE_NAME);
        if (FileUtils.isFileExists(fileInfo)) {
            try {
                String jsonData = FileUtils.readFileToString(fileInfo, "utf-8");
                bookInfo = new Gson().fromJson(jsonData, BookInfoModel.class);
            } catch (JsonParseException e) {
                Timber.e(e);
            }

        }
        return bookInfo;
    }

    public static BookListPageModel getListPageOfBook(String folderName) {
        BookListPageModel listPageModel = null;
        File folderBook = getFolderOfBook(folderName);
        File fileListPage = FileUtils.separatorWith(folderBook, Constant.BOOK_ITEMS_FILE_NAME);
        if (FileUtils.isFileExists(fileListPage)) {
            try {
                String jsonData = FileUtils.readFileToString(fileListPage, "utf-8");
                listPageModel = new Gson().fromJson(jsonData, BookListPageModel.class);
            } catch (JsonParseException e) {
                Timber.e(e);
            }

        }
        return listPageModel;
    }

    public static boolean checkBookAvailable(String folderName) {
        BookInfoModel bookInfoModel = getInfoOfBook(folderName);
        BookListPageModel listPageModel = getListPageOfBook(folderName);
        if (bookInfoModel == null || listPageModel == null) {
            return false;
        }
        return true;
    }

}

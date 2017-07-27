package smartbook.hutech.edu.smartbook.database;

import smartbook.hutech.edu.smartbook.common.App;

/**
 * Created by hienl on 7/28/2017.
 */

public class MyRepository {
    public static void deleteAnswer(int bookId) {
        App.getApp().getDaoSession().getAnsweredDao().queryBuilder()
                .where(AnsweredDao.Properties.Bid.eq(bookId))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static void deleteBookmart(int bookId) {
        App.getApp().getDaoSession().getBookmarkedDao().queryBuilder()
                .where(BookmarkedDao.Properties.Bid.eq(bookId))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static void deleteBookDownload(int bookId) {
        App.getApp().getDaoSession().getDownloadDao().queryBuilder()
                .where(DownloadDao.Properties.Bid.eq(bookId))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static void deleteLatestPage(int bookId) {
        App.getApp().getDaoSession().getLatestPageDao().queryBuilder()
                .where(LatestPageDao.Properties.Bid.eq(bookId))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }
}

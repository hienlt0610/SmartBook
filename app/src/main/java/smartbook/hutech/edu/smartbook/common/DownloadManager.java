package smartbook.hutech.edu.smartbook.common;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.ArrayList;
import java.util.List;

import smartbook.hutech.edu.smartbook.R;

import static android.R.attr.id;

/**
 * Created by hienl on 7/13/2017.
 */

public class DownloadManager {
    private final static class HolderClass {
        private final static DownloadManager INSTANCE = new DownloadManager();
    }

    public static DownloadManager getImpl() {
        return HolderClass.INSTANCE;
    }

    private ArrayList<DownloadStatusUpdater> updaterList = new ArrayList<>();

    public void startDownload(final String url, final String path, boolean isParent, String tag) {
        int downloadId = FileDownloader.getImpl().create(url)
                .setPath(path, isParent)
                .setListener(lis)
                .setTag(tag)
                .start();
    }

    public void addUpdater(final DownloadStatusUpdater updater) {
        if (!updaterList.contains(updater)) {
            updaterList.add(updater);
        }
    }

    public boolean removeUpdater(final DownloadStatusUpdater updater) {
        return updaterList.remove(updater);
    }


    private FileDownloadListener lis = new FileDownloadListener() {
        private NotificationCompat.Builder mBuilder;
        private NotificationManager mNotificationManager = (NotificationManager) App.getApp().getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            update(task);
            mBuilder = new NotificationCompat.Builder(App.getApp().getApplicationContext())
                    .setSmallIcon(R.drawable.ic_download)
                    .setContentTitle(task.getUrl())
                    .setOngoing(true)
                    .setContentText("Kết nối máy chủ...");
            mNotificationManager.notify(task.getId(), mBuilder.build());
        }

        @Override
        protected void started(BaseDownloadTask task) {
            super.started(task);
            update(task);
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            update(task);
            mBuilder.setContentText("Đang tải...");
            mNotificationManager.notify(task.getId(), mBuilder.build());
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            update(task);
            int progress = (int) ((task.getSmallFileSoFarBytes() / ((float) task.getSmallFileTotalBytes())) * 100);
            mBuilder.setProgress(100, progress, false);
            mBuilder.setContentText("Đang tải: " + progress + "%");
            mNotificationManager.notify(task.getId(), mBuilder.build());
        }

        @Override
        protected void blockComplete(BaseDownloadTask task) {
            blockCompleted(task);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            update(task);
            mBuilder.setContentText("Download complete")
                    .setOngoing(false)
                    .setProgress(0, 0, false);
            mNotificationManager.notify(task.getId(), mBuilder.build());
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            update(task);
            mBuilder.setContentText("Download paused")
                    .setProgress(0, 0, false)
                    .setOngoing(false);
            mNotificationManager.notify(task.getId(), mBuilder.build());
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            update(task);
            mBuilder.setContentText("Download error")
                    .setOngoing(false)
                    .setProgress(0, 0, false);
            mNotificationManager.notify(task.getId(), mBuilder.build());
        }

        @Override
        protected void warn(BaseDownloadTask task) {
            update(task);
            mBuilder.setContentText(null)
                    .setOngoing(false)
                    .setProgress(0, 0, false);
            mNotificationManager.notify(id, mBuilder.build());
        }
    };

    private void update(final BaseDownloadTask task) {
        final List<DownloadStatusUpdater> updaterListCopy = (List<DownloadStatusUpdater>) updaterList.clone();
        for (DownloadStatusUpdater downloadStatusUpdater : updaterListCopy) {
            downloadStatusUpdater.update(task);
        }
    }

    private void blockCompleted(final BaseDownloadTask task) {
        final List<DownloadStatusUpdater> updaterListCopy = (List<DownloadStatusUpdater>) updaterList.clone();
        for (DownloadStatusUpdater downloadStatusUpdater : updaterListCopy) {
            downloadStatusUpdater.blockCompleted(task);
        }
    }

    public interface DownloadStatusUpdater {
        void update(BaseDownloadTask task);

        void blockCompleted(BaseDownloadTask task);
    }
}

package smartbook.hutech.edu.smartbook.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import smartbook.hutech.edu.smartbook.utils.CompressorUtils;
import smartbook.hutech.edu.smartbook.utils.FileUtils;

/**
 * Created by hienl on 7/13/2017.
 */

public class DownloadCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        FileDownloadModel fileDownload = FileDownloadBroadcastHandler.parseIntent(intent);
        List<File> files = null;
        try {
            File file = new File(fileDownload.getTargetFilePath());
            files = CompressorUtils.unzipFileByKeyword(file.getAbsolutePath(), file.getParent(), null);
            fileDownload.deleteTargetFile();
            FileUtils.rename(files.get(0), file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package smartbook.hutech.edu.smartbook.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.File;

import smartbook.hutech.edu.smartbook.utils.BitmapUtils;

/**
 * Created by hienl on 7/4/2017.
 */

public class TaskSaveBitmap extends AsyncTask<Bitmap, Void, Void> {

    private File mFile;

    public TaskSaveBitmap(File file) {
        this.mFile = file;
    }

    @Override
    protected Void doInBackground(Bitmap... params) {
        BitmapUtils.saveBitmap(params[0], mFile);
        return null;
    }
}

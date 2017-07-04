package smartbook.hutech.edu.smartbook.common;

import android.app.Application;

import com.mikepenz.iconics.Iconics;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import org.greenrobot.greendao.database.Database;

import smartbook.hutech.edu.smartbook.database.DaoMaster;
import smartbook.hutech.edu.smartbook.database.DaoSession;
import timber.log.Timber;

/**
 * Created by hienl on 6/24/2017.
 */

public class App extends Application {

    private static App instance;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        instance = this;
        Iconics.registerFont(new MaterialDesignIconic());
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "smartbook");
        Database db = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
//        daoMaster.dropAllTables(db, true);
//        daoMaster.createAllTables(db, true);
    }

    public static App getApp() {
        return instance;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

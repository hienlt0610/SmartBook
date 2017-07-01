package smartbook.hutech.edu.smartbook.common;

import android.app.Application;

import com.mikepenz.iconics.Iconics;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import timber.log.Timber;

/**
 * Created by hienl on 6/24/2017.
 */

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        instance = this;
        Iconics.registerFont(new MaterialDesignIconic());
    }

    public static App getApp(){
        return instance;
    }
}

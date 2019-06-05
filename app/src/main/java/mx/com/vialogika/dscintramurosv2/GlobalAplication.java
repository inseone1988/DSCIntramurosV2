package mx.com.vialogika.dscintramurosv2;

import android.app.Application;
import android.content.Context;

import net.gotev.uploadservice.UploadService;

public class GlobalAplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
    }

    public static Context getAppContext(){
        return appContext;
    }
}

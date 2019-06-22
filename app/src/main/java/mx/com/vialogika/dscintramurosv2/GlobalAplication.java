package mx.com.vialogika.dscintramurosv2;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import net.gotev.uploadservice.UploadService;

import io.fabric.sdk.android.Fabric;

public class GlobalAplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        Fabric.with(this,new Crashlytics());
    }

    public static Context getAppContext(){
        return appContext;
    }
}

package mx.com.vialogika.dscintramurosv2;

import android.app.Application;
import android.content.Context;

public class GlobalAplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return appContext;
    }
}

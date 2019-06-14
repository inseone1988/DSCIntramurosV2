package mx.com.vialogika.dscintramurosv2.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mx.com.vialogika.dscintramurosv2.GlobalAplication;

public class FileUtils {

    public static  File createImageFile()throws IOException {
        Context context        = GlobalAplication.getAppContext();
        String  timestamp     = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String  imageFileName = "DSCProfile_" + timestamp + "_";
        File    storageDir    = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()){
            storageDir.mkdir();
        }
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        return image;
    }

    public static  File createImageSignature()throws IOException {
        Context context        = GlobalAplication.getAppContext();
        String  timestamp     = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String  imageFileName = "DSCSignature_" + timestamp + "_";
        File    storageDir    = context.getExternalFilesDir("incident_signatures");
        if (!storageDir.exists()){
            storageDir.mkdir();
        }
        File image = File.createTempFile(imageFileName,".png",storageDir);
        return image;
    }

    public static boolean saveBitmap(Bitmap bitmap,File output, Bitmap.CompressFormat format){
        try(FileOutputStream out = new FileOutputStream(output)){
            bitmap.compress(format,100,out);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}

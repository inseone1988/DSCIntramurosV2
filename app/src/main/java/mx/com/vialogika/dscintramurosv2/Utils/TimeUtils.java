package mx.com.vialogika.dscintramurosv2.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static Date parse(String format,String datetime){
        SimpleDateFormat formated = new SimpleDateFormat(format, Locale.ENGLISH);
        try{
            return formated.parse(datetime);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return null;
    }
}

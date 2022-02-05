package aybici.parkourplugin;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {
    public static String getDateString(long dateLong) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new Date(dateLong);
        return formatter.format(date);
    }
}
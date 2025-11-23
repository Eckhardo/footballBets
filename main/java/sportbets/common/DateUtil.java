package sportbets.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
  private static  String datePattern = "dd/MM/yyyy HH:mm";

    public static String formatDate(Date date) {

        SimpleDateFormat df = new SimpleDateFormat(datePattern);
        return df.format(date);
    }
}

package sportbets.common;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class DateUtil {
  private static  String datePattern = "dd/MM/yyyy HH:mm";

    public static String formatDate(LocalDateTime date) {

        SimpleDateFormat df = new SimpleDateFormat(datePattern);
        return df.format(date);
    }
}

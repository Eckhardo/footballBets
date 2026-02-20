package sportbets.common;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final String datePattern = "dd/MM/yyyy HH:mm";

    public static String formatDate(LocalDateTime date) {

        SimpleDateFormat df = new SimpleDateFormat(datePattern);
        return df.format(date);
    }

    public static LocalDateTime formatDate(String englishDate) {
        // 1. Parse the input string (yyyy-MM-dd HH:mm )
        DateTimeFormatter englishFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(englishDate, englishFormatter);

    }
}

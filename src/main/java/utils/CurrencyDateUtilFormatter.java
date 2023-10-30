package utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;


public class CurrencyDateUtilFormatter {


    private static final Locale SPANISH_LOCALE = new Locale("es", "ES");
    private static CurrencyDateUtilFormatter instance;

    private CurrencyDateUtilFormatter() {
    }

    public static CurrencyDateUtilFormatter getInstance() {
        if (instance == null) {
            instance = new CurrencyDateUtilFormatter();
        }
        return instance;
    }


    public static String formatLocalDate(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(SPANISH_LOCALE);
        return dateTimeFormatter.format(date);
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(SPANISH_LOCALE);
        return dateTimeFormatter.format(dateTime);
    }

    public static String formatLocalCurrency(double money) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(SPANISH_LOCALE);
        return currencyFormat.format(money);
    }


}



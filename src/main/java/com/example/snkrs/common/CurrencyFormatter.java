package com.example.snkrs.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String format(BigDecimal price) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(price);
    }

    public static String formatRange(BigDecimal minPrice, BigDecimal maxPrice) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return String.format("%s - %s", formatter.format(minPrice), formatter.format(maxPrice));
    }
}

package com.WxLtechDev.peopleDBweb.web.formatter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Component
public class BigDecimalFormatter implements Formatter<BigDecimal> {

    @Override
    public BigDecimal parse(final String text, final Locale locale) throws ParseException {
        return null;
    }

    @Override
    public String print(final BigDecimal object, final Locale locale) {
        // This locale is identified by the browser.
        // That depends on where you are using this web.
        return NumberFormat.getCurrencyInstance(locale).format(object);
    }
}

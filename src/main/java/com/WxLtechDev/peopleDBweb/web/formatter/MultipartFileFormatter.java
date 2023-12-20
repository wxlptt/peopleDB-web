package com.WxLtechDev.peopleDBweb.web.formatter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Locale;

@Component
public class MultipartFileFormatter implements Formatter<MultipartFile> {
    @Override
    public MultipartFile parse(final String text, final Locale locale) throws ParseException {
        return null;
    }

    @Override
    public String print(final MultipartFile object, final Locale locale) {
        return object.getOriginalFilename();
    }
}

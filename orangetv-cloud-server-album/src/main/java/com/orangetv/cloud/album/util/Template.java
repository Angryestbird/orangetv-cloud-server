package com.orangetv.cloud.album.util;

import lombok.experimental.UtilityClass;
import lombok.var;

import java.util.regex.Pattern;

@UtilityClass
public class Template {

    Pattern TEMPLATE_PATTERN = Pattern.compile("\\{([^}]*)}");

    public String render(String template, Object... args) {
        var matcher = TEMPLATE_PATTERN.matcher(template);
        var stringBuffer = new StringBuffer();
        int i = 0;
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, args[i++].toString());
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(render("{},{}", "hello", "world"));
    }
}

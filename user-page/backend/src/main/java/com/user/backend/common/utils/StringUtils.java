package com.user.backend.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom String Utils
 */
public class StringUtils {

    /**
     * uri에 있는 숫자 덩어리를 추출하는 메소드
     *
     * @param uri uri
     * @return 추출된 numberList
     */
    public static List<String> extractNumberFromUri(String uri) {
        List<String> numberList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(uri);
        while (matcher.find()) {
            numberList.add(matcher.group());
        }
        return numberList;
    }
}

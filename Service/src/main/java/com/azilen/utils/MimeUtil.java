package com.azilen.utils;

import org.apache.commons.lang3.StringUtils;

import javax.activation.MimetypesFileTypeMap;

public class MimeUtil {
    public static String getMimeType(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        return fileTypeMap.getContentType(fileName);
    }
}

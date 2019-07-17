package com.github.pdf.tools.util;

import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.util.stream.Stream;

/**
 * @author wangdongbo
 * @since 2019/7/17.
 */
public class FileUtil {

    public static void deleteFile(String... filePaths) {
        if (ArrayUtils.isEmpty(filePaths)) {
            return;
        }
        Stream.of(filePaths).forEach(path -> new File(path).delete());
    }

}

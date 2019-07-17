package com.github.pdf.tools.properties;

import com.github.pdf.tools.exception.PDFException;

/**
 * @author wangdongbo
 * @since 2019/7/17.
 */
public class PhantomJsSupport {

    public static String getPath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "/js/windows/phantomjs.exe";
        } else if (os.contains("mac")) {
            return "/js/macosx/phantomjs";
        } else if (os.contains("linux")) {
            return judgeArch();
        }
        throw new PDFException("无法判断操作系统");
    }

    private static String judgeArch() {
        String arch = System.getProperty("os.arch").toLowerCase();
        if (arch.contains("64")) {
            return "/js/linux64/phantomjs";
        }
        return "/js/linux32/phantomjs";
    }

}

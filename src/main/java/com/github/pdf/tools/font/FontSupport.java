package com.github.pdf.tools.font;

import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author wangdongbo
 * @since 2019/7/11.
 */
public class FontSupport {

    private static final Map<String, XMLWorkerFontProvider> FONT_PROVIDER_MAP_5 = new ConcurrentHashMap<>(16);

    private static final Map<String, FontProvider> FONT_PROVIDER_MAP_7 = new ConcurrentHashMap<>(16);

    static {
        FONT_PROVIDER_MAP_5.put("systemDefaultFontProvider", new AsianFontProvider());
        FONT_PROVIDER_MAP_7.put("systemDefaultFontProvider", new FontProvider());
    }

    public static XMLWorkerFontProvider getFontProvider5(String path) {
        if (StringUtils.isEmpty(path)) {
            return FONT_PROVIDER_MAP_5.get("systemDefaultFontProvider");
        }
        if (FONT_PROVIDER_MAP_5.containsKey(path)) {
            return FONT_PROVIDER_MAP_5.get(path);
        }
        XMLWorkerFontProvider provider = new XMLWorkerFontProvider(path);
        FONT_PROVIDER_MAP_5.put(path, provider);
        return provider;
    }

    public static FontProvider getFontProvider7(String path) {
        if (StringUtils.isEmpty(path)) {
            return FONT_PROVIDER_MAP_7.get("systemDefaultFontProvider");
        }
        if (FONT_PROVIDER_MAP_7.containsKey(path)) {
            return FONT_PROVIDER_MAP_7.get(path);
        }
        File file = new File(path);
        if (!file.exists() || !file.isDirectory() || ArrayUtils.isEmpty(file.listFiles())) {
            return FONT_PROVIDER_MAP_7.get("systemDefaultFontProvider");
        }
        File[] files = file.listFiles();
        FontProvider fontProvider = new FontProvider();
        Stream.of(files).map(File::getAbsolutePath).forEach(fontProvider::addFont);
        FONT_PROVIDER_MAP_7.put(path, fontProvider);
        return fontProvider;
    }

}

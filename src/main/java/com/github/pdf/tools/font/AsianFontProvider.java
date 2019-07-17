package com.github.pdf.tools.font;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangdongbo
 * @since 2019/7/4.
 */
@Slf4j
public class AsianFontProvider extends XMLWorkerFontProvider {

    @Override
    public Font getFont(final String fontname, String encoding, float size, final int style) {
        try {
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            return new Font(bfChinese, size, style);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return super.getFont(fontname, encoding, size, style);
    }
}

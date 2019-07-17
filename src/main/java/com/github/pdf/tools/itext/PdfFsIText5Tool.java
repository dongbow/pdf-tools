package com.github.pdf.tools.itext;

import com.github.pdf.tools.exception.PDFException;
import com.github.pdf.tools.param.PdfParam;
import com.github.pdf.tools.template.AbstractTemplate;
import com.itextpdf.text.pdf.BaseFont;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author wangdongbo
 * @since 2019/7/12.
 */
public class PdfFsIText5Tool {

    public static PdfFsIText5Tool INSTANCE = null;

    static {
        synchronized (PdfFsIText5Tool.class) {
            if (INSTANCE == null) {
                INSTANCE = new PdfFsIText5Tool();
            }
        }
    }

    public String createPdfFile(PdfParam pdfParam) {
        String content = AbstractTemplate.getContent(pdfParam.getTemplateEnum(), pdfParam.getTemplatePath(), pdfParam.getTemplateName(), pdfParam.getData());
        pdfParam.fill();
        File file = getDefaultSavePathFile(pdfParam.getFileName());
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(content);

            ITextFontResolver fontResolver = renderer.getFontResolver();
            if (StringUtils.isNotEmpty(pdfParam.getFontPath())) {
                File[] files = new File(pdfParam.getFontPath()).listFiles();
                for (File t : files) {
                    fontResolver.addFont(t.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                }
            }
            renderer.layout();
            renderer.createPDF(os);
            return file.getAbsolutePath();
        } catch (Exception e) {
            throw new PDFException("PDF生成失败", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    private static File getDefaultSavePathFile(String fileName) {
        String classpath = PdfFsIText5Tool.class.getClassLoader().getResource("").getPath();
        String savePath = classpath + "pdf/" + fileName;
        File file = new File(savePath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        return file;
    }

}

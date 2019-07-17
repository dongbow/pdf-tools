package com.github.pdf.tools.itext;

import com.github.pdf.tools.exception.PDFException;
import com.github.pdf.tools.font.FontSupport;
import com.github.pdf.tools.param.Page;
import com.github.pdf.tools.param.PdfParam;
import com.github.pdf.tools.template.AbstractTemplate;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

/**
 * @author wangdongbo
 * @since 2019/7/3.
 */
public class PdfIText7Tool {

    public static PdfIText7Tool INSTANCE = null;

    static {
        synchronized (PdfIText7Tool.class) {
            if (INSTANCE == null) {
                INSTANCE = new PdfIText7Tool();
            }
        }
    }

    private PdfIText7Tool() { }

    public String createPdfFile(PdfParam pdfParam) throws PDFException {
        String content = AbstractTemplate.getContent(pdfParam.getTemplateEnum(), pdfParam.getTemplatePath(), pdfParam.getTemplateName(), pdfParam.getData());
        File file = getDefaultSavePathFile(pdfParam.getFileName());
        ConverterProperties c = new ConverterProperties();
        c.setFontProvider(FontSupport.getFontProvider7(pdfParam.getFontPath()));
        c.setCharset("utf-8");
        FileOutputStream outputStream = null;
        PdfDocument pdfDocument = null;
        try {
            outputStream = FileUtils.openOutputStream(file);
            pdfDocument = new PdfDocument(new PdfWriter(outputStream));
            pdfDocument.setDefaultPageSize(new PageSize(getRectangle(pdfParam.getPage())));
            HtmlConverter.convertToPdf(content, pdfDocument, c);
            return file.getAbsolutePath();
        } catch (Exception e) {
            throw new PDFException("pdf生成失败", e);
        } finally {
            if (pdfDocument != null) {
                pdfDocument.close();
            }
            IOUtils.closeQuietly(outputStream);
        }
    }

    private File getDefaultSavePathFile(String fileName) {
        String classpath = PdfIText7Tool.class.getClassLoader().getResource("").getPath();
        String savePath = classpath + "pdf/" + fileName;
        File file = new File(savePath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        return file;
    }

    private Rectangle getRectangle(Page page) {
        if (Objects.isNull(page)) {
            return PageSize.A4;
        }
        if (page.checkAll()) {
            return new Rectangle(page.getX(), page.getY(), page.getWidth(), page.getHeight());
        }
        if (page.check()) {
            return new Rectangle(page.getWidth(), page.getHeight());
        }
        return PageSize.A4;
    }

}

package com.github.pdf.tools.itext;

import com.github.pdf.tools.exception.PDFException;
import com.github.pdf.tools.font.FontSupport;
import com.github.pdf.tools.param.Page;
import com.github.pdf.tools.param.PdfParam;
import com.github.pdf.tools.template.AbstractTemplate;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author wangdongbo
 * @since 2019/7/3.
 */
public class PdfIText5Tool {

    public static PdfIText5Tool INSTANCE = null;

    static {
        synchronized (PdfIText5Tool.class) {
            if (INSTANCE == null) {
                INSTANCE = new PdfIText5Tool();
            }
        }
    }

    private PdfIText5Tool() { }

    public String createPdfFile(PdfParam pdfParam) {
        String content = AbstractTemplate.getContent(pdfParam.getTemplateEnum(), pdfParam.getTemplatePath(), pdfParam.getTemplateName(), pdfParam.getData());
        File file = getDefaultSavePathFile(pdfParam.getFileName());
        InputStream cssStream = getCssInputStream(pdfParam.getCssInputStream());
        Rectangle rectangle = getRectangle(pdfParam.getPage());
        FileOutputStream outputStream = null;
        Document document = null;
        try {
            outputStream = FileUtils.openOutputStream(file);
            document = new Document(rectangle);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(content.getBytes()),
                    cssStream,
                    Charset.forName("UTF-8"), FontSupport.getFontProvider5(pdfParam.getFontPath()));
            return file.getAbsolutePath();
        } catch (Exception e) {
            throw new PDFException("pdf生成失败", e);
        } finally {
            if (document != null) {
                document.close();
            }
            IOUtils.closeQuietly(outputStream);
        }
    }

    private File getDefaultSavePathFile(String fileName) {
        String classpath = PdfIText5Tool.class.getClassLoader().getResource("").getPath();
        String savePath = classpath + "pdf/" + fileName;
        File file = new File(savePath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        return file;
    }

    private InputStream getCssInputStream(InputStream cssInputStream) {
        if (Objects.nonNull(cssInputStream)) {
            return cssInputStream;
        }
        return XMLWorkerHelper.class.getResourceAsStream("/default.css");
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

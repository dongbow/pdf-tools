package com.github.pdf.tools;

import com.github.pdf.tools.exception.PDFException;
import com.github.pdf.tools.itext.PdfFsIText5Tool;
import com.github.pdf.tools.itext.PdfIText5Tool;
import com.github.pdf.tools.itext.PdfIText7Tool;
import com.github.pdf.tools.itext.sign.SignTool;
import com.github.pdf.tools.js.PhantomjsTool;
import com.github.pdf.tools.param.PdfParam;
import com.github.pdf.tools.util.FileUtil;

import java.io.File;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author wangdongbo
 * @since 2019/7/12.
 */
public class PdfTool {

    public static PdfTool INSTANCE = null;

    private static PdfIText5Tool ITEXT5TOOL = PdfIText5Tool.INSTANCE;

    private static PdfIText7Tool ITEXT7TOOL = PdfIText7Tool.INSTANCE;

    private static PhantomjsTool PHANTOMJSTOOL = PhantomjsTool.INSTANCE;

    private static PdfFsIText5Tool PDFFSITEXT5TOOL = PdfFsIText5Tool.INSTANCE;

    private static SignTool SIGNTOOL = SignTool.INSTANCE;

    static {
        synchronized (PdfTool.class) {
            if (INSTANCE == null) {
                INSTANCE = new PdfTool();
            }
        }
    }

    public String createPdf5(PdfParam pdfParam) {
        if (Objects.isNull(pdfParam) || !pdfParam.check()) {
            throw new PDFException("参数有误");
        }
        pdfParam.fill();
        String path = ITEXT5TOOL.createPdfFile(pdfParam);
        File file = new File(path);
        if (file.isDirectory() && file.delete()) {
            path = ITEXT5TOOL.createPdfFile(pdfParam);
        }
        String newPath = SIGNTOOL.sign(path, pdfParam.getSignParam());
        if (!Objects.equals(path, newPath)) {
            FileUtil.deleteFile(path);
        }
        return newPath;
    }

    public String createPdf7(PdfParam pdfParam) {
        if (Objects.isNull(pdfParam) || !pdfParam.check()) {
            throw new PDFException("参数有误");
        }
        pdfParam.fill();
        String path = ITEXT7TOOL.createPdfFile(pdfParam);
        File file = new File(path);
        if (file.isDirectory() && file.delete()) {
            path = ITEXT7TOOL.createPdfFile(pdfParam);
        }
        String newPath = SIGNTOOL.sign(path, pdfParam.getSignParam());
        if (!Objects.equals(path, newPath)) {
            FileUtil.deleteFile(path);
        }
        return newPath;
    }

    public String createPdfJs(PdfParam pdfParam) {
        if (Objects.isNull(pdfParam) || !pdfParam.check()) {
            throw new PDFException("参数有误");
        }
        pdfParam.fill();
        String path = PHANTOMJSTOOL.createPdfFile(pdfParam);
        String[] paths = path.split("%@%");
        File file = new File(paths[0]);
        if (file.isDirectory()) {
            Stream.of(paths).forEach(url -> new File(url).delete());
            path = PHANTOMJSTOOL.createPdfFile(pdfParam);
            paths = path.split("%@%");
        }
        String newPath = SIGNTOOL.sign(paths[0], pdfParam.getSignParam());
        if (!Objects.equals(paths[0], newPath)) {
            FileUtil.deleteFile(paths);
        }
        return newPath;
    }

    public String createPdf5Fs(PdfParam pdfParam) {
        if (Objects.isNull(pdfParam) || !pdfParam.check()) {
            throw new PDFException("参数有误");
        }
        pdfParam.fill();
        String path = PDFFSITEXT5TOOL.createPdfFile(pdfParam);
        File file = new File(path);
        if (file.isDirectory() && file.delete()) {
            path = PDFFSITEXT5TOOL.createPdfFile(pdfParam);
        }
        String newPath = SIGNTOOL.sign(path, pdfParam.getSignParam());
        if (!Objects.equals(path, newPath)) {
            FileUtil.deleteFile(path);
        }
        return newPath;
    }

}

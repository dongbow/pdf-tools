package com.github.pdf.tools.js;

import com.github.pdf.tools.exception.PDFException;
import com.github.pdf.tools.param.PdfParam;
import com.github.pdf.tools.properties.PhantomJsSupport;
import com.github.pdf.tools.template.AbstractTemplate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.naming.NoPermissionException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author wangdongbo
 * @since 2019/7/12.
 */
public class PhantomjsTool {

    public static String TMP_JS;
    public static PhantomjsTool INSTANCE = null;
    private static String PDF_JS = null;

    static {
        synchronized (PhantomjsTool.class) {
            if (INSTANCE == null) {
                INSTANCE = new PhantomjsTool();
            }
        }
        TMP_JS = PhantomjsTool.class.getResource("/").getPath().toString() + "phantomjs";
        PDF_JS = PhantomjsTool.class.getResource("/").getPath().toString() + "html2Pdf.js";
        if (TMP_JS.startsWith("file:")) {
            TMP_JS = TMP_JS.replace("file:", "");
        }
    }

    public String createPdfFile(PdfParam pdfParam) {
        String content = AbstractTemplate.getContent(pdfParam.getTemplateEnum(), pdfParam.getTemplatePath(), pdfParam.getTemplateName(), pdfParam.getData());
        String tmpHtmlPath = getDefaultSavePathFile(pdfParam.getFileName()).getAbsolutePath().replace(".pdf", ".html");
        try {
            FileUtils.writeByteArrayToFile(new File(tmpHtmlPath), content.getBytes(Charset.forName("utf-8")));
            String pdfPath = parseHtml2Pdf(pdfParam, tmpHtmlPath);
            return pdfPath + "%@%" + tmpHtmlPath;
        } catch (Exception e) {
            new File(tmpHtmlPath).delete();
            if (e instanceof NoPermissionException) {
                return createPdfFile(pdfParam);
            }
            throw new PDFException("html生成异常", e);
        }
    }

    private String parseHtml2Pdf(PdfParam pdfParam, String url) throws IOException, NoPermissionException {
        //执行phantomjs 生成js
        checkJsExec();
        checkJs();
        Runtime rt = Runtime.getRuntime();
        Float width = pdfParam.getPage().getWidth();
        Float height = pdfParam.getPage().getHeight();
        String outDir = getDefaultSavePathFile(pdfParam.getFileName()).getAbsolutePath();
        Process p = rt.exec(TMP_JS + " " + PDF_JS + " " + url + " " + width + " " + height + " " + outDir);
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sbf = new StringBuilder();
        String tmp;
        while ((tmp = br.readLine()) != null) {
            sbf.append(tmp);
        }
        String[] arr = sbf.toString().split("\\$");
        String result = null;
        for (String s : arr) {
            if (s.endsWith("pdf")) {
                result = s;
            }
        }
        if (StringUtils.isEmpty(result)) {
            throw new PDFException("生成失败");
        }
        IOUtils.closeQuietly(is);
        return result;
    }

    private File getDefaultSavePathFile(String fileName) {
        String classpath = PhantomjsTool.class.getClassLoader().getResource("").getPath();
        String savePath = classpath + "pdf/" + fileName;
        File file = new File(savePath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        return file;
    }

    private void checkJsExec() throws NoPermissionException {
        File file = new File(TMP_JS);
        if (file.exists()) {
            return;
        }
        String js = PhantomJsSupport.getPath();
        write(file, js);
        if (!new File(TMP_JS).canExecute()) {
            throw new NoPermissionException("文件无执行权限");
        }
    }

    private void checkJs() {
        File file = new File(PDF_JS);
        if (file.exists()) {
            return;
        }
        write(file, "/js/html2Pdf.js");
    }

    private void write(File file, String url) {
        InputStream stream = PhantomjsTool.class.getResourceAsStream(url);
        if (stream == null) {
            throw new PDFException("JS流异常");
        }
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc;
        try {
            while ((rc = stream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            FileUtils.writeByteArrayToFile(file, swapStream.toByteArray());
            Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new PDFException("JS流异常");
        } finally {
            IOUtils.closeQuietly(swapStream);
            IOUtils.closeQuietly(stream);
        }
    }

}
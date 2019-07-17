package com.github.pdf.tools;

import com.github.pdf.tools.param.Page;
import com.github.pdf.tools.param.PdfParam;
import com.github.pdf.tools.param.SignParam;
import com.github.pdf.tools.template.TemplateEnum;

/**
 * @author wangdongbo
 * @since 2019/7/4.
 */
public class PdfToolTest {

    public static void main(String[] args) throws Throwable {
        String templatePath = "/Users/dongbo/Documents/workspce/github/pdf-tools/src/test/resources/templates";
        Page page = Page.builder()
                .width(727.0F)
                .height(2730.0F)
                .build();
        SignParam signParam = SignParam.builder()
                .posX(480F)
                .posY(2360F)
                .sign(true)
                .picturePath("/Users/dongbo/Documents/workspce/github/pdf-tools/src/test/resources/img/1498295372220.jpg")
                .build();
        PdfParam pdfParam = PdfParam.builder()
                .templateEnum(TemplateEnum.VELOCITY)
                .templatePath(templatePath)
                .templateName("index.vm")
                .data(null)
                .page(page)
                .signParam(signParam)
                .build();
//        String path = PdfTool.INSTANCE.createPdf5(pdfParam);
//        String path = PdfTool.INSTANCE.createPdf7(pdfParam);
        String path = PdfTool.INSTANCE.createPdfJs(pdfParam);
//        String path = PdfTool.INSTANCE.createPdf5Fs(pdfParam);
        System.out.println(path);
    }

}

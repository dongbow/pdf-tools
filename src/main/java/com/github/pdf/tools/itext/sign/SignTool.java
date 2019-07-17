package com.github.pdf.tools.itext.sign;

import com.github.pdf.tools.exception.SignException;
import com.github.pdf.tools.param.SignParam;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

/**
 * @author wangdongbo
 * @since 2019/7/13.
 */
@Slf4j
public class SignTool {

    public static SignTool INSTANCE = null;

    static {
        synchronized (SignTool.class) {
            if (INSTANCE == null) {
                INSTANCE = new SignTool();
            }
        }
    }

    public String sign(String pdfPath, SignParam signParam) {
        if (Objects.isNull(signParam) || !signParam.getSign()) {
            return pdfPath;
        }
        String path = new File(pdfPath).getParent();
        String newPath = path + "/sign" + UUID.randomUUID().toString().replace("-", "").toLowerCase() + ".pdf";
        PdfReader pdfReader = null;
        PdfStamper stamper = null;
        try {
            pdfReader = new PdfReader(FileUtils.openInputStream(new File(pdfPath)));
            stamper = new PdfStamper(pdfReader, FileUtils.openOutputStream(new File(newPath)));
            float x;
            float y;
            int pageNo = 1;
            if (StringUtils.isNotEmpty(signParam.getKeyWord())) {
                AcroFields form = stamper.getAcroFields();
                pageNo = form.getFieldPositions(signParam.getKeyWord()).get(0).page;
                Rectangle signRect = form.getFieldPositions(signParam.getKeyWord()).get(0).position;
                x = signRect.getRight();
                y = signRect.getBottom();
            } else {
                signParam.checkPos();
                x = signParam.getPosX();
                y = signParam.getPosY();
            }

            Image image = Image.getInstance(signParam.getPicturePath());
            PdfContentByte under = stamper.getOverContent(pageNo);

            image.scalePercent(100 * signParam.getPicWidth() / image.getWidth(), 100 * signParam.getPicHeight() / image.getHeight());
            // 添加图片
            image.setAbsolutePosition(x, y);
            under.addImage(image);

            stamper.close();
            pdfReader.close();
            return newPath;
        } catch (Exception e) {
            throw new SignException("签章异常", e);
        } finally {
            if (pdfReader != null) {
                pdfReader.close();
            }
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}


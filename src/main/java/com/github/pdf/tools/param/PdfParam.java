package com.github.pdf.tools.param;

import com.github.pdf.tools.template.TemplateEnum;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * @author wangdongbo
 * @since 2019/7/9.
 */
@Data
@Builder
public class PdfParam {

    private static final String FILE_TYPE = ".pdf";

    /**
     * 模板[非必填,默认VELOCITY]
     */
    private TemplateEnum templateEnum;

    /**
     * 模板路径[文件夹]
     */
    private String templatePath;

    /**
     * 模板名[加后缀]
     */
    private String templateName;

    /**
     * 模板参数[非必填]
     */
    private Object data;

    /**
     * 生成文件名[非必填]
     */
    private String fileName;

    /**
     * 页面大小, 非必填，默认A4
     */
    private Page page;

    /**
     * CSS输入流[非必填，只支持5，7会自己解析地址]
     */
    private InputStream cssInputStream;

    /**
     * 字体地址[文件夹], 非必填
     */
    private String fontPath;

    /**
     * 签章信息
     */
    private SignParam signParam;

    public boolean check() {
        return StringUtils.isNotEmpty(templatePath) && StringUtils.isNotEmpty(templateName);
    }

    public void fill() {
        if (Objects.isNull(templateEnum)) {
            this.templateEnum = TemplateEnum.VELOCITY;
        }
        if (StringUtils.isEmpty(fileName)) {
            this.fileName = UUID.randomUUID().toString().replace("-", "").toLowerCase() + FILE_TYPE;
        } else if (!fileName.endsWith(FILE_TYPE)) {
            fileName = fileName + FILE_TYPE;
        }
    }
}

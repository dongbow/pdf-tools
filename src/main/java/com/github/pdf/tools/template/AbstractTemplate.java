package com.github.pdf.tools.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdongbo
 * @since 2019/7/3.
 */
public abstract class AbstractTemplate {

    private static final Map<String, AbstractTemplate> TEMPLATE_MAP = new ConcurrentHashMap<>(16);

    static {
        TEMPLATE_MAP.put(TemplateEnum.VELOCITY.name(), new VelocityTemplate());
        TEMPLATE_MAP.put(TemplateEnum.FREEMARKER.name(), new FreeMarkerTemplate());
    }

    public static String getContent(TemplateEnum templateEnum, String templatePath, String templateName, Object data) {
        return TEMPLATE_MAP.get(templateEnum.name()).getContent(templatePath, templateName, data);
    }

    /**
     * 渲染
     *
     * @param templatePath
     * @param templateName
     * @param data
     * @return
     */
    protected abstract String getContent(String templatePath, String templateName, Object data);

}

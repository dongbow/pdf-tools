package com.github.pdf.tools.template;

import com.github.pdf.tools.exception.FreeMarkerException;
import com.google.common.base.Charsets;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdongbo
 * @since 2019/7/3.
 */
public class FreeMarkerTemplate extends AbstractTemplate {

    private static Map<String, FileTemplateLoader> FILE_TEMPLATE_LOADER_CACHE = new ConcurrentHashMap<>(16);

    private static Map<String, Configuration> CONFIGURATION_CACHE = new ConcurrentHashMap<>(16);

    private static Configuration getConfiguration(String templatePath) {
        if (CONFIGURATION_CACHE.get(templatePath) != null) {
            return CONFIGURATION_CACHE.get(templatePath);
        }
        Configuration config = new Configuration(Configuration.VERSION_2_3_25);
        config.setDefaultEncoding(Charsets.UTF_8.toString());
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        FileTemplateLoader fileTemplateLoader;
        try {
            if (FILE_TEMPLATE_LOADER_CACHE.containsKey(templatePath)) {
                fileTemplateLoader = FILE_TEMPLATE_LOADER_CACHE.get(templatePath);
            } else {
                fileTemplateLoader = new FileTemplateLoader(new File(templatePath));
            }
        } catch (IOException e) {
            throw new FreeMarkerException("fileTemplateLoader init error!", e);
        }
        config.setTemplateLoader(fileTemplateLoader);
        CONFIGURATION_CACHE.put(templatePath, config);
        return config;
    }

    @Override
    protected String getContent(String templatePath, String templateName, Object data) {
        try {
            Template template = getConfiguration(templatePath).getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            writer.flush();
            return writer.toString();
        } catch (Exception ex) {
            throw new FreeMarkerException("FreeMarkerUtil process fail", ex);
        }
    }

}

package com.github.pdf.tools.template;

import com.github.pdf.tools.exception.VelocityException;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanMap;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdongbo
 * @since 2019/7/3.
 */
public class VelocityTemplate extends AbstractTemplate {

    private static final Map<String, VelocityEngine> ENGINE_MAP = new ConcurrentHashMap<>(16);

    private static VelocityEngine getEngine(String templatePath) {
        if (ENGINE_MAP.containsKey(templatePath)) {
            return ENGINE_MAP.get(templatePath);
        }
        Properties properties = new Properties();
        properties.setProperty(Velocity.OUTPUT_ENCODING, Charsets.UTF_8.toString());
        properties.setProperty(Velocity.INPUT_ENCODING, Charsets.UTF_8.toString());
        properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templatePath);
        VelocityEngine velocityEngine = new VelocityEngine(properties);
        ENGINE_MAP.put(templatePath, velocityEngine);
        return velocityEngine;
    }

    @Override
    protected String getContent(String templatePath, String templateName, Object data) {
        try {
            VelocityContext velocityContext = new VelocityContext(Objects.nonNull(data) ? Maps.newHashMap(BeanMap.create(data)) : Collections.emptyMap());
            Template template = getEngine(templatePath).getTemplate(templateName, Charsets.UTF_8.toString());
            StringWriter writer = new StringWriter();
            template.merge(velocityContext, writer);
            writer.flush();
            return writer.toString();
        } catch (Exception e) {
            throw new VelocityException("Velocity exception", e);
        }
    }
}

package com.azilen.service.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NotificationTemplateProcessor {
    public static NotificationTemplateProcessor notificationTemplateProcessor = null;
    public static Configuration cfg;
    public static Map<String, Template> templateCache = new HashMap<>();
    public static StringTemplateLoader stringLoader = new StringTemplateLoader();

    private NotificationTemplateProcessor() {

    }

    public synchronized static NotificationTemplateProcessor getInstance() {
        if (notificationTemplateProcessor == null) {
            notificationTemplateProcessor = new NotificationTemplateProcessor();
            cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateLoader(stringLoader);
        }
        log.info("template instance created");
        return notificationTemplateProcessor;
    }

    public String processTemplate(String templateData,int version, String subType, Map<String, Object> values,String eventType)
        throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException,
        TemplateException {
        log.info("template processing obj");
        Template template = getTemplate(templateData,version, subType, eventType);
        try (StringWriter out = new StringWriter()) {
            template.process(values, out);
            String proccessedTemplate = out.getBuffer().toString();
            //log.info("processed template with data obj::{}", proccessedTemplate);
            return proccessedTemplate;
        }
    }

    private synchronized Template getTemplate(String templateData,int version,String subType,String eventType)
        throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException { log.info("puting template in cache obj::{}", templateData);

        cfg.removeTemplateFromCache(subType+ "_" + eventType +"_"+version );
        String data = StringEscapeUtils.unescapeHtml4(templateData);
        stringLoader.putTemplate(subType+ "_" + eventType +"_"+version, data);
        cfg.setTemplateLoader(stringLoader);
        Template template = cfg.getTemplate(subType+ "_" + eventType +"_"+version);

        log.info("found template from cache obj");
        return template;
    }
}

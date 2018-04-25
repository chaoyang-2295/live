package com.huashengke.com.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Properties getProperties(String propertiesName){
        Properties properties = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesName);
        try {
            properties.load(in);
            in.close();
        } catch (IOException e) {
            logger.error("no such properties "+propertiesName);
        }
        return properties;
    }
}

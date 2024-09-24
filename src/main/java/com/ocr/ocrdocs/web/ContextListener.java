package com.ocr.ocrdocs.web;

import com.ocr.ocrdocs.data.DataProvider;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.IOException;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContextListener.super.contextInitialized(sce);

        Properties properties = new Properties();


        try {
            properties.load( sce.getServletContext().getResourceAsStream("/WEB-INF/dbkey.properties") );
            final String url = properties.getProperty("DBNAME");
            DataProvider.init(url);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}


package com.gym_project;

import com.gym_project.config.ApplicationConfig;
import com.gym_project.config.SwaggerConfig;
import com.gym_project.config.WebMvcConfig;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class AppRunner {

    public static void main(String[] args) throws Exception {

        int port = 8080;

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        Context context = tomcat.addContext("", new File(".").getAbsolutePath());

        AnnotationConfigWebApplicationContext appContext =
                new AnnotationConfigWebApplicationContext();

        appContext.register(ApplicationConfig.class, WebMvcConfig.class, SwaggerConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);

        Tomcat.addServlet(context, "dispatcher", dispatcherServlet)
                .setLoadOnStartup(1);

        context.addServletMappingDecoded("/", "dispatcher");

        System.out.println("Starting server at http://localhost:" + port);

        tomcat.start();
        tomcat.getServer().await();
    }
}
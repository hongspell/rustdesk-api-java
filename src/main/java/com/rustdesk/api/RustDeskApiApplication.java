package com.rustdesk.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * RustDesk API Application
 *
 * @author RustDesk API Team
 * @version 2.0.0
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@ConfigurationPropertiesScan
public class RustDeskApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RustDeskApiApplication.class, args);
        System.out.println("""

                ====================================
                RustDesk API Server Started Successfully
                ====================================
                Please check the console for admin password
                Access Admin Panel: http://localhost:21114/_admin/
                Access API Docs: http://localhost:21114/swagger-ui.html
                ====================================
                """);
    }
}

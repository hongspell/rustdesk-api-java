package com.rustdesk.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA Configuration
 * Enables JPA auditing for automatic timestamp management
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.rustdesk.api.repository")
@EnableTransactionManagement
public class JpaConfig {
    // JPA auditing is now enabled
    // @CreatedDate and @LastModifiedDate annotations in BaseEntity will work automatically
}

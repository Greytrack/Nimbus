package com.cjc.nimbus.upgrade.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author CJC
 * @date 2025/6/22
 */
@Slf4j
@Configuration
@Getter
public class FlywayConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Value("${spring.flyway.encoding:UTF-8}")
    private String encoding;

    @Value("${spring.flyway.locations}")
    private String[] locations;

    @Value("${spring.flyway.sql-migration-prefix:V}")
    private String sqlMigrationPrefix;

    @Value("${spring.flyway.sql-migration-separator:__}")
    private String sqlMigrationSeparator;

    @Value("${spring.flyway.sql-migration-suffixes:.sql}")
    private String sqlMigrationSuffixes;

    @Value("${spring.flyway.validate-on-migrate:true}")
    private boolean validateOnMigrate;

    @Value("${spring.flyway.baseline-on-migrate:true}")
    private boolean baselineOnMigrate;

    @Value("${spring.flyway.placeholder-replacement:false}")
    private boolean placeholderReplacement;

    @Value("${spring.flyway.clean-disabled:true}")
    private boolean cleanDisabled;

    @Value("${spring.flyway.baseline-version}")
    private String baselineVersion;

    @Bean
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(datasourceUrl, datasourceUsername, datasourcePassword)
                .encoding(encoding)
                .locations(locations)
                .sqlMigrationPrefix(sqlMigrationPrefix)
                .sqlMigrationSeparator(sqlMigrationSeparator)
                .sqlMigrationSuffixes(sqlMigrationSuffixes)
                .validateOnMigrate(validateOnMigrate)
                .baselineOnMigrate(baselineOnMigrate)
                .placeholderReplacement(placeholderReplacement)
                .cleanDisabled(cleanDisabled)
                .baselineVersion(baselineVersion)
                .load();
    }
}

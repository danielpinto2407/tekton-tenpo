package com.tekton.tenpo.infrastructure.adapters.in.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.percentage")
public class PercentageApiProperties {
    private String baseUrl;
    private String path;
}

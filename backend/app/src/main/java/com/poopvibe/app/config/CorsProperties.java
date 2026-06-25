package com.poopvibe.app.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configures browser origins allowed to call the REST API during PWA development.
 */
@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {
    private List<String> allowedOrigins = new ArrayList<>();

    /**
     * Returns the browser origins allowed to call API routes.
     *
     * @return allowed origin URLs
     */
    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    /**
     * Replaces the allowed browser origin list from configuration binding.
     *
     * @param allowedOrigins allowed origin URLs
     */
    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}

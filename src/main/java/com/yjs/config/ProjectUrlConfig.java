package com.yjs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by yjs
 */
@Data
@Component
@ConfigurationProperties(prefix = "projecturl")
public class ProjectUrlConfig {

    public String sell;

}

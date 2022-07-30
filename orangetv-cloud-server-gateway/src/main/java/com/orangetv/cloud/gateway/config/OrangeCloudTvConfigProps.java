package com.orangetv.cloud.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("orangetv")
public class OrangeCloudTvConfigProps {

    private boolean enableLocationRewrite;
    private String locationRewritePrefix = "/api";
}

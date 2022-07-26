package com.orangetv.cloud.album.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("orangetv")
public class OrangeTVConfigProps {
    private String rootPath;
    private String appId;
}

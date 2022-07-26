package com.orangetv.cloud.videostore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("orangetv")
public class OrangeTVConfigProps {
    private String videoScanSuffixes;
    private String videoScanPath;
}

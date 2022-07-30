package com.orangetv.cloud.videostore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Orange Cloud TV video service config
 */
@Data
@ConfigurationProperties("orangetv")
public class OrangeTVConfigProps {
    private String videoScanSuffixes;
    private String videoScanPath;
    private String videoRepoUrl;
    private int videoRepoId;
}

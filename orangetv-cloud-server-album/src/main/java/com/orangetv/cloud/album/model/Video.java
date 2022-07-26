package com.orangetv.cloud.album.model;

import lombok.Data;

@Data
public class Video {
    private Integer id;
    private String name;
    private String path;
    private String cover;
    private Long length;
}

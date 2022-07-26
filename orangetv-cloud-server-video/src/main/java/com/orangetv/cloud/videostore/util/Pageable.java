package com.orangetv.cloud.videostore.util;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Pageable<T> {
    private int total;
    private List<T> data;
    private int current;
}

package com.orangetv.cloud.videostore.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {

    private T body;
    private Integer code;
    private String msg;

    public static <U> Response<U> ok(U body) {
        return new Response<>(body, 0, "ok");
    }

    public static <U> Response<U> fail(String msg) {
        msg = msg == null ? "error" : msg;
        return new Response<>(null, -1, msg);
    }
}

package org.wingstudio.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Response {
    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    private boolean success;
    private String message;
    private Object body;
}

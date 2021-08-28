package org.clever.hot.reload.spring.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/28 11:38 <br/>
 */
@Data
public class ErrorResponse implements Serializable {
    /**
     * 时间戳
     */
    private Date timestamp;
    /**
     * 异常消息(exception.message)
     */
    private String error;
    /**
     * 响应状态码(HTTP 状态码)
     */
    private int status;
    /**
     * 异常类型，异常的具体类型
     */
    private String exception;
    /**
     * 错误消息，用于前端显示
     */
    private String message;
    /**
     * 请求路径，当前请求的路径
     */
    private String path;
}

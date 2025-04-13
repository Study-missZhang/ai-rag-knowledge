package com.zky.dev.tech.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: ZhangKaiYuan
 * @description: 响应参数
 * @create: 2025/4/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> implements Serializable {

    private String code;
    private String info;
    private T data;
}

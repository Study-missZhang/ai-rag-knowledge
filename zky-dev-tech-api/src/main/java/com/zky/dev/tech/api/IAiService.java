package com.zky.dev.tech.api;

import org.springframework.ai.chat.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * @author: ZhangKaiYuan
 * @description: API接口
 * @create: 2025/4/10
 */
public interface IAiService {

    //聊天回复方法
    ChatResponse generate(String model, String message);

    //聊天回复流
    Flux<ChatResponse> generateStream(String model, String message);
}

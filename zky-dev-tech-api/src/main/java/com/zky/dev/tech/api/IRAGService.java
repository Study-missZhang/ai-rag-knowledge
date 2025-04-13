package com.zky.dev.tech.api;

import com.zky.dev.tech.api.response.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: ZhangKaiYuan
 * @description: RAG知识库接口
 * @create: 2025/4/12
 */
public interface IRAGService {

    Response<List<String>> queryRagTagList();

    Response<String> uploadFile(String ragTag, List<MultipartFile> files);
}

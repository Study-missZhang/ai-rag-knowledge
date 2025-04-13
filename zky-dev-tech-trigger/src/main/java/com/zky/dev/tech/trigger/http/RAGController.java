package com.zky.dev.tech.trigger.http;

import com.zky.dev.tech.api.IRAGService;
import com.zky.dev.tech.api.response.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ZhangKaiYuan
 * @description: RAG知识库
 * @create: 2025/4/12
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/rag/")
public class RAGController implements IRAGService {

    @Resource
    private OllamaChatClient ollamaChatClient;
    @Resource
    private TokenTextSplitter tokenTextSplitter;
    @Resource
    private SimpleVectorStore simpleVectorStore;
    @Resource
    private PgVectorStore pgVectorStore;
    @Resource
    private RedissonClient redissonClient;

    @RequestMapping(value = "query_rag_tag_list", method = RequestMethod.GET)
    @Override
    public Response<List<String>> queryRagTagList() {
        RList<String> elements = redissonClient.getList("ragTag");
        return Response.<List<String>>builder()
                .code("00000")
                .info("调用成功")
                .data(new ArrayList<>(elements))
                .build();
    }
    @RequestMapping(value = "file/upload", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    @Override
    public Response<String> uploadFile(@RequestParam String ragTag,@RequestParam("file") List<MultipartFile> files) {
        log.info("知识库上传开始 {}", ragTag);
        for (MultipartFile file : files){
            //读取知识库，进行分段
            TikaDocumentReader documentReader = new TikaDocumentReader(file.getResource());
            List<Document> documents = documentReader.get();
            List<Document> documentSplitterList = tokenTextSplitter.apply(documents);

            //为文档添加元数据标记
            documents.forEach(doc -> doc.getMetadata().put("knowledge", ragTag));
            documentSplitterList.forEach(doc -> doc.getMetadata().put("knowledge", ragTag));

            //存入数据库中
            pgVectorStore.accept(documentSplitterList);

            //存入redis中
            RList<String> elements = redissonClient.getList("ragTag");
            if(!elements.contains(ragTag)){
                elements.add(ragTag);
            }
        }
        log.info("知识库上传完成 {}", ragTag);
        return Response.<String>builder()
                .code("0000")
                .info("调用成功")
                .build();
    }
}

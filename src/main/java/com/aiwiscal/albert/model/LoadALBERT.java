package com.aiwiscal.albert.model;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tensorflow.Graph;
import org.tensorflow.Session;

import javax.annotation.PostConstruct;
import java.io.InputStream;

/**
 * @author wenhan
 * @create 2020-03-21-11:40
 * 自动加载albert tensorflow模型
 */
@Getter
@Component("loadALBERT")
public class LoadALBERT {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Session session;

    private final int vectorDim = 312;

    private final int maxSupportLen = 510;

    private final String modelPath = "albert-model/albert_tiny_zh_google.pb";



    @PostConstruct
    private void init(){
        loadGraph();  // 调用加载方法
    }

    private void loadGraph(){
        Graph graph = new Graph();
        try{
            // 获取资源文件中的模型文件inputStream
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(modelPath);
            byte[] graphPb = IOUtils.toByteArray(inputStream);
            graph.importGraphDef(graphPb);
            this.session = new Session(graph);
            logger.info("ALBERT checkpoint loaded @ {}, vector dimension - {}, maxSupportLen - {}",
                    modelPath, vectorDim, maxSupportLen);
        } catch (Exception e){
            logger.error("Failed to load ALBERT checkpoint @ {} ! - {}", modelPath, e.toString());
        }
    }
}

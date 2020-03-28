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
@Getter // lombok生成getter方法，方便取数据
@Component("loadALBERT")
public class LoadALBERT {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Session session;  // TensorFlow Session 对象，可完成推理

    private final int vectorDim = 312;  // albert 向量维数

    private final int maxSupportLen = 510; // albert 支持的最大长度，原始为512，去掉首尾的[CLS]和[SEP]，即为510

    private final String modelPath = "albert-model/albert_tiny_zh_google.pb"; // 模型资源文件路径



    @PostConstruct
    private void init(){
        loadGraph();  // 调用加载方法
    }

    private void loadGraph(){
        Graph graph = new Graph();
        try{
            // 获取资源文件中的模型文件输入流
            InputStream inputStream = this.getClass().getClassLoader()
                    .getResourceAsStream(modelPath);
            // 使用commons-io中的IOUtils将模型文件输入流转化为byte数组
            byte[] graphPb = IOUtils.toByteArray(inputStream);
            //初始化TensorFlow graph
            graph.importGraphDef(graphPb);
            // 把graph装入一个新的Session，可运行推理
            this.session = new Session(graph);
            logger.info("ALBERT checkpoint loaded @ {}, vector dimension - {}, maxSupportLen - {}",
                    modelPath, vectorDim, maxSupportLen);
        } catch (Exception e){
            logger.error("Failed to load ALBERT checkpoint @ {} ! - {}", modelPath, e.toString());
        }
    }
}

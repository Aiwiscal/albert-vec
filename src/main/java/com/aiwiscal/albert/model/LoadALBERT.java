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
 */
@Getter
@Component("loadALBERT")
public class LoadALBERT {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Session session;

    private final int vectorDim = 312;

    private final int maxSupportLen = 510;

    @PostConstruct
    private void init(){
        loadGraph();
    }

    private void loadGraph(){
        Graph graph = new Graph();
        try{
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("albert-model/albert_tiny_zh_google.pb");
            byte[] graphPb = IOUtils.toByteArray(inputStream);
            graph.importGraphDef(graphPb);
            this.session = new Session(graph);
            logger.info("ALBERT checkpoint loaded.");
        } catch (Exception e){
            logger.error("Failed to load ALBERT checkpoint! - {}", e.toString());
        }
    }
}

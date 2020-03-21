package com.aiwiscal.albert.controller;

import com.aiwiscal.albert.params.InputText;
import com.aiwiscal.albert.params.OutputVector;
import com.aiwiscal.albert.service.InferALBERT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenhan
 * @create 2020-03-21-19:15
 * 处理请求controller
 */
@RestController
public class AlbertVecController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private InferALBERT inferALBERT;

    @Value("${server.port}")
    private int port;

    @RequestMapping("/")
    public String runStatus(){
        return String.format("======== ALBERT Vector Service is running @ port %d =======", this.port);
    }

    @PostMapping(path="/vector")
    public OutputVector getVector(@RequestBody InputText inputText){
        return inferALBERT.infer(inputText);
    }
}

package com.aiwiscal.albert.starter;

import com.aiwiscal.albert.params.InputText;
import com.aiwiscal.albert.params.OutputToken;
import com.aiwiscal.albert.params.OutputVector;
import com.aiwiscal.albert.service.InferALBERT;
import com.aiwiscal.albert.model.LoadVocab;
import com.aiwiscal.albert.service.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author wenhan
 * @create 2020-03-21-14:24
 * 启动简易自检
 */
@Component("servicePass")
public class ServicePass implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private InferALBERT inferALBERT;

    @Autowired
    private LoadVocab loadVocab;

    @Autowired
    private Tokenizer tokenizer;


    @Override
    public void run(String... args) throws Exception {
        inferArrPass();
    }

    private void inferArrPass(){
        try{
            InputText inputText = new InputText("你好 世 界， 世界你好!", 5);
            OutputToken outputToken = tokenizer.tokenize(inputText);

            logger.debug("tokenize() passed ...");

            OutputVector outputVector = inferALBERT.infer(inputText);
            logger.debug("infer() passed ...");

            logger.info("ALBERT Vector Service is ready to listen ...");

        }catch (Exception e){
            logger.error("failed to run ServicePass - {} ", e.toString());
        }


    }

}

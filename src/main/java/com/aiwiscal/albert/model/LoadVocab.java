package com.aiwiscal.albert.model;


import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wenhan
 * @create 2020-03-21-15:10
 */
@Getter
@Component("loadVocab")
public class LoadVocab {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, Integer> vocabTable = new HashMap<>();

    @PostConstruct
    private void init(){
        loadVocab();
    }

    private void loadVocab(){
        try{
            InputStreamReader inputReader = new InputStreamReader(
                    this.getClass().getClassLoader().getResourceAsStream("albert-model/vocab.txt"));
            BufferedReader bf = new BufferedReader(inputReader);
            String str;
            int n = 0;
            while ((str = bf.readLine()) != null) {
                String lineWord = str.trim();
                this.vocabTable.put(lineWord, n);
                n++;
            }
            bf.close();
            inputReader.close();
            logger.info("albert vocab loaded. total number: {} ", n);
        }catch (Exception e){
            logger.error("failed to load vocab! - {}", e.toString());
        }
    }
}

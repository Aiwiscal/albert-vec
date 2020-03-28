package com.aiwiscal.albert.service;

import com.aiwiscal.albert.model.LoadALBERT;
import com.aiwiscal.albert.model.LoadVocab;
import com.aiwiscal.albert.params.InputText;
import com.aiwiscal.albert.params.InputTextValid;
import com.aiwiscal.albert.params.OutputToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wenhan
 * @create 2020-03-21-15:09
 * 简易分词器，获得输入文本的tokenId和segmentId
 * 注意：当前仅支持中文！
 */
@Component("tokenizer")
public class Tokenizer {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LoadVocab loadVocab; // 注入词汇表映射

    @Autowired
    private LoadALBERT loadALBERT;  // 注入albert模型数据

    // 原始输入清洗，切分字符并查表得到tokenId和segmentId
    // 返回OutputToken类对象
    public OutputToken tokenize(InputText inputText){
        OutputToken outputToken = new OutputToken();
        try{
            // 简单清洗文本，生成有效输入类InputTextValid对象
            InputTextValid inputTextValid = validateText(inputText);

            // 获得tokenId
            float[] tokenId = getTokenId(inputTextValid);

            // 获得segmentId
            float[] segmentId = getSegmentId(inputTextValid);

            outputToken.setTokenId(tokenId);
            outputToken.setSegmentId(segmentId);
            outputToken.setSuccess(true);
            outputToken.setInputTextValid(inputTextValid);
            logger.debug("Text tokenized ...");
        }catch (Exception e){
            logger.error("Failed to tokenize the text - {}", e.toString());
            outputToken.setSuccess(false);
        }
        return outputToken;
    }

    // 查表获得tokenId
    private float[] getTokenId(InputTextValid inputTextValid){
        String[] textTokenList = inputTextValid.getTextTokenList();
        Map<String, Integer> vocabTable = loadVocab.getVocabTable(); // 获得注入loadVacab对象中的字符映射表
        float[] tokenId = new float[textTokenList.length + 2];
        tokenId[0] = 101; // 头部添加[CLS]标记，token id为101
        tokenId[tokenId.length - 1] = 102; // 尾部添加[SEP]标记，token id为102
        for (int i = 0; i < textTokenList.length; i++) {
            String currentCharStr = textTokenList[i];
            if(!vocabTable.containsKey(currentCharStr)){
                currentCharStr = "[UNK]"; // 不在词汇表中的，设定为[UNK]
            }
            tokenId[i+1] = vocabTable.get(currentCharStr); // 查表
        }
        return tokenId;
    }

    // 单文本输入，直接生成 全0 segmentId
    private float[] getSegmentId(InputTextValid inputTextValid){
        return new float[inputTextValid.getTextTokenList().length + 2];
    }

    // 清洗原始文本，去除空格，补充[PAD]，切分字符
    private InputTextValid validateText(InputText inputText){
        String text = inputText.getText();
        text = text.replaceAll("\\pZ", ""); // 去掉空字符
        int validLength = inputText.getValidLength();
        // 获得文本真实的，有效的长度
        if(validLength < 0){
            validLength = text.length();
        }else {
            validLength = Math.min(loadALBERT.getMaxSupportLen(), validLength);
        }

        String[] textTokenList = new String[validLength];
        StringBuilder newText = new StringBuilder();
        // 字符切分并添加[PAD]
        for (int i = 0; i < validLength; i++) {
            if(i < text.length()){
                textTokenList[i] = String.valueOf(text.charAt(i));
                newText.append(text.charAt(i));
            }else {
                textTokenList[i] = "[PAD]";
                newText.append("#");
            }
        }
        InputTextValid inputTextValid = new InputTextValid();
        inputTextValid.setTextTokenList(textTokenList);
        inputTextValid.setValidLength(validLength);
        inputTextValid.setText(newText.toString());
        return inputTextValid;
    }

}

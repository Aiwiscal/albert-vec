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
    private LoadVocab loadVocab;

    @Autowired
    private LoadALBERT loadALBERT;

    public OutputToken tokenize(InputText inputText){
        OutputToken outputToken = new OutputToken();
        try{
            InputTextValid inputTextValid = validateText(inputText);
            float[] tokenId = getTokenId(inputTextValid);
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

    private float[] getTokenId(InputTextValid inputTextValid){
        String[] textTokenList = inputTextValid.getTextTokenList();
        Map<String, Integer> vocabTable = loadVocab.getVocabTable();
        float[] tokenId = new float[textTokenList.length + 2];
        tokenId[0] = 101;
        tokenId[tokenId.length - 1] = 102;
        for (int i = 0; i < textTokenList.length; i++) {
            String currentCharStr = textTokenList[i];
            if(!vocabTable.containsKey(currentCharStr)){
                currentCharStr = "[UNK]";
            }
            tokenId[i+1] = vocabTable.get(currentCharStr);
        }
        return tokenId;
    }

    private float[] getSegmentId(InputTextValid inputTextValid){
        return new float[inputTextValid.getTextTokenList().length + 2];
    }

    private InputTextValid validateText(InputText inputText){
        String text = inputText.getText();
        text = text.replaceAll("\\pZ", "");
        int validLength = inputText.getValidLength();
        if(validLength == -1){
            validLength = text.length();
        }else {
            validLength = Math.min(loadALBERT.getMaxSupportLen(), validLength);
        }

        String[] textTokenList = new String[validLength];
        StringBuilder newText = new StringBuilder();
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

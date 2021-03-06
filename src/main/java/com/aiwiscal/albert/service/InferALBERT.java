package com.aiwiscal.albert.service;

import com.aiwiscal.albert.model.LoadALBERT;
import com.aiwiscal.albert.params.InputText;
import com.aiwiscal.albert.params.OutputToken;
import com.aiwiscal.albert.params.OutputVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tensorflow.Tensor;

/**
 * @author wenhan
 * @create 2020-03-21-14:03
 * 核心ALBERT推理类
 */
@Service
public class InferALBERT {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LoadALBERT loadALBERT;  // 注入模型数据

    @Autowired
    private Tokenizer tokenizer;    // 注入分词器

    private float[] inferArr(float[] inputToken, float[] inputSegment){
        // 将1维数组扩展为2维以满足输入需要
        float[][] inputToken2D = new float[1][inputToken.length];
        float[][] inputSegment2D = new float[1][inputSegment.length];
        System.arraycopy(inputToken, 0, inputToken2D[0], 0, inputToken.length);
        System.arraycopy(inputSegment, 0, inputSegment2D[0], 0, inputSegment.length);

        // 调用TensorFlow会话(Session)中的runner，实现模型推理
        // 注入数据使用feed，取结果使用fetch，根据输入输出tensor的名称操作
        Tensor result = loadALBERT.getSession().runner()
                .feed("Input-Token", Tensor.create(inputToken2D))
                .feed("Input-Segment", Tensor.create(inputSegment2D))
                .fetch("output_1")
                .run().get(0);
        float[] ret = new float[loadALBERT.getVectorDim()];
        // 将结果的Tensor对象内部数据拷贝至原生数组
        result.copyTo(ret);
        return ret;
    }

    public OutputVector infer(InputText inputText){
        OutputVector outputVector = new OutputVector();
        outputVector.setSuccess(false);
        // null 检查
        if(inputText == null){
            logger.warn("get NULL inputText, return default outputVector object.");
            return outputVector;
        }
        if(inputText.getText() == null || inputText.getText().length() == 0){
            logger.warn("get NULL or empty text in inputText, return default outputVector object.");
            return outputVector;
        }
        if(inputText.getValidLength() == 0){
            logger.warn("the input validLength equals to 0, return default outputVector object.");
        }
        outputVector.setRawText(inputText.getText());
        outputVector.setRawValidLength(inputText.getValidLength());
        long tic = System.currentTimeMillis();
        // 分词并推理，打印相关日志
        try{
            OutputToken outputToken = tokenizer.tokenize(inputText);
            logger.info("Raw Input: Text - \"{}\", ValidLength - {} ", inputText.getText(), inputText.getValidLength());
            outputVector.setText(outputToken.getInputTextValid().getText());
            outputVector.setValidLength(outputToken.getInputTextValid().getValidLength());
            logger.info("Validated Input: Text - \"{}\", ValidLength - {} ", outputVector.getText(), outputVector.getValidLength());
            float[] result = inferArr(outputToken.getTokenId(), outputToken.getSegmentId());
            outputVector.setVector(result);
            if(outputToken.isSuccess()){
                outputVector.setSuccess(true);
                logger.debug("ALBERT inference finished ... ");
            }else {
                logger.warn("ALBERT inference finished BUT errors occurred in tokenizer, the vector may be invalid ...");
            }
        }catch (Exception e){
            logger.error("Failed to infer ALBERT - {} ", e.toString());
        }
        long toc = System.currentTimeMillis();
        logger.info("ALBERT vector generation finished - time cost: {} ms. ", toc-tic);
        // 返回OutputVector对象
        return outputVector;
    }
}

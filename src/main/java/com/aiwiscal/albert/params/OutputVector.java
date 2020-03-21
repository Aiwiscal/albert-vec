package com.aiwiscal.albert.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenhan
 * @create 2020-03-21-15:34
 * 最终请求返回类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputVector {
    // 原始输入文本
    private String rawText;

    // 有效文本
    private String text;

    // 输入有效长度
    private int rawValidLength;

    // 实际有效长度
    private int validLength;

    // albert向量
    private float[] vector;

    // 是否成功
    private boolean success;

}

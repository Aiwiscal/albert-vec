package com.aiwiscal.albert.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenhan
 * @create 2020-03-21-15:42
 * 模型推理输入类，包含tokenId和segmentId
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputToken {
    // 有效输入类对象
    InputTextValid inputTextValid;

    // 有效文本对应tokenId
    private float[] tokenId;

    // 有效文本对应segmentId
    private float[] segmentId;

    // 是否成功
    private boolean success;
}

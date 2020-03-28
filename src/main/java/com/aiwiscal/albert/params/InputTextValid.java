package com.aiwiscal.albert.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenhan
 * @create 2020-03-21-18:39
 * 有效输入类，对原始输入去空字符或补充[PAD]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputTextValid {
    // 将有效文本切分为单个字符列表
    private String[] TextTokenList;

    // 去掉空字符或补充[PAD]后的有效文本
    private String text;

    // 有效文本的真实长度
    private int validLength;
}

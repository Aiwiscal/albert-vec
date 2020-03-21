package com.aiwiscal.albert.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenhan
 * @create 2020-03-21-15:32
 * 原始输入类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputText {
    // text:原始文本
    private String text = "";

    // validLength: 有效长度
    private int validLength = -1;
}

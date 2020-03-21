package com.aiwiscal.albert.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenhan
 * @create 2020-03-21-18:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputTextValid {
    private String[] TextTokenList;
    private String text;
    private int validLength;
}

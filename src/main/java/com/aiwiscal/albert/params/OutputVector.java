package com.aiwiscal.albert.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenhan
 * @create 2020-03-21-15:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputVector {
    private String rawText;

    private String text;

    private int rawValidLength;

    private int validLength;

    private float[] vector;

    private boolean success;

}

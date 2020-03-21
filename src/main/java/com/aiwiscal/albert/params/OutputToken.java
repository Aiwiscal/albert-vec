package com.aiwiscal.albert.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenhan
 * @create 2020-03-21-15:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputToken {
    InputTextValid inputTextValid;
    private float[] tokenId;
    private float[] segmentId;
    private boolean success;
}

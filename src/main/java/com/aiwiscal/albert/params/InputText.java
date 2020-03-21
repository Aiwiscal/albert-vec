package com.aiwiscal.albert.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenhan
 * @create 2020-03-21-15:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputText {
    private String text = "";
    private int validLength = -1;
}

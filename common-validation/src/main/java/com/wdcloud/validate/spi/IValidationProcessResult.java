package com.wdcloud.validate.spi;

import com.wdcloud.validate.dto.ValidateResult;

import java.util.List;

public interface IValidationProcessResult {
    /**
     * process validate result
     *
     * @param vrList
     */
    public void doProcessResult(List<ValidateResult> vrList);
}

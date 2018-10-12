package com.wdcloud.validate.spi;

import com.wdcloud.validate.dto.ValidateResult;
import com.wdcloud.validate.exceptions.ValidateException;

import java.util.List;
public class ValidationDefaultProcessResult implements IValidationProcessResult {

    @Override
    public void doProcessResult(List<ValidateResult> vrList) {
        if (vrList.size() > 0) {
            throw new ValidateException(getValidateResult(vrList));
        }
    }

    private String getValidateResult(List<ValidateResult> vrList) {
        StringBuffer result = new StringBuffer();
        for (ValidateResult vr : vrList) {
            result.append(vr.getMsgKey()).append(":").append(vr.getMsgBody()).append(";");
        }
        if (result.length() > 0) {
            return result.substring(0, result.length() - 1);
        }
        return result.toString();
    }
}

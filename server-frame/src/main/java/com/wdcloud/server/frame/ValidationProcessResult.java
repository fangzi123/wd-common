package com.wdcloud.server.frame;

import com.wdcloud.validate.dto.ValidateResult;
import com.wdcloud.validate.exceptions.ValidateException;
import com.wdcloud.validate.spi.IValidationProcessResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidationProcessResult implements IValidationProcessResult {

    @Override
    public void doProcessResult(List<ValidateResult> vrList) {
        if (vrList.size() > 0) {
            throw new ValidateException(getValidateResult(vrList));
        }
    }

    private String getValidateResult(List<ValidateResult> vrList) {
        StringBuilder result = new StringBuilder();
        for (ValidateResult vr : vrList) {
            result.append(vr.getMsgKey())
                    .append(":")
                    .append(MessageUtil.getMessage(vr.getMsgBody()))
                    .append(";");
        }
        if (result.length() > 0) {
            return result.substring(0, result.length() - 1);
        }
        return result.toString();
    }
}

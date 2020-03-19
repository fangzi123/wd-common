package com.wdcloud.server.frame;

import com.google.common.base.Throwables;
import com.wdcloud.server.frame.api.utils.response.Code;
import com.wdcloud.server.frame.api.utils.response.Response;
import com.wdcloud.base.exception.BaseException;
import com.wdcloud.base.exception.DuplicateNameException;
import com.wdcloud.validate.exceptions.ValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

/**
 * @ClassName GlobalDefaultExceptionHandler
 * @Description 全局异常捕获
 * @Author andy
 * @Date 2018/8/22
 */

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @Autowired
    protected HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception ex) {
        logger.error(Throwables.getStackTraceAsString(ex));
        if (ex instanceof ValidateException) {
            return Response.returnResponse(Code.ERROR, ex.getMessage());
        }
        if (ex instanceof BaseException) {
            if(ex instanceof DuplicateNameException){
                return Response.returnResponse(Code.DUPLICATE_NAME, ex.getMessage(), ((BaseException) ex).getI18nMsg());
            }else{
                return Response.returnResponse(Code.ERROR, ex.getMessage(), ((BaseException) ex).getI18nMsg());
            }
        }
        return Response.returnResponse(Code.ERROR,  Code.ERROR.name);
    }
}

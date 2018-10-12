package com.wdcloud.server.frame.api;

import com.wdcloud.server.frame.FrameConstants;
import com.wdcloud.server.frame.api.utils.response.Code;
import com.wdcloud.server.frame.api.utils.response.Response;
import com.wdcloud.utils.exception.BaseException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception ex) {
        ex.printStackTrace();
        logger.error(ExceptionUtils.getFullStackTrace(ex));
        if (ex instanceof BaseException) {
            return Response.returnResponse(Code.ERROR, ex.getMessage(), ((BaseException) ex).getI18nMsg());
        } else {
            return Response.returnResponse(Code.ERROR, Code.ERROR.name);
        }
    }
    protected String getUserId() {
        logger.debug("[BaseController] getUserId. useerId = {}",
                request.getSession().getAttribute(FrameConstants.LOGIN_USER_ID).toString());
        return request.getSession().getAttribute(FrameConstants.LOGIN_USER_ID) + "";
    }
}
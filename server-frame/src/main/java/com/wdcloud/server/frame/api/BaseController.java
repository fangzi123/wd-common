package com.wdcloud.server.frame.api;

import com.wdcloud.server.frame.FrameConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected HttpServletRequest request;

    protected String getUserId() {
        logger.debug("[BaseController] getUserId. useerId = {}",
                request.getSession().getAttribute(FrameConstants.LOGIN_USER_ID));
        return request.getSession().getAttribute(FrameConstants.LOGIN_USER_ID) + "";
    }
}
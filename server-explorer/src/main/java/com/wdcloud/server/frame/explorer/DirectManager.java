package com.wdcloud.server.frame.explorer;

import com.wdcloud.server.frame.exception.ParamErrorException;
import com.wdcloud.server.frame.explorer.factory.GlobalFactory;
import com.wdcloud.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class DirectManager {

    @Resource
    private GlobalFactory globalFactory;

    public void handle(HttpServletRequest request, HttpServletResponse response, String resource, String function) {

        log.debug("[DirectManager] access. resourceName={}, functionName={}", resource,function);
        if (StringUtil.isEmpty(resource) || StringUtil.isEmpty(function)) {
            log.error("[DirectManager] access. params is error. resourceName={}, functionName={}", resource, function);
            throw new ParamErrorException();
        }

        globalFactory.getDirectComponent(resource, function).handle(request, response);

        log.info("[DirectManager] access finish, resource={}, functionName={}", resource, function);
    }
}

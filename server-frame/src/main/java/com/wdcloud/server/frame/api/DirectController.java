package com.wdcloud.server.frame.api;

import com.wdcloud.server.frame.explorer.DirectManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class DirectController {
    @Autowired
    private DirectManager directManager;

    @RequestMapping(value = "{resourceName}/{functionName}/direct", method = {RequestMethod.POST, RequestMethod.GET})
    public void handle(@PathVariable("resourceName") String resource,
                       @PathVariable("functionName") String function,
                       HttpServletRequest request, HttpServletResponse response) {
        log.info("[DirectController] request, resource={}, function={}", resource, function);
        directManager.handle(request, response, resource, function);
        log.info("[DirectController] request finish, resource={}, function={}", resource, function);
    }
}

package com.wdcloud.server.frame.api;

import com.google.common.base.Joiner;
import com.wdcloud.server.frame.FrameConstants;
import com.wdcloud.server.frame.api.utils.response.Code;
import com.wdcloud.server.frame.api.utils.response.Response;
import com.wdcloud.server.frame.explorer.DataQueryManager;
import com.wdcloud.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 查询接口
 *
 * @author csf
 * @Date 2016/5/9.
 */
@RestController
@RequestMapping("/")
public class MagicQueryController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(MagicQueryController.class);

    @Resource
    private DataQueryManager dmDataQueryManager;

    /**
     * 自定义查询
     *
     * @param path1   资源名称
     * @param request request
     * @return 查询结果
     */
    @GetMapping(value = "{path1}/", produces = {"application/javascript", "application/json"})
    public String magicQuery(@PathVariable(value = "path1", required = false) String path1,
                             HttpServletRequest request) {

        final String resourceName = getResourceName(new String[]{path1});
        return exec(request, resourceName);
    }

    /**
     * 自定义查询
     *
     * @param path1   资源名称
     * @param path2   方法名
     * @param path3   方法名
     * @param path4   方法名
     * @param request request
     * @return 查询结果
     */
    @GetMapping(value = "{path1}/{path2}", produces = {"application/javascript", "application/json"})
    public String magicQuery(@PathVariable(value = "path1", required = false) String path1,
                             @PathVariable(value = "path2", required = false) String path2,
                             HttpServletRequest request) {
        final String resourceName = getResourceName(new String[]{path1, path2});
        return exec(request, resourceName);
    }

    /**
     * 自定义查询
     *
     * @param path1   资源名称
     * @param path2   方法名
     * @param path3   方法名
     * @param path4   方法名
     * @param request request
     * @return 查询结果
     */
    @GetMapping(value = "{path1}/{path2}/{path3}", produces = {"application/javascript", "application/json"})
    public String magicQuery(@PathVariable(value = "path1", required = false) String path1,
                             @PathVariable(value = "path2", required = false) String path2,
                             @PathVariable(value = "path3", required = false) String path3,
                             HttpServletRequest request) {

        final String resourceName = getResourceName(new String[]{path1, path2, path3});
        return exec(request, resourceName);
    }

    /**
     * 自定义查询
     *
     * @param path1   资源名称
     * @param path2   方法名
     * @param path3   方法名
     * @param path4   方法名
     * @param request request
     * @return 查询结果
     */
    @GetMapping(value = "{path1}/{path2}/{path3}/{path4}", produces = {"application/javascript", "application/json"})
    public String magicQuery(@PathVariable(value = "path1", required = false) String path1,
                             @PathVariable(value = "path2", required = false) String path2,
                             @PathVariable(value = "path3", required = false) String path3,
                             @PathVariable(value = "path4", required = false) String path4,
                             HttpServletRequest request) {
        final String resourceName = getResourceName(new String[]{path1, path2, path3, path4});
        return exec(request, resourceName);
    }

    /**
     * 自定义查询
     *
     * @param path1   资源名称
     * @param path2   方法名
     * @param path3   方法名
     * @param path4   方法名
     * @param request request
     * @return 查询结果
     */
    @GetMapping(value = "{path1}/{path2}/{path3}/{path4}/{path5}", produces = {"application/javascript", "application/json"})
    public String magicQuery(@PathVariable(value = "path1", required = false) String path1,
                             @PathVariable(value = "path2", required = false) String path2,
                             @PathVariable(value = "path3", required = false) String path3,
                             @PathVariable(value = "path4", required = false) String path4,
                             @PathVariable(value = "path5", required = false) String path5,
                             HttpServletRequest request) {

        final String resourceName = getResourceName(new String[]{path1, path2, path3, path4, path5});
        return exec(request, resourceName);

    }

    /**
     * 自定义查询
     *
     * @param path1   资源名称
     * @param path2   方法名
     * @param path3   方法名
     * @param path4   方法名
     * @param request request
     * @return 查询结果
     */
    @GetMapping(value = "{path1}/{path2}/{path3}/{path4}/{path5}/{path6}", produces = {"application/javascript", "application/json"})
    public String magicQuery(@PathVariable(value = "path1", required = false) String path1,
                             @PathVariable(value = "path2", required = false) String path2,
                             @PathVariable(value = "path3", required = false) String path3,
                             @PathVariable(value = "path4", required = false) String path4,
                             @PathVariable(value = "path5", required = false) String path5,
                             @PathVariable(value = "path6", required = false) String path6,
                             HttpServletRequest request) {

        final String resourceName = getResourceName(new String[]{path1, path2, path3, path4, path5, path6});
        return exec(request, resourceName);

    }

    private String exec(HttpServletRequest request, String resourceName) {
        Map<String, String> paramMap = WebUtil.requestToMap(request);
        paramMap.put(FrameConstants.LOGIN_USER_ID, getUserId());
        Object search = dmDataQueryManager.magicQuery(resourceName, paramMap);
        return Response.returnResponse(Code.OK, search, Code.OK.name);
    }

    private String getResourceName(String[] paths) {
        return Joiner.on("/").skipNulls().join(paths);
    }
}
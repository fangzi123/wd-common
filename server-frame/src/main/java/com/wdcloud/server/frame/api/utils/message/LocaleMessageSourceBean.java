package com.wdcloud.server.frame.api.utils.message;

import com.wdcloud.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
@Component("localeResolver")
public class LocaleMessageSourceBean implements LocaleResolver {
    //分隔符
    private final String REGX = "_";
    @Resource
    private MessageSource messageSource;

    /**
     * 按指定的语言key获取相应的国际化信息
     *
     * @param msgKey
     * @return String
     */
    public String getMessage(String msgKey) {
        try {
            return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            log.warn("[LocalMessageSourceBean] can't find message for code '{}' at locale {}", msgKey, LocaleContextHolder.getLocale());
            return msgKey;
        }
    }

    /**
     * 按语言参数获取对应的语言配置local,默认为本地操作系统的区域语言
     *
     * @param language
     * @return
     */
    private Locale getLocale(String language) {
        //默认请求头的语言设置
        if (!StringUtil.isEmpty(language)) {
            String[] lanCty = language.split(REGX);
            if (lanCty.length == 2) {
                return new Locale(lanCty[0], lanCty[1]);
            } else {
                return new Locale(lanCty[0]);
            }
        }
        return Locale.getDefault();
    }


    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String language = request.getHeader("language");
        return getLocale(language);
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    }
}
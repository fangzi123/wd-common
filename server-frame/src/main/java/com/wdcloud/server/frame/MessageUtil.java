package com.wdcloud.server.frame;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

@Slf4j
@Component
public class MessageUtil {
    private static MessageSource messageSource;

    @Resource
    public void setMessageSource(MessageSource messageSource) {
        MessageUtil.messageSource = messageSource;
    }

    public static String getMessage(String key) {
        return getMessage(key, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String key, Locale locale) {
        return getMessage(key, locale, (String[]) null);
    }

    public static String getMessage(String key, Locale locale, String... args) {
        try {
            return messageSource.getMessage(key, args, locale);
        } catch (NoSuchMessageException e) {
            log.warn("[LocalMessageSourceBean] can't find message for code '{}' at locale {}", key, LocaleContextHolder.getLocale());
            return key;
        }
    }
}

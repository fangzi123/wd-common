package com.wdcloud.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * dubbo 客户端获取服
 *
 * @author csf
 */
@SuppressWarnings(value = "unused")
@Component
@Configuration
@EnableConfigurationProperties(DubboProperties.class)
public class DubboReferenceContext implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(DubboReferenceContext.class);
    private final ReferenceConfigCache cache = ReferenceConfigCache.getCache();
    private List<DubboServiceInfo> serviceCacheList = Collections.synchronizedList(new ArrayList<>());
    @SuppressWarnings("unused")
    private final Object finalizerGuardian = new Object() {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            serviceCacheList.clear();
        }
    };
    private ApplicationConfig applicationConfig;
    private RegistryConfig registryConfig;
    @Autowired
    private DubboProperties dubboProperties;

    @PostConstruct
    private void init() {
        this.registryConfig = new RegistryConfig();
        registryConfig.setProtocol(dubboProperties.getProtocol());
        registryConfig.setAddress(dubboProperties.getAddress());
        registryConfig.setCheck(dubboProperties.isCheck());
        this.applicationConfig = new ApplicationConfig(dubboProperties.getName());
        if (dubboProperties.getTimeout() > 0) {
            this.registryConfig.setTimeout(dubboProperties.getTimeout());
        }
        if (isNotEmpty(dubboProperties.getUserName())) {
            this.registryConfig.setUsername(dubboProperties.getUserName());
        }
        if (isNotEmpty(dubboProperties.getPassword())) {
            this.registryConfig.setPassword(dubboProperties.getPassword());
        }
    }

    @SuppressWarnings("unused")
    public <T> T get(Class<T> clazz) {
        return get(clazz, null);
    }

    public <T> T get(Class<T> clazz, String group) {
        return get(clazz, group, null);
    }

    public <T> T get(String version, Class<T> clazz) {
        return get(clazz, null, version);
    }

    public <T> T get(Class<T> clazz, String group, String version) {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Object obj = getClass(clazz, group, version);
            if (obj == null) {
                ReferenceConfig<T> config = new ReferenceConfig<T>();
                config.setApplication(applicationConfig);
                config.setRegistry(registryConfig);
                config.setInterface(clazz);
                if (Utils.isNotEmpty(group)) {
                    config.setGroup(group);
                }
                if (Utils.isNotEmpty(version)) {
                    config.setVersion(version);
                }
                try {
                    T value = cache.get(config);
                    if (value == null) {
                        return null;
                    }
                    DubboServiceInfo<T> serviceInfo = new DubboServiceInfo<T>(group, version, clazz, value);

                    serviceCacheList.add(serviceInfo);
                    return value;
                } catch (RuntimeException e) {
                    cache.destroy(config);
                    throw e;
                }
            } else {
                return (T) obj;
            }
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    private Object getClass(Class<?> clazz, String group, String version) {
        if (serviceCacheList.isEmpty()) {
            logger.debug("get dubbo service list is empty");
            return null;
        }

        for (DubboServiceInfo serviceInfo : serviceCacheList) {
            if (serviceInfo.equals(group, version, clazz)) {
                return serviceInfo.getT();
            }
        }
        return null;
    }

    private boolean isNotEmpty(String str) {
        return str != null && !"".equals(str.trim());
    }

    @PreDestroy
    public void destroy() {
        cache.destroyAll();
        logger.info("DubboReferenceContext destroy! ");
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("DubboReferenceContext load success! address={} : protocol={}",
                dubboProperties.getAddress(), dubboProperties.getProtocol());
    }
}
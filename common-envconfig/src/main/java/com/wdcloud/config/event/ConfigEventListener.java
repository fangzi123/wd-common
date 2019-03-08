package com.wdcloud.config.event;

import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class ConfigEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private ConfigurableEnvironment env;
    private String configPath;

    private static Logger log = LoggerFactory.getLogger(ConfigEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        this.env = event.getEnvironment();
        //是否启用配置
        Boolean property = env.getProperty("inspiry.config.enabled", Boolean.class, false);
        if (!property) {
            return;
        }
        log.info("inspiry config start");
        configPath = env.getProperty("inspiry.config.git.path", "/tmp/" + UUID.randomUUID().toString());
        gitClone();
        appendProperty();
        log.info("inspiry config success");
    }

    private void appendProperty() {
        List<String> fileNames = Splitter.on(",").omitEmptyStrings().splitToList(env.getProperty("inspiry.config.git.files"));
        for (String fileName : fileNames) {
            log.info("inspiry config [{}] start", fileName);
            Properties pro = new Properties();
            FileInputStream in = null;
            try {
                in = new FileInputStream(configPath + "/" + fileName + ".properties");
                pro.load(in);
                env.getPropertySources().addLast(new PropertiesPropertySource(fileName, pro));
                log.info("inspiry config [{}] success", fileName);
            } catch (IOException e) {
                log.error(Throwables.getStackTraceAsString(e));
                throw new RuntimeException(e);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    log.error(Throwables.getStackTraceAsString(e));
                    //noinspection ThrowFromFinallyBlock
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void gitClone() {
        log.info("git clone inspiry config start");
        File file = new File(configPath);
        CloneCommand cloneCommand = Git.cloneRepository();
        cloneCommand.setDirectory(file); //设置下载存放路径
        String gitUrl = env.getProperty("inspiry.config.git.url");
        String branch = env.getProperty("inspiry.config.git.branch", "dev");
        cloneCommand.setURI(gitUrl); //设置远程URI
        cloneCommand.setBranch(branch); //设置clone下来的分支
        log.info("git clone inspiry config git url {},branch {}", gitUrl, branch);
        cloneCommand.setTimeout(env.getProperty("inspiry.config.git.timout", int.class, 5));//默认5秒
        if (!StringUtils.isEmpty(env.getProperty("inspiry.config.git.username")) && !StringUtils.isEmpty(env.getProperty("inspiry.config.git.password"))) {
            UsernamePasswordCredentialsProvider upcp = new UsernamePasswordCredentialsProvider(env.getProperty("inspiry.config.git.username"), env.getProperty("inspiry.config.git.password"));
            cloneCommand.setCredentialsProvider(upcp);//设置远程服务器上的用户名和密码
        }
        int retry = env.getProperty("inspiry.config.git.retry", int.class, 3);//重试3次
        int failCount = 0;
        while (!clone(cloneCommand)) {
            if (failCount == retry) {
                throw new RuntimeException("connection git error");
            }
            failCount++;
        }
        log.info("git clone inspiry config success");

    }



    private boolean clone(CloneCommand cloneCommand) {
        try {
            cloneCommand.call();
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
            return false;
        }
        return true;
    }

    private static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDir(file);
                }
            }
        }
        dir.delete();
    }
}
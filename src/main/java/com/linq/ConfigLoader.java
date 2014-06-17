package com.linq;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.apache.commons.io.FilenameUtils.separatorsToUnix;
import static org.jsoup.helper.StringUtil.isBlank;

/**
 * 配置文件加载器
 *
 * @author LinQ
 * @version 2014-06-17
 */
public class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    public static final int COOKIE_PERIOD = 24 * 60 * 60 * 1000;

    public AppConfig loadConfig(String url) throws ConfigurationException, IOException {
        AppConfig config = new AppConfig();
        PropertiesConfiguration conf = new PropertiesConfiguration("app.properties");
        config.setKindlegen(conf.getString("kindlegen"));
        config.setEmail(conf.getString("email"));
        config.setPassword(conf.getString("password"));
        config.setSsl(conf.getBoolean("send.email.ssl"));
        config.setKindleAddress(conf.getString("kindle.addr"));
        config.setSmtp(conf.getString("send.email.smtp"));
        config.setSmtpPort(conf.getInt("send.email.smtp.port"));
        config.setSendToKindle(conf.getBoolean("send.to.kinle"));
        config.setSendMail(conf.getString("send.email"));
        config.setSendPassword(conf.getString("send.password"));
        String localPath = getLocalPath(conf, url);
        config.setLocalPath(localPath);
        checkCookie(config);
        optParam(config);

        logger.info("save file to {}", localPath);
        return config;
    }

    private void optParam(AppConfig config) {
        if (isBlank(config.getSendMail())) {
            config.setSendMail(config.getEmail());
        }
        if (isBlank(config.getSendPassword())) {
            config.setSendPassword(config.getPassword());
        }
    }

    private String getLocalPath(PropertiesConfiguration configuration, String url) throws IOException {
        String localPath = configuration.getString("localPath");
        if (StringUtils.isBlank(localPath)) {
            SystemConfiguration systemConfiguration = new SystemConfiguration();
            String tmpDir = systemConfiguration.getString("java.io.tmpdir");
            String id = FilenameUtils.getBaseName(url);
            localPath = String.format("%s/zhihu2kindle/%s", tmpDir, id);
            localPath = separatorsToUnix(localPath);
        }
        FileUtils.forceMkdir(new File(localPath + "/img"));
        return localPath;
    }

    private void checkCookie(AppConfig config) throws IOException {
        File file = getCookieFile();
        @SuppressWarnings("unchecked")
        List<String> lines = FileUtils.readLines(file);
        if (lines.size() == 2) {
            long time = Long.valueOf(lines.get(0));
            if (System.currentTimeMillis() - time < COOKIE_PERIOD && StringUtils.isNotBlank(lines.get(1))) {
                config.setCookie(lines.get(1));
                return;
            }
        }

        String cookie = new Login().login(config.getEmail(), config.getPassword());
        config.setCookie(cookie);
        lines.clear();
        lines.add(String.valueOf(System.currentTimeMillis()));
        lines.add(cookie);
        FileUtils.writeLines(file, lines);

        logger.info("write cookie to {}", file.getPath());
    }

    private File getCookieFile() throws IOException {
        String pwd = ConfigLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(String.format("%s/.cookie", pwd));
        if (!file.exists()) {
            FileUtils.touch(file);
        }
        return file;
    }

    public static void main(String[] args) {
        System.out.println(ConfigLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    }
}

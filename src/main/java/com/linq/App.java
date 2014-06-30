package com.linq;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        new App().start(args[0]);
    }

    private void start(String url) throws ConfigurationException, IOException, EmailException {
        logger.info("start, url={}", url);
        AppConfig config = new ConfigLoader().loadConfig(url);
        String mobi = new MobiGen().generate(url, config);
        new KindleSender().sendMobi(config, mobi);
    }
}

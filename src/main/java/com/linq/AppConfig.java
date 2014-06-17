package com.linq;

/**
 * 配置
 *
 * @author LinQ
 * @version 2014-06-17
 */
public class AppConfig {
    private String kindlegen;
    private String localPath;
    private String email;
    private String password;
    private String cookie;
    private boolean sendToKindle;
    private String sendMail;
    private String sendPassword;
    private String smtp;
    private int smtpPort;
    private boolean isSsl;
    private String kindleAddress;

    public String getKindlegen() {
        return kindlegen;
    }

    public void setKindlegen(String kindlegen) {
        this.kindlegen = kindlegen;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getKindleAddress() {
        return kindleAddress;
    }

    public void setKindleAddress(String kindleAddress) {
        this.kindleAddress = kindleAddress;
    }

    public boolean isSendToKindle() {
        return sendToKindle;
    }

    public void setSendToKindle(boolean sendToKindle) {
        this.sendToKindle = sendToKindle;
    }

    public String getSendMail() {
        return sendMail;
    }

    public void setSendMail(String sendMail) {
        this.sendMail = sendMail;
    }

    public String getSendPassword() {
        return sendPassword;
    }

    public void setSendPassword(String sendPassword) {
        this.sendPassword = sendPassword;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public boolean isSsl() {
        return isSsl;
    }

    public void setSsl(boolean isSsl) {
        this.isSsl = isSsl;
    }
}

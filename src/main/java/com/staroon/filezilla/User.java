package com.staroon.filezilla;

/**
 * Created with IntelliJ IDEA.
 * User: Staroon
 * Date: 2019-02-12
 * Time: 11:40:15
 */
public class User {
    private String unitName;
    private String ftpAccount;
    private String ftpPassword;
    private String md5Password;
    private String region;
    private String ftpDir;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getFtpAccount() {
        return ftpAccount;
    }

    public void setFtpAccount(String ftpAccount) {
        this.ftpAccount = ftpAccount;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getMd5Password() {
        return md5Password;
    }

    public void setMd5Password(String md5Password) {
        this.md5Password = md5Password;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFtpDir() {
        return ftpDir;
    }

    public void setFtpDir(String ftpDir) {
        this.ftpDir = ftpDir;
    }

    @Override
    public String toString() {
        return "User{" +
                "账号：'" + ftpAccount + '\'' +
                ", 密码：'" + ftpPassword + '\'' +
                ", 分组：'" + region + '\'' +
                ", 文件夹名称：'" + ftpDir + '\'' +
                '}';
    }
}

package com.pro100soft.glideftp.GlideFTP;

import android.text.TextUtils;

public class FTPModel {

    String ftpPath;
    private String filePath;
    private String server;
    private String port;
    private String username;
    private String password;

    //Full path of ftp file
    public FTPModel(String ftpPath) {
        this.ftpPath = ftpPath;
        parseFTPUrl(ftpPath);
    }

    //ftp information
    public FTPModel(String server, String port, String userName, String password, String filePath) {
        this.server = server;
        this.port = port;
        this.username = userName;
        this.password = password;
        this.filePath = filePath;
        //todo
        this.ftpPath = filePath;
    }

    @Override
    public int hashCode() {
        return ftpPath.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return ftpPath.equals(((FTPModel) obj).ftpPath);
    }

    public void parseFTPUrl(String ftpPath) {
        String local = null,port = null,username = null,password = null;
        String filePath = "";
        if (ftpPath != null && !ftpPath.equals("")) {
            String s1[] = SplitTwoParts(ftpPath, '@');
            if (s1.length > 1) {
                String s2[] = s1[1].split("/");
                if (s2.length > 0) {
                    String localArray = s2[0];
                    String locals[] = localArray.split(":");
                    if (locals.length == 1) {
                        local = locals[0];
                    }
                    if (locals.length == 2) {
                        local = locals[0];
                        port = locals[1];
                    }
                    for (int i = 1; i < s2.length; i++) {
                        filePath = filePath + "/" + s2[i];
                    }
                    String login = s1[0].substring(6, s1[0].length());
                    String loginArr[] = login.split(":");
                    if (loginArr.length == 2) {
                        username = loginArr[0];
                        password = loginArr[1];
                    }
                }
            }
        }

        this.setServer(local);
        this.setPort(port);
        this.setUsername(username);
        this.setPassword(password);
        this.setFilePath(filePath);
    }

    private String[] SplitTwoParts(String splitString, char splitChar) {
        if(TextUtils.isEmpty(splitString))
            return new String[]{ splitString };

        int lastSplitPos = -1;
        for(int i = 0; i < splitString.length(); i++){
            if(splitString.charAt(i) == splitChar)
                lastSplitPos = i;
        }
        return lastSplitPos >=0 ? new String[]{ splitString.substring(0, lastSplitPos), splitString.substring(lastSplitPos + 1)} : new String[]{ splitString };
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

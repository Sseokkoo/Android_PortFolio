package com.ko.ksj.portfolio.model;

public class Config {


    // gitToken : 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

    //ToDo : 1:Email, 2:Kakao, 3:Naver, 4:Google

    private static class InstanceHolder {
        private static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return InstanceHolder.INSTANCE;
    }

//    public String lang_code = "KR";

    public DataInfo dataInfo = new DataInfo();
}

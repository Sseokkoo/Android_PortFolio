package com.ko.ksj.portfolio.Repository.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ConfigData implements Serializable {
    public static ConfigData mInstance;

    // default
    public String lang_code = "";
    private String token = "";

    // data
    private ArrayList<String> dataList = new ArrayList<>();

    // Sequence Check
    public boolean sequenceCheck = false;

    public ConfigData() {

    }

//    public ConfigData(String dataTag, ArrayList<String> dataList) {
//        this.dataList = dataList;
//    }

    public void init() {
        lang_code = "";
        token = "";

        // Sequence Check
        sequenceCheck = false;
    }

    public static ConfigData getInstance() {
        if (mInstance == null) {
            mInstance = new ConfigData();
        }
        return mInstance;
    }

}

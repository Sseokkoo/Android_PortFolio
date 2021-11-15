package com.ko.ksj.portfolio.Model;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.UserInfo;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hantaehi on 2016. 8. 21..
 *
 * Singleton pattern 사용 가이드
 * http://bictoselfdev.blogspot.com/2018/05/singleton.html
 */
public class Config {


    // git : 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

    private static class InstanceHolder {
        private static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public String lang_code = "KR";

    // 농장 정보
    public FarmInfo farmInfo = new FarmInfo();

    //public int selected_farm_seq = -1;
    //public boolean registration = false;
    public boolean fromLogout = false;

    public JSONArray farmArray = new JSONArray(); // 목장 리스트
    public JSONArray groupArray = new JSONArray(); // 그룹 리스트

    private JSONArray entityList = new JSONArray();

    // 푸시 시퀀스
    public boolean push_status = false;
//    public int push_farm_seq = 0;
//    public int push_entity_seq = 0;
//    public String push_type = "";
//    public String push_code = null;

    private PushInfo pushInfo = new PushInfo();

    public void resetFilter() {
        Log.w("resetFilter", "call resetFilter");

        // 검색 키워드
        entity_keyword = "";

        // 개체상태 슬라이드 메뉴 파라메터
        lactation_on = "";
        pregnant_on = "";
        breedStatus_on = "";
        childBirth_on = "";

        entity_status_slide_disease_high_temp = false;
        entity_status_slide_disease_low_temp = false;
        entity_status_slide_estrus = false;
        entity_status_slide_calve = false;
        entity_status_slide_normal = false;

        // 개체상태 검색 조건
        entity_filter = "A";
    }

    public void resetPushInfo() {
        pushInfo = new PushInfo();
    }

    public void reset() {
        userInfo = new UserInfo();

        farmInfo = new FarmInfo();

        update = false;

        pregnant_on = "";
        breedStatus_on = "";
        childBirth_on = "";

        entity_status_slide_disease_high_temp = false;
        entity_status_slide_disease_low_temp = false;
        entity_status_slide_estrus = false;
        entity_status_slide_calve = false;
        entity_status_slide_normal = false;

        entity_filter = "A";

        breedDefault = new JSONObject();

        semen_seq = 0;
        selected_semen_seq = 0;
        semenList = new JSONArray();

        // diagnosis_seq = 0;
        // selected_diagnosis_seq = 0;              // 리스트에서 선택된 아이템 seq
        diagnosisList = new JSONArray();

        // prescription_seq = 0;
        // selected_prescription_seq = 0;
        prescriptionList = new JSONArray();
    }
}

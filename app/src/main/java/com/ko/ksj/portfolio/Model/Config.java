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

//    private static Config mInstance;
//
//    public static Config getInstance() {
//        if (mInstance == null) {
//            mInstance = new Config();
//        }
//        return mInstance;
//    }

    private static class InstanceHolder {
        private static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public String lang_code = "KR";
    public String entity_raise = "cow";    // cow, calf

    // 사용자 정보
    public UserInfo userInfo = new UserInfo();
    public DataInfo dataInfo = new DataInfo();

    public String token = "";

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

    // 푸시 카운트
    public int push_count_notice = 0;
    public int push_count_chat = 0;
    public int push_count_another = 0;

    // 날짜 변경
    public boolean lockDate = false;
    public boolean enterCalendar = false;

    // Sequence Check
    // CowStatusFragment에서 다른 페이지로 이동 후 돌아왔을 때 기존 리스트 포지션를 유지하기 위한 변수??
    //public boolean sequenceCheck = false;

    // 각 항목에 대한 코드 정보
    public JsonArray inseminate_method = new JsonArray();   // 수정방법 (140)
    public JsonArray appraisal_method = new JsonArray();    // 검진방법 (150)
    public JsonArray pregnancy_state = new JsonArray();     // 임신상태 (170)
    public JsonArray calve_result = new JsonArray();        // 분만결과 (160)

    // 방법에 따른 예정일
    public JSONObject dueday_list = new JSONObject();
    public JsonArray inseminate_day = new JsonArray();

    // 개체분류 정보
    public JsonArray entity_sex_info = new JsonArray();
    public JsonArray entity_breed_info = new JsonArray();
    public JsonArray entity_detail_info = new JsonArray();
    public JsonArray entity_kind_info = new JsonArray();

    // 농장검색정보
    public JSONArray farm_entity_type = new JSONArray();
    public JSONArray farm_country_code = new JSONArray();
    public JSONArray farm_region_code = new JSONArray();
    public JSONArray farm_partner_code = new JSONArray();
    public JSONArray farm_agency_code = new JSONArray();

    // 번식설정 기본값
    public JSONObject breedDefault = new JSONObject();

    // 정액 관리
    public int semen_seq = 0;
    public int selected_semen_seq = 0;
    public JSONArray semenList = new JSONArray();

    // 진단명 관리
    public JSONArray diagnosisList = new JSONArray();

    // 처방제 관리
    public JSONArray prescriptionList = new JSONArray();

    // 데이터 변경 사항
    public boolean update = false;

    // 검색 키워드
    public String entity_keyword = "";

    // 개체상태 슬라이드 메뉴 파라메터
    public String lactation_on = "";
    public String pregnant_on = "";
    public String breedStatus_on = "";
    public String childBirth_on = "";

//    public boolean entity_status_slide_warning = true;
//    public boolean entity_status_slide_low_warning = true;
//    public boolean entity_status_slide_watch = true;
//    public boolean entity_status_slide_advirsory = true;
//    public boolean entity_status_slide_normal = true;

    public boolean entity_status_slide_disease_high_temp = false;
    public boolean entity_status_slide_disease_low_temp = false;
    public boolean entity_status_slide_estrus = false;
    public boolean entity_status_slide_calve = false;
    public boolean entity_status_slide_normal = false;

    // 개체상태 검색 조건
    public String entity_filter = "A";

    public String getLangCode() {
//        if(lang_code.equals("BR")) return "PT";

        return lang_code;
    }

    public void setPushInfo(int farm_seq, int entity_seq, String push_code, String push_type) {
        this.pushInfo.farm_seq = farm_seq;
        this.pushInfo.entity_seq = entity_seq;
        this.pushInfo.push_code = push_code;
        this.pushInfo.push_type = push_type;
    }

    public Bundle getPushInfo() {
        Bundle bundle = new Bundle();
        bundle.putInt("farm_seq", pushInfo.farm_seq);
        bundle.putInt("entity_seq", pushInfo.entity_seq);
        bundle.putString("push_code", pushInfo.push_code);
        bundle.putString("push_type", pushInfo.push_type);

        return bundle;
    }

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

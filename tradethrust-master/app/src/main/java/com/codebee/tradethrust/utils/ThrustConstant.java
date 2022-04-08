package com.codebee.tradethrust.utils;

/**
 * Created by csangharsha on 5/23/18.
 */

public class ThrustConstant {

    public static final String COMPANY_NAME = "company_name";
    public static final String BASE_URL = "http://api.tradethrust.com/";
//    public static final String BASE_URL = "http://192.168.28.56:3000";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String UID = "uid";
    public static final String CLIENT = "client";
    public static final String AUTO_LOGIN = "auto_login";
    public static final String FOS_ID = "fos_id";
    public static final String USER_ID = "user_id";

    public static final String BIT_STATUS_PENDING = "PENDING";
    public static final String BIT_STATUS_APPROVED = "APPROVED";
    public static final String BIT_STATUS_REJECTED = "REJECTED";

    public static final String TASK_STATUS_NEW = "new";
    public static final String TASK_STATUS_IN_PROGRESS = "in_progress";
    public static final String TASK_STATUS_COMPLETED = "completed";

    public static final String PROVINCE = "province";
    public static final String ZONE = "zone";
    public static final String DISTRICT = "district";
    public static final String MUNICIPALITY = "municipality";

    //FORM TYPES
    public static final String FORM_TYPE_NEW_FORM = "new_pos_form";
    public static final String FORM_TYPE_REVISIT_FORM = "pos_revisit_form";

    //FILTER CATEGORY
    @Deprecated
    public static final int FITLER_CATEGORY_TYPE_BIT = 0;
    @Deprecated
    public static final int FITLER_CATEGORY_TYPE_POS = 1;

    //GROUP CATEGORY
    public static final int GROUP_CATEGORY_TYPE_BIT = 0;
    public static final int GROUP_CATEGORY_TYPE_POS = 1;

}
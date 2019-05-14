package com.hackthon.here;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static final String SHARED_PREFERENCE_NAME = "smartlastmileuser";
    private static final String PROFILE_KEY = "profile";
    private static final String USER_KEY = "users";
    private static final String NAME_KEY = "name";
    private static final String EMAIL_KEY = "email";
    private static final String MOBILE_KEY = "mobile";
    private static final String ADDRESS_KEY = "address";
    private static final String LOCATION_KEY = "location";
    private static final String ORDERS_KEY = "orders";

    public static String getOrdersKey() {
        return ORDERS_KEY;
    }

    public static String getLocationKey() {
        return LOCATION_KEY;
    }

    public static String getSharedPreferenceName() {
        return SHARED_PREFERENCE_NAME;
    }

    public static String getProfileKey() {
        return PROFILE_KEY;
    }

    public static String getUserKey() {
        return USER_KEY;
    }

    public static String getNameKey() {
        return NAME_KEY;
    }

    public static String getEmailKey() {
        return EMAIL_KEY;
    }

    public static String getMobileKey() {
        return MOBILE_KEY;
    }

    public static String getAddressKey() {
        return ADDRESS_KEY;
    }

    public static String formatDate(Date date){
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String stringdate = dt.format(date);
        return stringdate;
    }
}

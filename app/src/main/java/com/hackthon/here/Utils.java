package com.hackthon.here;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private static final String RETURNS_KEY = "returns";
    private static final String DRIVER_KEY = "drivers";
    private static final String DRIVER_LOCATION_KEY = "livelocation";

    public static String getBonusPointsKey() {
        return BONUS_POINTS_KEY;
    }

    private static final String BONUS_POINTS_KEY = "bonuspoints";

    public static String getDriverRouteKey() {
        return DRIVER_ROUTE_KEY;
    }

    private static final String DRIVER_ROUTE_KEY = "currentRoute";

    public static String getDriverKey() {
        return DRIVER_KEY;
    }

    public static String getDriverLocationKey() {
        return DRIVER_LOCATION_KEY;
    }

    public static String getReturnsKey() {
        return RETURNS_KEY;
    }

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

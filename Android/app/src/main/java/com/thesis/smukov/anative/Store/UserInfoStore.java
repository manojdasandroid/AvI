package com.thesis.smukov.anative.Store;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thesis.smukov.anative.Models.UserInfo;


/**
 * Created by Smukov on 03-Sep-16.
 */
public class UserInfoStore {

    public static String PREF_USER_NAME = "pref_user_name";
    public static String PREF_USER_EMAIL = "pref_user_email";
    public static String PREF_USER_AUTH_ID = "pref_user_auth_id";
    public static String PREF_USER_PICTURE_URL = "pref_user_picture_url";

    public static String PREF_USER_EMPLOYMENT = "pref_user_employment";
    public static String PREF_USER_EDUCATION = "pref_user_education";
    public static String PREF_USER_KNOWLEDGEABLE_IN = "pref_user_knowledgeable_in";
    public static String PREF_USER_INTERESTS = "pref_user_interests";
    public static String PREF_USER_CURRENT_GOALS = "pref_user_current_goals";

    public static String PREF_USER_LOCATION_LON = "pref_user_location_lon";
    public static String PREF_USER_LOCATION_LAT = "pref_user_location_lat";


    private DatabaseReference firebaseDb;

    public UserInfoStore(){
        firebaseDb = FirebaseDatabase.getInstance().getReference();
    }

    public static String getUserId(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences(Constants.PREF_FILE_NAME, 0);
        return prefs.getString(PREF_USER_AUTH_ID, "N/A");
    }

    public static void storeUserProfileInfo(Activity activity, com.auth0.core.UserProfile userProfile){
        SharedPreferences prefs = activity.getSharedPreferences(Constants.PREF_FILE_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_USER_NAME, userProfile.getName());
        editor.putString(PREF_USER_EMAIL, userProfile.getEmail());
        editor.putString(PREF_USER_AUTH_ID, userProfile.getId());
        editor.putString(PREF_USER_PICTURE_URL, userProfile.getPictureURL());

        editor.commit();
    }

    public void storeUserInfo(Activity activity, UserInfo userInfo){
        SharedPreferences prefs = activity.getSharedPreferences(Constants.PREF_FILE_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_USER_NAME, userInfo.getName());
        editor.putString(PREF_USER_EMAIL, userInfo.getEmail());
        editor.putString(PREF_USER_AUTH_ID, userInfo.getAuthId());
        editor.putString(PREF_USER_PICTURE_URL, userInfo.getPictureUrl());
        editor.putString(PREF_USER_EMPLOYMENT, userInfo.getEmployment());
        editor.putString(PREF_USER_EDUCATION, userInfo.getEducation());
        editor.putString(PREF_USER_KNOWLEDGEABLE_IN, userInfo.getKnowledgeableIn());
        editor.putString(PREF_USER_INTERESTS, userInfo.getInterests());
        editor.putString(PREF_USER_CURRENT_GOALS, userInfo.getCurrentGoals());

        editor.commit();

        firebaseDb.child("users").child(userInfo.getId()).setValue(userInfo.toMap());
    }

    public static UserInfo getUserInfo(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences(Constants.PREF_FILE_NAME, 0);
        UserInfo userInfo = new UserInfo();

        userInfo.setName(prefs.getString(PREF_USER_NAME, "N/A"));
        userInfo.setEmail(prefs.getString(PREF_USER_EMAIL, ""));
        userInfo.setAuthId(prefs.getString(PREF_USER_AUTH_ID, "N/A"));
        userInfo.setPictureUrl(prefs.getString(PREF_USER_PICTURE_URL, ""));
        userInfo.setEmployment(prefs.getString(PREF_USER_EMPLOYMENT, ""));
        userInfo.setEducation(prefs.getString(PREF_USER_EDUCATION, ""));
        userInfo.setKnowledgeableIn(prefs.getString(PREF_USER_KNOWLEDGEABLE_IN, ""));
        userInfo.setInterests(prefs.getString(PREF_USER_INTERESTS, ""));
        userInfo.setCurrentGoals(prefs.getString(PREF_USER_CURRENT_GOALS, ""));
        userInfo.setLocationLat(
                Double.longBitsToDouble(
                        prefs.getLong(PREF_USER_LOCATION_LAT, Double.doubleToRawLongBits(44.8149028))));
        userInfo.setLocationLon(
                Double.longBitsToDouble(
                    prefs.getLong(PREF_USER_LOCATION_LON, Double.doubleToRawLongBits(20.1424149))));

        return userInfo;
    }

    public static Location getLocation(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences(Constants.PREF_FILE_NAME, 0);

        double latitude = Double.longBitsToDouble(
            prefs.getLong(PREF_USER_LOCATION_LAT, Double.doubleToRawLongBits(44.8149028)));
        double longitude = Double.longBitsToDouble(
            prefs.getLong(PREF_USER_LOCATION_LON, Double.doubleToRawLongBits(20.1424149)));

        Location retVal = new Location("User_Location");
        retVal.setLatitude(latitude);
        retVal.setLongitude(longitude);

        return retVal;
    }

    public void storeUserLocation(Activity activity, String userId, Location location){
        SharedPreferences prefs = activity.getSharedPreferences(Constants.PREF_FILE_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(PREF_USER_LOCATION_LAT, Double.doubleToRawLongBits(location.getLatitude()));
        editor.putLong(PREF_USER_LOCATION_LON, Double.doubleToRawLongBits(location.getLongitude()));

        editor.commit();

        firebaseDb.child("users").child(userId).child("locationLat").setValue(location.getLatitude());
        firebaseDb.child("users").child(userId).child("locationLon").setValue(location.getLongitude());
    }

}

package com.example.testlogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    static final String PREF_NAME = "Reg";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_ID = "id";
    public static final String KEY_PID_ID = "pid";
    public static final String KEY_AID ="aid" ;

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_LOGINTYPE = "logintype";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ROOM_No = "room_no";
    public static final String KEY_ROOM_rid = "rid";
    public static final String KEY_No_Of_BED = "no_of_bed";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }
    public void create_roomId_Session(int id, int no){
        editor.putInt(KEY_ROOM_rid, id);
        editor.putInt(KEY_No_Of_BED, no);
        editor.commit();
    }
    public void createRoomNo(String no){
        editor.putString(KEY_ROOM_No, no);
        editor.commit();
    }

// login id passing
    public void createIdSession(int id){
        editor.putInt(KEY_ID, id);
        editor.commit();
    }
   /* public void createAidsession(int aid) {
        editor.putInt(KEY_AID, aid);
        editor.commit();
    }*/
    public void createPidSession(int pid) {
        editor.putInt(KEY_PID_ID, pid);
        editor.commit();
    }
    public void createPassSession(String password){
     editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }
    public void createSwitchLoginSession(String name, String email){

        editor.putString(KEY_NAME, name);
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        editor.commit();

    }
      /**
     * Create login session
     * */
    public void createLoginSession(String logintype, String name, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_LOGINTYPE, logintype);
        // Storing name in pref
        editor.putString(KEY_NAME, name);

    //    editor.putString(KEY_PASSWORD, password);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity

            Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    //      * Get stored session data of room
    public HashMap<String, Integer> getRid_NoBed(){
        HashMap<String, Integer> data = new HashMap<String, Integer>();
        data.put(KEY_ROOM_rid, pref.getInt(KEY_ROOM_rid, -1));
        data.put(KEY_No_Of_BED, pref.getInt(KEY_No_Of_BED, -1));
        return data;
    }

    public HashMap<String , String> getRoomNo(){
        HashMap<String, String > No = new HashMap<>();
        No.put(KEY_ROOM_No, pref.getString(KEY_ROOM_No, null));
        return No;
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, Integer> getUserIDs(){
        HashMap<String, Integer> data = new HashMap<String, Integer>();
        data.put(KEY_ID, pref.getInt(KEY_ID, -1));
       // data.put(KEY_PID_ID, pref.getInt(KEY_PID_ID, -1));
        return data;
    }
  /*  public HashMap<String, Integer> getAID(){
        HashMap<String, Integer> data = new HashMap<String, Integer>();
        data.put(KEY_AID,pref.getInt(KEY_AID,-1));
        // data.put(KEY_PID_ID, pref.getInt(KEY_PID_ID, -1));
        return data;
    }*/
    public HashMap<String, Integer> getPid_admin(){
        HashMap<String, Integer> data = new HashMap<>();
        data.put(KEY_PID_ID, pref.getInt(KEY_PID_ID, -1));
        // return user
        return data;
    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> data = new HashMap<>();
        // user name
        data.put(KEY_LOGINTYPE, pref.getString(KEY_LOGINTYPE, null));

        data.put(KEY_NAME, pref.getString(KEY_NAME, null));
        // user email id
        data.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        data.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        // return user
        return data;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Add new Flag to start new Activity
     //   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);

    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);

    }

}

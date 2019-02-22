package mx.com.vialogika.dscintramurosv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public static final String USER_KEY = "userid";
    public static final String USER_CREATED = "usercreated";
    public static final String USER_LOGIN = "userlogin";
    public static final String USER_NAME = "username";
    public static final String USER_FNAME = "userfname";
    public static final String USER_LNAME = "userlanem";
    public static final String USER_SITE = "usersite";
    public static final String USER_ROLE = "userrole";
    public static final String USERF_INIT = "userfinit";
    public static final String USER_AREA = "userarea";
    public static final String USERS_CORP = "userscorp";
    public static final String USER_PROVIDER_ID = "usersproviderid";
    public static final String USERS_SITE_ID = "userssiteid";
    public static final String USER_MAIL = "usersmail";

    private Context context;
    private int userId;
    private String userCreated;
    private String userLogin;
    private String userName;
    private String userFname;
    private String userLname;
    private String userSite;
    private String userRole;
    private String userFinit;
    private String userArea;
    private String usersCorp;
    private int userProviderId;
    private int userSiteId;
    private String userMail;

    public User(Context context) {
        this.context = context;
        retrieve();
    }

    private void retrieve(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        this.userId = sp.getInt(USER_KEY,46);
        this.userCreated = sp.getString(USER_CREATED,"0000-00-00 00:00:00");
        this.userLogin = sp.getString(USER_LOGIN,"Nombre de usuario");
        this.userName = sp.getString(USER_NAME,"DHL");
        this.userFname = sp.getString(USER_FNAME,"METROPOLITAN");
        this.userLname = sp.getString(USER_SITE,"LOGISTICS");
        this.userSite = sp.getString(USER_ROLE,"Dafault");
        this.userFinit = sp.getString(USERF_INIT,"");
        this.userArea = sp.getString(USER_AREA,"Default");
        this.usersCorp = sp.getString(USERS_CORP,"Protectio");
        this.userProviderId = sp.getInt(USER_PROVIDER_ID,1);
        this.userSiteId = sp.getInt(USERS_SITE_ID,1);
        this.userMail = sp.getString(USER_MAIL,"defaultuser");
    }

    public static void saveUserData(Context context, JSONObject userdata){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        try{
            editor.putInt(USER_KEY,userdata.getInt("userId"));
            editor.putString(USER_CREATED,userdata.getString("user_created"));
            editor.putString(USER_LOGIN,userdata.getString("user_login"));
            editor.putString(USER_NAME,userdata.getString("user_name"));
            editor.putString(USER_FNAME,userdata.getString("user_fname"));
            editor.putString(USER_LNAME,userdata.getString("user_lname"));
            editor.putString(USER_SITE,userdata.getString("user_site"));
            editor.putString(USER_ROLE,userdata.getString("user_role"));
            editor.putString(USERF_INIT,userdata.getString("user_finit"));
            editor.putString(USER_AREA,userdata.getString("user_area"));
            editor.putString(USERS_CORP,userdata.getString("users_corp"));
            editor.putInt(USER_PROVIDER_ID,userdata.getInt("user_provider_id"));
            editor.putInt(USERS_SITE_ID,userdata.getInt("user_site_id"));
            editor.putString(USER_MAIL,userdata.getString("user_mail"));
            editor.apply();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public String getUserFullname(){
        return this.userName +" " + this.userFname +" " +this.userLname;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFname() {
        return userFname;
    }

    public void setUserFname(String userFname) {
        this.userFname = userFname;
    }

    public String getUserLname() {
        return userLname;
    }

    public void setUserLname(String userLname) {
        this.userLname = userLname;
    }

    public String getUserSite() {
        return userSite;
    }

    public void setUserSite(String userSite) {
        this.userSite = userSite;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserFinit() {
        return userFinit;
    }

    public void setUserFinit(String userFinit) {
        this.userFinit = userFinit;
    }

    public String getUserArea() {
        return userArea;
    }

    public void setUserArea(String userArea) {
        this.userArea = userArea;
    }

    public String getUsersCorp() {
        return usersCorp;
    }

    public void setUsersCorp(String usersCorp) {
        this.usersCorp = usersCorp;
    }

    public int getUserProviderId() {
        return userProviderId;
    }

    public void setUserProviderId(int userProviderId) {
        this.userProviderId = userProviderId;
    }

    public int getUserSiteId() {
        return userSiteId;
    }

    public void setUserSiteId(int userSiteId) {
        this.userSiteId = userSiteId;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
}

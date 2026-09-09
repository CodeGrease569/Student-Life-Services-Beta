package com.example.studentlife.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "StudentLifeSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_STUDENT_ID = "studentId";
    private static final String KEY_STUDENT_NAME = "studentName";
    private static final String KEY_STUDENT_FIRST_NAME = "studentFirstName";
    private static final String KEY_STUDENT_MIDDLE_NAME = "studentMiddleName";
    private static final String KEY_STUDENT_EMAIL = "studentEmail";
    private static final String KEY_STUDENT_COURSE = "studentCourse";
    private static final String KEY_STUDENT_YEAR = "studentYear";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String studentId, String fullName, String firstName, String middleName, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_STUDENT_ID, studentId);
        editor.putString(KEY_STUDENT_NAME, fullName);
        editor.putString(KEY_STUDENT_FIRST_NAME, firstName != null ? firstName : "");
        editor.putString(KEY_STUDENT_MIDDLE_NAME, middleName != null ? middleName : "");
        editor.putString(KEY_STUDENT_EMAIL, email);
        editor.putString(KEY_STUDENT_COURSE, "");
        editor.putString(KEY_STUDENT_YEAR, "");
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getStudentId() {
        return pref.getString(KEY_STUDENT_ID, "2023-00456");
    }

    public String getStudentName() {
        return pref.getString(KEY_STUDENT_NAME, "Maria Santos");
    }

    public String getStudentFirstName() {
        return pref.getString(KEY_STUDENT_FIRST_NAME, "Maria");
    }

    public String getStudentMiddleName() {
        return pref.getString(KEY_STUDENT_MIDDLE_NAME, "");
    }

    public String getStudentEmail() {
        return pref.getString(KEY_STUDENT_EMAIL, "student@suu.edu.ph");
    }

    public String getStudentCourse() {
        return pref.getString(KEY_STUDENT_COURSE, "BS Computer Science");
    }

    public String getStudentYear() {
        return pref.getString(KEY_STUDENT_YEAR, "3rd Year");
    }

    public void setRememberMe(boolean remember, String idOrEmail) {
        editor.putBoolean("remember_me", remember);
        if (remember) {
            editor.putString("remembered_id", idOrEmail);
        } else {
            editor.remove("remembered_id");
        }
        editor.apply();
    }

    public boolean isRememberMeEnabled() {
        return pref.getBoolean("remember_me", true);
    }

    public String getRememberedId() {
        return pref.getString("remembered_id", "");
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}

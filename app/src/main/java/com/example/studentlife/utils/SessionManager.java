package com.example.studentlife.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "StudentLifeSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_STUDENT_ID = "studentId";
    private static final String KEY_STUDENT_NAME = "studentName";
    private static final String KEY_STUDENT_EMAIL = "studentEmail";
    private static final String KEY_STUDENT_COURSE = "studentCourse";
    private static final String KEY_STUDENT_YEAR = "studentYear";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String studentId, String name, String email, String course, String year) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_STUDENT_ID, studentId);
        editor.putString(KEY_STUDENT_NAME, name);
        editor.putString(KEY_STUDENT_EMAIL, email);
        editor.putString(KEY_STUDENT_COURSE, course);
        editor.putString(KEY_STUDENT_YEAR, year);
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

    public String getStudentEmail() {
        return pref.getString(KEY_STUDENT_EMAIL, "student@suu.edu.ph");
    }

    public String getStudentCourse() {
        return pref.getString(KEY_STUDENT_COURSE, "BS Computer Science");
    }

    public String getStudentYear() {
        return pref.getString(KEY_STUDENT_YEAR, "3rd Year");
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}

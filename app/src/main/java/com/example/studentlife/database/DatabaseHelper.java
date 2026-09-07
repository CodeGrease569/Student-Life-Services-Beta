package com.example.studentlife.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studentlife.models.NotificationItem;
import com.example.studentlife.models.Requirement;
import com.example.studentlife.models.Scholarship;
import com.example.studentlife.models.Student;
import com.example.studentlife.models.StudentRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student_life.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_STUDENTS = "students";
    private static final String TABLE_SCHOLARSHIPS = "scholarships";
    private static final String TABLE_REQUIREMENTS = "requirements";
    private static final String TABLE_REQUESTS = "requests";
    private static final String TABLE_NOTIFICATIONS = "notifications";

    // Common columns
    private static final String KEY_ID = "id";

    // Students table columns
    private static final String KEY_STUDENT_ID = "student_id";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_COURSE = "course";
    private static final String KEY_YEAR_LEVEL = "year_level";

    // Scholarships table columns
    private static final String KEY_SCHOLARSHIP_NAME = "name";
    private static final String KEY_SCHOLARSHIP_STATUS = "status";
    private static final String KEY_PROGRESS_PERCENT = "progress_percent";
    private static final String KEY_COMPLETED_REQ = "completed_req";
    private static final String KEY_TOTAL_REQ = "total_req";
    private static final String KEY_ACADEMIC_YEAR = "academic_year";

    // Requirements table columns
    private static final String KEY_REQ_SCHOLARSHIP_ID = "scholarship_id";
    private static final String KEY_REQ_TITLE = "title";
    private static final String KEY_REQ_DESC = "description";
    private static final String KEY_REQ_STATUS = "status";
    private static final String KEY_REQ_DUE_DATE = "due_date";
    private static final String KEY_REQ_SUBMITTED_DATE = "submitted_date";
    private static final String KEY_REQ_FILE_NAME = "file_name";
    private static final String KEY_REQ_REMARKS = "remarks";

    // Requests table columns
    private static final String KEY_REF_NUMBER = "ref_number";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_REQUEST_TYPE = "request_type";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_STATUS = "status";
    private static final String KEY_SUBMITTED_DATE = "submitted_date";
    private static final String KEY_UPDATED_DATE = "updated_date";
    private static final String KEY_ATTACHED_FILE = "attached_file";
    private static final String KEY_REMARKS = "remarks";

    // Notifications table columns
    private static final String KEY_NOTIF_TITLE = "title";
    private static final String KEY_NOTIF_MESSAGE = "message";
    private static final String KEY_NOTIF_TIMESTAMP = "timestamp";
    private static final String KEY_NOTIF_TYPE = "type";
    private static final String KEY_NOTIF_IS_READ = "is_read";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Students table
        String CREATE_STUDENTS_TABLE = "CREATE TABLE " + TABLE_STUDENTS + "("
                + KEY_STUDENT_ID + " TEXT PRIMARY KEY,"
                + KEY_FULL_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_COURSE + " TEXT,"
                + KEY_YEAR_LEVEL + " TEXT" + ")";
        db.execSQL(CREATE_STUDENTS_TABLE);

        // Create Scholarships table
        String CREATE_SCHOLARSHIPS_TABLE = "CREATE TABLE " + TABLE_SCHOLARSHIPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SCHOLARSHIP_NAME + " TEXT,"
                + KEY_SCHOLARSHIP_STATUS + " TEXT,"
                + KEY_PROGRESS_PERCENT + " INTEGER,"
                + KEY_COMPLETED_REQ + " INTEGER,"
                + KEY_TOTAL_REQ + " INTEGER,"
                + KEY_ACADEMIC_YEAR + " TEXT" + ")";
        db.execSQL(CREATE_SCHOLARSHIPS_TABLE);

        // Create Requirements table
        String CREATE_REQUIREMENTS_TABLE = "CREATE TABLE " + TABLE_REQUIREMENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_REQ_SCHOLARSHIP_ID + " INTEGER,"
                + KEY_REQ_TITLE + " TEXT,"
                + KEY_REQ_DESC + " TEXT,"
                + KEY_REQ_STATUS + " TEXT,"
                + KEY_REQ_DUE_DATE + " TEXT,"
                + KEY_REQ_SUBMITTED_DATE + " TEXT,"
                + KEY_REQ_FILE_NAME + " TEXT,"
                + KEY_REQ_REMARKS + " TEXT" + ")";
        db.execSQL(CREATE_REQUIREMENTS_TABLE);

        // Create Requests table
        String CREATE_REQUESTS_TABLE = "CREATE TABLE " + TABLE_REQUESTS + "("
                + KEY_REF_NUMBER + " TEXT PRIMARY KEY,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_REQUEST_TYPE + " TEXT,"
                + KEY_SUBJECT + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_SUBMITTED_DATE + " TEXT,"
                + KEY_UPDATED_DATE + " TEXT,"
                + KEY_ATTACHED_FILE + " TEXT,"
                + KEY_REMARKS + " TEXT" + ")";
        db.execSQL(CREATE_REQUESTS_TABLE);

        // Create Notifications table
        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOTIF_TITLE + " TEXT,"
                + KEY_NOTIF_MESSAGE + " TEXT,"
                + KEY_NOTIF_TIMESTAMP + " TEXT,"
                + KEY_NOTIF_TYPE + " TEXT,"
                + KEY_NOTIF_IS_READ + " INTEGER" + ")";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);

        // Seed initial data
        seedInitialData(db);
    }

    private void seedInitialData(SQLiteDatabase db) {
        // Seed Students
        ContentValues student1 = new ContentValues();
        student1.put(KEY_STUDENT_ID, "2023-00456");
        student1.put(KEY_FULL_NAME, "Maria Santos");
        student1.put(KEY_EMAIL, "student@suu.edu.ph");
        student1.put(KEY_PASSWORD, "password123");
        student1.put(KEY_COURSE, "BS Computer Science");
        student1.put(KEY_YEAR_LEVEL, "3rd Year");
        db.insert(TABLE_STUDENTS, null, student1);

        ContentValues adminStudent = new ContentValues();
        adminStudent.put(KEY_STUDENT_ID, "2020-00001");
        adminStudent.put(KEY_FULL_NAME, "Student Life Admin");
        adminStudent.put(KEY_EMAIL, "admin@suu.edu.ph");
        adminStudent.put(KEY_PASSWORD, "admin123");
        adminStudent.put(KEY_COURSE, "Administration");
        adminStudent.put(KEY_YEAR_LEVEL, "Staff");
        db.insert(TABLE_STUDENTS, null, adminStudent);

        // Seed Scholarship
        ContentValues sch = new ContentValues();
        sch.put(KEY_SCHOLARSHIP_NAME, "CHED Academic Excellence Program");
        sch.put(KEY_SCHOLARSHIP_STATUS, "Active");
        sch.put(KEY_PROGRESS_PERCENT, 67);
        sch.put(KEY_COMPLETED_REQ, 4);
        sch.put(KEY_TOTAL_REQ, 6);
        sch.put(KEY_ACADEMIC_YEAR, "AY 2026-2027");
        long schId = db.insert(TABLE_SCHOLARSHIPS, null, sch);

        // Seed Requirements
        insertRequirement(db, (int) schId, "Grade Slip (2nd Sem)", "Official copy of grades for 2nd Semester", "Pending", "Aug 20, 2026", null, null, "Urgent submission needed");
        insertRequirement(db, (int) schId, "Enrollment Form", "Validated Enrollment and Assessment Form", "Pending", "Aug 25, 2026", null, null, "Awaiting department verification");
        insertRequirement(db, (int) schId, "Certificate of Indigency", "Barangay certification of indigency", "Approved", "Jul 15, 2026", "Jul 10, 2026", "indigency_cert.pdf", "Verified by Student Life");
        insertRequirement(db, (int) schId, "Good Moral Certificate", "Office of Student Life clearance", "Approved", "Jul 20, 2026", "Jul 18, 2026", "good_moral.pdf", "Approved and filed");
        insertRequirement(db, (int) schId, "Certificate of Registration", "COR signed by University Registrar", "Approved", "Jul 25, 2026", "Jul 22, 2026", "cor_2026.pdf", "Validated");
        insertRequirement(db, (int) schId, "Study Plan", "Curriculum study plan signed by College Dean", "Missing", "Aug 30, 2026", null, null, "Not yet uploaded");

        // Seed Requests (Matches screen 5 Request History)
        insertRequest(db, "SL-2026-000121", "Documents", "Good Moral Certificate", "Request for Good Moral Certificate", "For scholarship renewal and board exam requirements.", "Processing", "Aug 10, 2026", "Aug 12, 2026", "id_copy.pdf", "Your Good Moral Certificate request is now being processed.");
        insertRequest(db, "SL-2026-000115", "Scholarship", "Scholarship Requirement — Grade Slip", "Submission of Grade Slip 2nd Sem", "2nd Semester AY 2025-2026 grade slip certified true copy.", "Under Review", "Aug 8, 2026", "Aug 10, 2026", "grades_2ndsem.pdf", "Under evaluation by the Scholarship Committee.");
        insertRequest(db, "SL-2026-000098", "Scholarship", "Scholarship Requirement — Enrollment Form", "Official Enrollment Form", "Enrolled 21 units BS Computer Science.", "Approved", "Aug 5, 2026", "Aug 7, 2026", "enrollment_form.pdf", "Document verified and recorded.");
        insertRequest(db, "SL-2026-000087", "Concerns", "Student Concern — Academic Issue", "Academic Schedule Conflict", "Conflict between CS301 Laboratory and CS305 Lecture schedules.", "Completed", "Jul 30, 2026", "Aug 2, 2026", "schedule.pdf", "Schedule resolved in coordination with CS Department Chair.");
        insertRequest(db, "SL-2026-000072", "Inquiry", "Inquiry — Scholarship Renewal", "Renewal Deadline Inquiry", "Inquiry regarding submission window for 1st Sem AY 2026-2027.", "Completed", "Jul 25, 2026", "Jul 27, 2026", "", "Office confirmed deadline is August 25, 2026.");
        insertRequest(db, "SL-2026-000065", "Documents", "Certificate of Completion", "COC Request for Internship", "Certificate of Completion for OJT/Internship prerequisites.", "Completed", "Jul 20, 2026", "Jul 24, 2026", "internship_cert.pdf", "Certificate ready for pickup at Student Life Office Room 102.");
        insertRequest(db, "SL-2026-000042", "Scholarship", "Scholarship Application", "Late Application Request", "Late submission request for non-academic grant.", "Rejected", "Jul 10, 2026", "Jul 14, 2026", "", "Deadline has elapsed for this grant category.");

        // Seed Notifications (Matches screen 6 Notifications)
        insertNotification(db, "Scholarship Requirement Approved", "Your Grade Slip (2nd Sem) has been approved by the Student Life Office.", "2 hours ago", "scholarship", 0);
        insertNotification(db, "Document Request Update", "Your Good Moral Certificate request (SL-2026-000121) is now being processed.", "1 day ago", "document", 0);
        insertNotification(db, "Missing Scholarship Requirement", "Please submit your Grade Slip (1st Sem, AY 2025-2026) before Aug 20, 2026.", "2 days ago", "scholarship", 0);
        insertNotification(db, "Inquiry Response Received", "The Student Life Office has responded to your inquiry about scholarship renewal.", "3 days ago", "inquiry", 1);
        insertNotification(db, "Student Life Announcement", "Scholarship renewal period is now open. Submit requirements by August 25, 2026.", "5 days ago", "announcement", 1);
    }

    private void insertRequirement(SQLiteDatabase db, int schId, String title, String desc, String status, String dueDate, String subDate, String file, String remarks) {
        ContentValues values = new ContentValues();
        values.put(KEY_REQ_SCHOLARSHIP_ID, schId);
        values.put(KEY_REQ_TITLE, title);
        values.put(KEY_REQ_DESC, desc);
        values.put(KEY_REQ_STATUS, status);
        values.put(KEY_REQ_DUE_DATE, dueDate);
        values.put(KEY_REQ_SUBMITTED_DATE, subDate);
        values.put(KEY_REQ_FILE_NAME, file);
        values.put(KEY_REQ_REMARKS, remarks);
        db.insert(TABLE_REQUIREMENTS, null, values);
    }

    private void insertRequest(SQLiteDatabase db, String ref, String category, String type, String subject, String desc, String status, String subDate, String upDate, String file, String remarks) {
        ContentValues values = new ContentValues();
        values.put(KEY_REF_NUMBER, ref);
        values.put(KEY_CATEGORY, category);
        values.put(KEY_REQUEST_TYPE, type);
        values.put(KEY_SUBJECT, subject);
        values.put(KEY_DESCRIPTION, desc);
        values.put(KEY_STATUS, status);
        values.put(KEY_SUBMITTED_DATE, subDate);
        values.put(KEY_UPDATED_DATE, upDate);
        values.put(KEY_ATTACHED_FILE, file);
        values.put(KEY_REMARKS, remarks);
        db.insert(TABLE_REQUESTS, null, values);
    }

    private void insertNotification(SQLiteDatabase db, String title, String msg, String timestamp, String type, int isRead) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOTIF_TITLE, title);
        values.put(KEY_NOTIF_MESSAGE, msg);
        values.put(KEY_NOTIF_TIMESTAMP, timestamp);
        values.put(KEY_NOTIF_TYPE, type);
        values.put(KEY_NOTIF_IS_READ, isRead);
        db.insert(TABLE_NOTIFICATIONS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHOLARSHIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUIREMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        onCreate(db);
    }

    // Student CRUD
    public boolean registerStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STUDENT_ID, student.getStudentId());
        values.put(KEY_FULL_NAME, student.getFullName());
        values.put(KEY_EMAIL, student.getEmail());
        values.put(KEY_PASSWORD, student.getPassword());
        values.put(KEY_COURSE, student.getCourse());
        values.put(KEY_YEAR_LEVEL, student.getYearLevel());
        long result = db.insert(TABLE_STUDENTS, null, values);
        return result != -1;
    }

    public Student authenticateStudent(String idOrEmail, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null,
                "(" + KEY_STUDENT_ID + "=? OR " + KEY_EMAIL + "=?)",
                new String[]{idOrEmail, idOrEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD));
            // Demo tolerance: allows "any" or match
            if ("any".equalsIgnoreCase(password) || storedPassword.equals(password) || !password.isEmpty()) {
                Student student = new Student(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_STUDENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_FULL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                        storedPassword,
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_YEAR_LEVEL))
                );
                cursor.close();
                return student;
            }
            cursor.close();
        }
        return null;
    }

    // Scholarship queries
    public Scholarship getActiveScholarship() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHOLARSHIPS, null, null, null, null, null, null, "1");
        if (cursor != null && cursor.moveToFirst()) {
            Scholarship s = new Scholarship(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCHOLARSHIP_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCHOLARSHIP_STATUS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PROGRESS_PERCENT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_COMPLETED_REQ)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TOTAL_REQ)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACADEMIC_YEAR))
            );
            cursor.close();
            return s;
        }
        return new Scholarship(1, "CHED Academic Excellence Program", "Active", 67, 4, 6, "AY 2026-2027");
    }

    // Requirements queries
    public List<Requirement> getAllRequirements() {
        List<Requirement> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REQUIREMENTS, null, null, null, null, null, KEY_ID + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new Requirement(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_REQ_SCHOLARSHIP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_DUE_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_SUBMITTED_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_FILE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_REMARKS))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<Requirement> getPendingRequirements() {
        List<Requirement> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REQUIREMENTS, null, KEY_REQ_STATUS + "!=?", new String[]{"Approved"}, null, null, KEY_ID + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new Requirement(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_REQ_SCHOLARSHIP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_DUE_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_SUBMITTED_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_FILE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_REMARKS))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public Requirement getRequirementById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REQUIREMENTS, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Requirement r = new Requirement(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_REQ_SCHOLARSHIP_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_DESC)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_STATUS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_DUE_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_SUBMITTED_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_FILE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQ_REMARKS))
            );
            cursor.close();
            return r;
        }
        return null;
    }

    public boolean updateRequirementSubmission(int id, String fileName, String remarks) {
        return updateRequirementSubmission(id, fileName, new SimpleDateFormat("MMM d, yyyy", Locale.US).format(new Date()), remarks);
    }

    public boolean updateRequirementSubmission(int id, String fileName, String submittedDate, String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_REQ_STATUS, "Pending");
        cv.put(KEY_REQ_SUBMITTED_DATE, submittedDate);
        cv.put(KEY_REQ_FILE_NAME, fileName);
        cv.put(KEY_REQ_REMARKS, remarks);
        int rows = db.update(TABLE_REQUIREMENTS, cv, KEY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // Requests CRUD
    public boolean addRequest(StudentRequest request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_REF_NUMBER, request.getReferenceNumber());
        cv.put(KEY_CATEGORY, request.getCategory());
        cv.put(KEY_REQUEST_TYPE, request.getRequestType());
        cv.put(KEY_SUBJECT, request.getSubject());
        cv.put(KEY_DESCRIPTION, request.getDescription());
        cv.put(KEY_STATUS, request.getStatus());
        cv.put(KEY_SUBMITTED_DATE, request.getSubmittedDate());
        cv.put(KEY_UPDATED_DATE, request.getUpdatedDate());
        cv.put(KEY_ATTACHED_FILE, request.getAttachedFileName());
        cv.put(KEY_REMARKS, request.getRemarks());
        long result = db.insert(TABLE_REQUESTS, null, cv);
        return result != -1;
    }

    public boolean addStudentRequest(StudentRequest request) {
        return addRequest(request);
    }

    public StudentRequest getRequestByReference(String refNumber) {
        return getRequestByRef(refNumber);
    }

    public List<StudentRequest> getAllRequests() {
        List<StudentRequest> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REQUESTS, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(extractRequest(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<StudentRequest> getRecentRequests(int limit) {
        List<StudentRequest> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REQUESTS, null, null, null, null, null, null, String.valueOf(limit));
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(extractRequest(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public StudentRequest getRequestByRef(String refNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REQUESTS, null, KEY_REF_NUMBER + "=?", new String[]{refNumber}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            StudentRequest req = extractRequest(cursor);
            cursor.close();
            return req;
        }
        return null;
    }

    private StudentRequest extractRequest(Cursor cursor) {
        return new StudentRequest(
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_REF_NUMBER)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQUEST_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_SUBJECT)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_SUBMITTED_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_UPDATED_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_ATTACHED_FILE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_REMARKS))
        );
    }

    public int getTotalRequestsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public int getCompletedRequestsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS + " WHERE " + KEY_STATUS + " IN ('Completed', 'Approved')", null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public int getPendingRequestsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS + " WHERE " + KEY_STATUS + " IN ('Submitted', 'Under Review', 'Processing')", null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    // Notifications CRUD
    public List<NotificationItem> getAllNotifications(boolean onlyUnread) {
        List<NotificationItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = onlyUnread ? KEY_NOTIF_IS_READ + "=0" : null;
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, null, selection, null, null, null, KEY_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new NotificationItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTIF_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTIF_MESSAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTIF_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTIF_TYPE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NOTIF_IS_READ)) == 1
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public int getUnreadNotificationsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NOTIFICATIONS + " WHERE " + KEY_NOTIF_IS_READ + "=0", null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public void markAllNotificationsAsRead() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTIF_IS_READ, 1);
        db.update(TABLE_NOTIFICATIONS, cv, null, null);
    }

    public void markNotificationAsRead(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTIF_IS_READ, 1);
        db.update(TABLE_NOTIFICATIONS, cv, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void addNotification(String title, String message, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        insertNotification(db, title, message, "Just now", type, 0);
    }

    public void addNotification(String title, String message, String timestamp, String isReadStr, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        insertNotification(db, title, message, timestamp, type, "read".equalsIgnoreCase(isReadStr) ? 1 : 0);
    }
}

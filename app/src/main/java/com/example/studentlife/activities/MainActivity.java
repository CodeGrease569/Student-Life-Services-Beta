package com.example.studentlife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.studentlife.adapters.NotificationAdapter;
import com.example.studentlife.adapters.PendingActionAdapter;
import com.example.studentlife.adapters.RecentRequestAdapter;
import com.example.studentlife.adapters.RequestHistoryAdapter;
import com.example.studentlife.adapters.RequirementAdapter;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.NotificationItem;
import com.example.studentlife.models.Requirement;
import com.example.studentlife.models.Scholarship;
import com.example.studentlife.models.StudentRequest;
import com.example.studentlife.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private BottomNavigationView bottomNav;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    // Top Bar Views
    private TextView tvTopBadgeCount, tvTopAvatarInitials;

    // Dashboard Views
    private TextView tvStudentGreeting, tvStudentDetails, tvDashScholarshipName, tvDashScholarshipProgressLabel, tvPendingCountBadge;
    private ProgressBar pbDashScholarship;
    private RecyclerView rvPendingActions, rvRecentRequests;

    // Scholarship Views
    private TextView tvSchProgramName, tvSchAcademicYear, tvSchProgressStats, tvSchStatusBadge;
    private ProgressBar pbSchProgress;
    private RecyclerView rvScholarshipRequirements;
    private TextView tabSchOverview, tabSchApply, tabSchRequirements;

    // Requests Views
    private TextView tvStatTotalCount, tvStatPendingCount, tvStatCompletedCount;
    private EditText etSearchRequests;
    private RecyclerView rvRequestHistory;
    private RequestHistoryAdapter requestHistoryAdapter;
    private String currentStatusFilter = "All";
    private TextView[] filterChips;

    // Alerts Views
    private TextView tvMarkAllRead, tabNotifAll, tabNotifUnread;
    private RecyclerView rvNotifications;
    private NotificationAdapter notificationAdapter;
    private boolean showingOnlyUnread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        viewFlipper = findViewById(R.id.viewFlipper);
        bottomNav = findViewById(R.id.bottomNav);

        initTopBar();
        initDashboard();
        initServices();
        initScholarship();
        initRequests();
        initAlerts();

        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAllData();
    }

    private void initTopBar() {
        tvTopBadgeCount = findViewById(R.id.tvTopBadgeCount);
        tvTopAvatarInitials = findViewById(R.id.tvTopAvatarInitials);

        String name = sessionManager.getStudentName();
        tvTopAvatarInitials.setText(getInitials(name));

        findViewById(R.id.btnTopNotification).setOnClickListener(v -> switchTab(4));
        findViewById(R.id.btnTopProfile).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "MS";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                viewFlipper.setDisplayedChild(0);
                return true;
            } else if (itemId == R.id.nav_services) {
                viewFlipper.setDisplayedChild(1);
                return true;
            } else if (itemId == R.id.nav_scholarship) {
                viewFlipper.setDisplayedChild(2);
                return true;
            } else if (itemId == R.id.nav_requests) {
                viewFlipper.setDisplayedChild(3);
                return true;
            } else if (itemId == R.id.nav_alerts) {
                viewFlipper.setDisplayedChild(4);
                return true;
            }
            return false;
        });
    }

    public void switchTab(int index) {
        viewFlipper.setDisplayedChild(index);
        if (index == 0) bottomNav.setSelectedItemId(R.id.nav_dashboard);
        else if (index == 1) bottomNav.setSelectedItemId(R.id.nav_services);
        else if (index == 2) bottomNav.setSelectedItemId(R.id.nav_scholarship);
        else if (index == 3) bottomNav.setSelectedItemId(R.id.nav_requests);
        else if (index == 4) bottomNav.setSelectedItemId(R.id.nav_alerts);
    }

    // ================= DASHBOARD =================
    private void initDashboard() {
        tvStudentGreeting = findViewById(R.id.tvStudentGreeting);
        tvStudentDetails = findViewById(R.id.tvStudentDetails);
        tvDashScholarshipName = findViewById(R.id.tvDashScholarshipName);
        tvDashScholarshipProgressLabel = findViewById(R.id.tvDashScholarshipProgressLabel);
        pbDashScholarship = findViewById(R.id.pbDashScholarship);
        tvPendingCountBadge = findViewById(R.id.tvPendingCountBadge);

        rvPendingActions = findViewById(R.id.rvPendingActions);
        rvPendingActions.setLayoutManager(new LinearLayoutManager(this));

        rvRecentRequests = findViewById(R.id.rvRecentRequests);
        rvRecentRequests.setLayoutManager(new LinearLayoutManager(this));

        // Quick action clicks
        findViewById(R.id.actionQuickScholarship).setOnClickListener(v -> switchTab(2));
        findViewById(R.id.actionQuickDocuments).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DocumentRequestActivity.class);
            intent.putExtra("default_doc_type", "Good Moral Certificate");
            startActivity(intent);
        });
        findViewById(R.id.actionQuickConcerns).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, StudentConcernActivity.class)));
        findViewById(R.id.actionQuickLostId).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DocumentRequestActivity.class);
            intent.putExtra("default_doc_type", "Lost ID Assistance");
            startActivity(intent);
        });

        findViewById(R.id.tvDashViewScholarship).setOnClickListener(v -> switchTab(2));
        findViewById(R.id.cardDashScholarship).setOnClickListener(v -> switchTab(2));
        findViewById(R.id.tvViewAllRequests).setOnClickListener(v -> switchTab(3));
    }

    // ================= SERVICES =================
    private void initServices() {
        findViewById(R.id.cardServiceApplyScholarship).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScholarshipApplyActivity.class));
        });

        findViewById(R.id.cardServiceSubmitRequirements).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SubmitRequirementActivity.class);
            intent.putExtra("requirement_id", 1);
            startActivity(intent);
        });

        findViewById(R.id.cardServiceGoodMoral).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DocumentRequestActivity.class);
            intent.putExtra("default_doc_type", "Good Moral Certificate");
            startActivity(intent);
        });

        findViewById(R.id.cardServiceCoc).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DocumentRequestActivity.class);
            intent.putExtra("default_doc_type", "Certificate of Completion");
            startActivity(intent);
        });

        findViewById(R.id.cardServiceLostId).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DocumentRequestActivity.class);
            intent.putExtra("default_doc_type", "Lost ID Assistance");
            startActivity(intent);
        });

        findViewById(R.id.cardServiceStudentConcerns).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, StudentConcernActivity.class));
        });

        findViewById(R.id.cardServiceInquiries).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, InquiryActivity.class));
        });
    }

    // ================= SCHOLARSHIP =================
    private void initScholarship() {
        tvSchProgramName = findViewById(R.id.tvSchProgramName);
        tvSchAcademicYear = findViewById(R.id.tvSchAcademicYear);
        tvSchProgressStats = findViewById(R.id.tvSchProgressStats);
        tvSchStatusBadge = findViewById(R.id.tvSchStatusBadge);
        pbSchProgress = findViewById(R.id.pbSchProgress);

        tabSchOverview = findViewById(R.id.tabScholarshipOverview);
        tabSchApply = findViewById(R.id.tabScholarshipApply);
        tabSchRequirements = findViewById(R.id.tabScholarshipRequirements);

        tabSchOverview.setOnClickListener(v -> {
            setScholarshipTabActive(tabSchOverview);
        });

        tabSchApply.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScholarshipApplyActivity.class));
        });

        tabSchRequirements.setOnClickListener(v -> {
            setScholarshipTabActive(tabSchRequirements);
            // Scroll to requirements
        });

        findViewById(R.id.btnSchApplyRenewal).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScholarshipApplyActivity.class));
        });

        findViewById(R.id.btnSchUploadDoc).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SubmitRequirementActivity.class);
            intent.putExtra("requirement_id", 1);
            startActivity(intent);
        });

        rvScholarshipRequirements = findViewById(R.id.rvScholarshipRequirements);
        rvScholarshipRequirements.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setScholarshipTabActive(TextView activeTab) {
        tabSchOverview.setBackgroundResource(R.drawable.bg_tab_unselected);
        tabSchOverview.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        tabSchApply.setBackgroundResource(R.drawable.bg_tab_unselected);
        tabSchApply.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        tabSchRequirements.setBackgroundResource(R.drawable.bg_tab_unselected);
        tabSchRequirements.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));

        activeTab.setBackgroundResource(R.drawable.bg_tab_selected);
        activeTab.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    // ================= REQUESTS =================
    private void initRequests() {
        tvStatTotalCount = findViewById(R.id.tvStatTotalCount);
        tvStatPendingCount = findViewById(R.id.tvStatPendingCount);
        tvStatCompletedCount = findViewById(R.id.tvStatCompletedCount);
        etSearchRequests = findViewById(R.id.etSearchRequests);

        rvRequestHistory = findViewById(R.id.rvRequestHistory);
        rvRequestHistory.setLayoutManager(new LinearLayoutManager(this));

        setupStatusFilterChips();

        etSearchRequests.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (requestHistoryAdapter != null) {
                    requestHistoryAdapter.filter(s.toString(), currentStatusFilter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupStatusFilterChips() {
        filterChips = new TextView[]{
                findViewById(R.id.chipFilterAll),
                findViewById(R.id.chipFilterProcessing),
                findViewById(R.id.chipFilterUnderReview),
                findViewById(R.id.chipFilterApproved),
                findViewById(R.id.chipFilterCompleted),
                findViewById(R.id.chipFilterRejected)
        };

        String[] filterNames = {"All", "Processing", "Under Review", "Approved", "Completed", "Rejected"};

        for (int i = 0; i < filterChips.length; i++) {
            final int index = i;
            final String filterName = filterNames[i];
            filterChips[i].setOnClickListener(v -> {
                currentStatusFilter = filterName;
                for (int j = 0; j < filterChips.length; j++) {
                    if (j == index) {
                        filterChips[j].setBackgroundResource(R.drawable.bg_tab_selected);
                        filterChips[j].setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    } else {
                        filterChips[j].setBackgroundResource(R.drawable.bg_pill_sand);
                        filterChips[j].setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_secondary));
                    }
                }
                if (requestHistoryAdapter != null) {
                    requestHistoryAdapter.filter(etSearchRequests.getText().toString(), currentStatusFilter);
                }
            });
        }
    }

    // ================= ALERTS =================
    private void initAlerts() {
        tvMarkAllRead = findViewById(R.id.tvMarkAllRead);
        tabNotifAll = findViewById(R.id.tabNotifAll);
        tabNotifUnread = findViewById(R.id.tabNotifUnread);
        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));

        tvMarkAllRead.setOnClickListener(v -> {
            dbHelper.markAllNotificationsAsRead();
            refreshNotificationsList();
            updateNotificationBadge();
        });

        tabNotifAll.setOnClickListener(v -> {
            showingOnlyUnread = false;
            tabNotifAll.setBackgroundResource(R.drawable.bg_tab_selected);
            tabNotifAll.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            tabNotifUnread.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabNotifUnread.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_secondary));
            refreshNotificationsList();
        });

        tabNotifUnread.setOnClickListener(v -> {
            showingOnlyUnread = true;
            tabNotifUnread.setBackgroundResource(R.drawable.bg_tab_selected);
            tabNotifUnread.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            tabNotifAll.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabNotifAll.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_secondary));
            refreshNotificationsList();
        });
    }

    private void refreshAllData() {
        // Update user greetings
        String fullName = sessionManager.getStudentName();
        String studentId = sessionManager.getStudentId();
        String course = sessionManager.getStudentCourse();
        String year = sessionManager.getStudentYear();

        tvStudentGreeting.setText(fullName);
        tvStudentDetails.setText(studentId + "  ·  " + course + "  ·  " + year);
        tvTopAvatarInitials.setText(getInitials(fullName));

        // Scholarship Info
        Scholarship scholarship = dbHelper.getActiveScholarship();
        if (scholarship != null) {
            tvDashScholarshipName.setText(scholarship.getName());
            tvDashScholarshipProgressLabel.setText("Progress: " + scholarship.getProgressPercent() + "% (" + scholarship.getCompletedRequirements() + "/" + scholarship.getTotalRequirements() + " Requirements)");
            pbDashScholarship.setProgress(scholarship.getProgressPercent());

            tvSchProgramName.setText(scholarship.getName());
            tvSchAcademicYear.setText("Academic Year " + scholarship.getAcademicYear() + "  ·  1st Semester");
            tvSchProgressStats.setText("Progress: " + scholarship.getProgressPercent() + "% (" + scholarship.getCompletedRequirements() + " of " + scholarship.getTotalRequirements() + " requirements submitted)");
            pbSchProgress.setProgress(scholarship.getProgressPercent());
        }

        // Pending Actions
        List<Requirement> pendingList = dbHelper.getPendingRequirements();
        tvPendingCountBadge.setText(pendingList.size() + " action items");
        PendingActionAdapter pendingAdapter = new PendingActionAdapter(this, pendingList);
        rvPendingActions.setAdapter(pendingAdapter);

        // Recent Requests (limit 2 on dash)
        List<StudentRequest> recentList = dbHelper.getRecentRequests(2);
        RecentRequestAdapter recentAdapter = new RecentRequestAdapter(this, recentList);
        rvRecentRequests.setAdapter(recentAdapter);

        // Requirements List (Scholarship tab)
        List<Requirement> allReqs = dbHelper.getAllRequirements();
        RequirementAdapter reqAdapter = new RequirementAdapter(this, allReqs);
        rvScholarshipRequirements.setAdapter(reqAdapter);

        // Request History Tab
        tvStatTotalCount.setText(String.valueOf(dbHelper.getTotalRequestsCount()));
        tvStatPendingCount.setText(String.valueOf(dbHelper.getPendingRequestsCount()));
        tvStatCompletedCount.setText(String.valueOf(dbHelper.getCompletedRequestsCount()));

        List<StudentRequest> allRequests = dbHelper.getAllRequests();
        if (requestHistoryAdapter == null) {
            requestHistoryAdapter = new RequestHistoryAdapter(this, allRequests);
            rvRequestHistory.setAdapter(requestHistoryAdapter);
        } else {
            requestHistoryAdapter.updateData(allRequests);
        }
        if (etSearchRequests != null) {
            requestHistoryAdapter.filter(etSearchRequests.getText().toString(), currentStatusFilter);
        }

        // Notifications
        refreshNotificationsList();
        updateNotificationBadge();
    }

    private void refreshNotificationsList() {
        List<NotificationItem> list = dbHelper.getAllNotifications(showingOnlyUnread);
        if (notificationAdapter == null) {
            notificationAdapter = new NotificationAdapter(this, list, dbHelper);
            rvNotifications.setAdapter(notificationAdapter);
        } else {
            notificationAdapter.updateData(list);
        }
    }

    private void updateNotificationBadge() {
        int unreadCount = dbHelper.getUnreadNotificationsCount();
        if (unreadCount > 0) {
            tvTopBadgeCount.setVisibility(View.VISIBLE);
            tvTopBadgeCount.setText(String.valueOf(unreadCount));
            // Also badge on bottom nav
            bottomNav.getOrCreateBadge(R.id.nav_alerts).setNumber(unreadCount);
        } else {
            tvTopBadgeCount.setVisibility(View.GONE);
            bottomNav.removeBadge(R.id.nav_alerts);
        }
    }
}

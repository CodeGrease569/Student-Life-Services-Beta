package com.example.studentlife.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.StudentRequest;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DocumentRequestActivity extends AppCompatActivity {

    private Spinner spDocType, spPurpose, spCopies;
    private EditText etDetails;
    private TextView tvHeaderTitle, tvUploadFileName;
    private DatabaseHelper dbHelper;
    private String uploadedFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_request);

        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.cardDocReqBack).setOnClickListener(v -> finish());

        tvHeaderTitle = findViewById(R.id.tvDocReqHeaderTitle);
        spDocType = findViewById(R.id.spDocType);
        spPurpose = findViewById(R.id.spDocPurpose);
        spCopies = findViewById(R.id.spDocCopies);
        etDetails = findViewById(R.id.etDocDetails);
        tvUploadFileName = findViewById(R.id.tvDocUploadFileName);
        MaterialButton btnSubmit = findViewById(R.id.btnDocSubmit);

        // Populate doc types
        String[] docTypes = {
                "Good Moral Certificate",
                "Certificate of Completion",
                "Lost ID Assistance",
                "Endorsement Letter",
                "Dean's List Certificate"
        };
        ArrayAdapter<String> docTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, docTypes);
        spDocType.setAdapter(docTypeAdapter);

        // Pre-select if intent had extra
        String defaultDoc = getIntent().getStringExtra("default_doc_type");
        if (defaultDoc != null) {
            tvHeaderTitle.setText(defaultDoc);
            for (int i = 0; i < docTypes.length; i++) {
                if (docTypes[i].equalsIgnoreCase(defaultDoc)) {
                    spDocType.setSelection(i);
                    break;
                }
            }
        }

        // Purposes
        String[] purposes = {
                "Scholarship / Financial Grant Requirement",
                "Employment / Job Application",
                "Transfer to Another Institution",
                "Licensure Examination / Board Prep",
                "Internship / OJT Endorsement",
                "Passport / Travel Visa Application",
                "Other Student Academic Requirement"
        };
        ArrayAdapter<String> purposeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, purposes);
        spPurpose.setAdapter(purposeAdapter);

        // Copies
        String[] copies = {"1 Copy", "2 Copies", "3 Copies", "5 Copies"};
        ArrayAdapter<String> copiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, copies);
        spCopies.setAdapter(copiesAdapter);

        findViewById(R.id.layoutDocUploadBox).setOnClickListener(v -> {
            uploadedFileName = "Student_ID_and_Receipt_Scan.pdf (1.2 MB)";
            tvUploadFileName.setText("✓ " + uploadedFileName);
            Toast.makeText(this, "Attachment added!", Toast.LENGTH_SHORT).show();
        });

        btnSubmit.setOnClickListener(v -> submitRequest());
    }

    private void submitRequest() {
        String docType = spDocType.getSelectedItem().toString();
        String purpose = spPurpose.getSelectedItem().toString();
        String copies = spCopies.getSelectedItem().toString();
        String details = etDetails.getText().toString().trim();

        String refNumber = "SL-2026-" + String.format(Locale.US, "%06d", (int) (Math.random() * 900000 + 100000));
        String currentDate = new SimpleDateFormat("MMM d, yyyy", Locale.US).format(new Date());

        String description = "Purpose: " + purpose + "\nQuantity: " + copies + "\nDetails: " + (details.isEmpty() ? "Standard issuance" : details);

        StudentRequest request = new StudentRequest(
                refNumber,
                "Documents",
                docType,
                currentDate,
                currentDate,
                "Processing",
                description,
                uploadedFileName.isEmpty() ? "id_attachment.pdf" : uploadedFileName,
                "Your request has been received and forwarded to the Document Records Officer."
        );

        dbHelper.addStudentRequest(request);
        dbHelper.addNotification(
                "Document Request Received",
                "Your request for " + docType + " (" + refNumber + ") is now being processed by Student Life.",
                "Just now",
                "unread",
                "document"
        );

        new AlertDialog.Builder(this)
                .setTitle("Request Submitted!")
                .setMessage("Your request for '" + docType + "' has been submitted.\n\nReference: " + refNumber + "\nStatus: Processing\nYou will receive a notification once the document is ready for release.")
                .setPositiveButton("Done", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
}

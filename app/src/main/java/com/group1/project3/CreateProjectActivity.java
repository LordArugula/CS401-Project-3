package com.group1.project3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.gson.Gson;
import com.group1.project3.model.Project;
import com.group1.project3.util.FirebaseUtil;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class CreateProjectActivity extends AppCompatActivity {

    private EditText text_projectName;
    private View preview_colorPicker;
    private Button button_submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        text_projectName = findViewById(R.id.create_project_text_project_name);
        preview_colorPicker = findViewById(R.id.create_project_background_color_picker_preview);
        button_submit = findViewById(R.id.create_project_submit_button);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean error = validateProjectName();

                button_submit.setEnabled(error);
            }

            private boolean validateProjectName() {
                boolean error = false;
                String projectName = getProjectName();
                if (projectName.isEmpty()) {
                    error = true;
                }
                return !error;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        };
        text_projectName.addTextChangedListener(tw);
    }

    private Project createProject() {
        FirebaseUser currentUser = FirebaseUtil.getAuth().getCurrentUser();

        DocumentReference projectDoc = FirebaseUtil.getFirestore()
                .collection("projects")
                .document();

        String projectName = getProjectName();
        Project project = new Project(projectDoc.getId(), projectName, currentUser.getUid());
        project.setColor(getColor());

        projectDoc.set(project.serializeAsMap());

        return project;
    }
//
//    private void sendInviteEmail(FirebaseUser user, Project project, String[] emails) {
//        FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse("https://ggez.page.link"))
//                .setDomainUriPrefix("https://ggez.page.link")
//                .buildShortDynamicLink()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                        emailIntent.setType("message/rfc822");
//                        emailIntent.putExtra(Intent.EXTRA_EMAIL, emails);
//                        String subject = MessageFormat.format("{0} invited you to join {1}", user.getDisplayName(), project.getName());
//                        String message = task.getResult().getShortLink().toString();
//
//                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//                        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
//                        startActivity(Intent.createChooser(emailIntent, "Send Email"));
//                    }
//                });
//    }

    @NonNull
    private String getProjectName() {
        return text_projectName.getText().toString().trim();
    }

    private Color getColor() {
        return Color.valueOf(((ColorDrawable) preview_colorPicker.getBackground()).getColor());
    }

    void launchProjectMenuActivity(Project project) {
        Intent intent = new Intent(this, ProjectMenuActivity.class);
//        if (project != null) {
//            String projectAsJson = new Gson().toJson(project.serializeAsMap());
//            intent.putExtra("project", projectAsJson);
//        }
        startActivity(intent);
    }

    public void cancelCreateProject(View view) {
        launchProjectMenuActivity(null);
    }

    public void onCreateProject(View view) {
        launchProjectMenuActivity(createProject());
    }

    public void openColorPicker(View view) {
        new ColorPickerDialog.Builder(this)
                .setTitle("Project Color")
                .setPositiveButton("Select", (ColorEnvelopeListener) (envelope, fromUser) -> {
                    preview_colorPicker.setBackgroundColor(envelope.getColor());
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .attachAlphaSlideBar(false)
                .attachBrightnessSlideBar(true)
                .show();
    }
}
package com.example.realmnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;

public class UpdateCourse extends AppCompatActivity {

    EditText cname,cdur,ctrack,cdesc;


    private String courseName, courseDuration, courseDescription, courseTracks;
    private long id;
    private Button upbtn,dltbtn;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);

        cname = findViewById(R.id.cname);
        cdur = findViewById(R.id.cdur);
        cdesc =findViewById(R.id.cdesc);
        ctrack = findViewById(R.id.ctrack);
        upbtn =findViewById(R.id.upbtn);
        dltbtn = findViewById(R.id.dlbtn);
        realm = Realm.getDefaultInstance();


        courseName = getIntent().getStringExtra("courseName");
        courseDuration = getIntent().getStringExtra("courseDuration");
        courseDescription = getIntent().getStringExtra("courseDescription");
        courseTracks = getIntent().getStringExtra("courseTracks");
        id = getIntent().getLongExtra("id", 0);

        // on below line we are setting data in our edit text fields.
        cname.setText(courseName);
        cdur.setText(courseDuration);
        cdesc.setText(courseDescription);
        ctrack.setText(courseTracks);


        dltbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse(id);
                startActivity(new Intent(UpdateCourse.this,ShowData.class));
            }
        });


        upbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String courseName = cname.getText().toString();
                String courseDescription = cdesc.getText().toString();
                String courseDuration = cdur.getText().toString();
                String courseTracks = ctrack.getText().toString();
                if (TextUtils.isEmpty(courseName)){
                    cname.setError("Invalid text");
                } else if (TextUtils.isEmpty(courseDescription)) {
                    cdesc.setError("Invalid text");
                } else if (TextUtils.isEmpty(courseDuration)) {
                    cdur.setError("InvalidText");
                } else if (TextUtils.isEmpty(courseTracks)) {
                    ctrack.setError("Invalid text");

                }else {
                    final  DataModel model = realm.where(DataModel.class).equalTo("id",id).findFirst();
                    updateCourse(model,courseName,courseDuration,courseTracks,courseDescription);
                }
                startActivity(new Intent(UpdateCourse.this,ShowData.class));

            }
        });







    }

    private void updateCourse(DataModel model, String courseName, String courseDuration, String courseTracks, String courseDescription) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.setSname(courseName);
                model.setSdesc(courseDescription);
                model.setSdur(courseDuration);
                model.setStrk(courseTracks);

                realm.copyToRealmOrUpdate(model);
            }
        });
    }

    private void deleteCourse(long id) {
        DataModel model = realm.where(DataModel.class).equalTo("id",id).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteFromRealm();
            }
        });
    }
}
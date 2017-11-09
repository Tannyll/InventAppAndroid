package com.emirci.inventapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    EditText userName, userContact;
    Button saveBtn;

    DatabaseReference mDataRef;

    String keyUser;

    String userNameString, userContactString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        keyUser = getIntent().getStringExtra("USER_KEY");


        mDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(keyUser);

        userName = (EditText) findViewById(R.id.userNameEditText);
        userContact = (EditText) findViewById(R.id.userPhnoEditText);
        saveBtn = (Button) findViewById(R.id.userProfileBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userNameString = userName.getText().toString();
                userContactString = userContact.getText().toString();


                Log.d("adf", userContactString + " : " + userNameString);

                if (!TextUtils.isEmpty(userNameString) && !TextUtils.isEmpty(userContactString)) {
                    mDataRef.child("userName").setValue(userNameString);
                    mDataRef.child("userContact").setValue(userContactString);
                    mDataRef.child("isVerified").setValue("verfied");

                    Toast.makeText(ProfileActivity.this, "USer profilke added", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ProfileActivity.this, WelcomeActivity.class));

                }
            }
        });
    }
}

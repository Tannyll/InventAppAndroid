package com.emirci.inventapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.emirci.inventapp.model.InventoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;

//https://www.youtube.com/watch?v=mv-NslL31sk

public class MainActivity extends AppCompatActivity {


    Button createUser, moveToLogin, inventoryBtn;
    EditText userEmailEdit, userPasswordEdit;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mDatabaseRef, mUserCheckData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        inventoryBtn = (Button) findViewById(R.id.btnInventoryList);

        createUser = (Button) findViewById(R.id.createUserbtn);
        moveToLogin = (Button) findViewById(R.id.moveToLoginBtn);
        userEmailEdit = (EditText) findViewById(R.id.userEmailEditText);
        userPasswordEdit = (EditText) findViewById(R.id.passEditTextCreate);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUserCheckData = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    final String emailForEver = user.getEmail();

                    mUserCheckData.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            checkUserValidation(dataSnapshot, emailForEver);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                } else {

                }
            }
        };

        inventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InventoryesActivity.class));
            }
        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userEmailString, userPassString;

                if (userEmailEdit == null) {
                    Toast.makeText(MainActivity.this, "Eposta ve şifre yazmalısınız.", Toast.LENGTH_LONG).show();
                    return;
                }

                userEmailString = userEmailEdit.getText().toString().trim();
                userPassString = userPasswordEdit.getText().toString().trim();

                if (!TextUtils.isEmpty(userEmailString) && !TextUtils.isEmpty(userPassString)) {

                    mAuth.createUserWithEmailAndPassword(userEmailString, userPassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                DatabaseReference mChildDatabase = mDatabaseRef.child("Users").push();

                                String key_user = mChildDatabase.getKey();

                                mChildDatabase.child("isVerified").setValue("unverified");
                                mChildDatabase.child("userKey").setValue(key_user);
                                mChildDatabase.child("emailUser").setValue(userEmailString);
                                mChildDatabase.child("passWordUser").setValue(userPassString);

                                Toast.makeText(MainActivity.this, "User Account Created", Toast.LENGTH_LONG).show();


                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                            } else {
                                Toast.makeText(MainActivity.this, "Failed to create User Account", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }


            }
        });

        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkUserValidation(DataSnapshot dataSnapshot, String emailForVer) {


        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext()) {

            DataSnapshot dataUser = (DataSnapshot) iterator.next();

            if (dataUser.child("emailUser").getValue().toString().equals(emailForVer)) {

                if (dataUser.child("isVerified").getValue().toString().equals("unverified")) {

                    Intent in = new Intent(MainActivity.this, LoginActivity.class);
                    in.putExtra("USER_KEY", dataUser.child("userKey").getValue().toString());
                    startActivity(in);

                } else {

                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));

                }

            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}

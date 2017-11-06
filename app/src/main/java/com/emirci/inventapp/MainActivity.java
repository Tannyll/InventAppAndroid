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

public class MainActivity extends AppCompatActivity {


    Button createUser, moveToLogin;
    EditText userEmailEdit, userPasswordEdit;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    DatabaseReference mDatabaseref,mUserCheckData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        createUser = (Button) findViewById(R.id.createUserbtn);
        moveToLogin = (Button) findViewById(R.id.moveToLoginBtn);

        userEmailEdit = (EditText) findViewById(R.id.userEmailEditText);
        userPasswordEdit = (EditText) findViewById(R.id.passEditTextCreate);

        mDatabaseref = FirebaseDatabase.getInstance().getReference();
        mUserCheckData = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    final  String emailForEver = user.getEmail();
                    mUserCheckData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            checkUserValidation(dataSnapshot,emailForEver);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    startActivity(new Intent(MainActivity.this, Welcome.class));


                } else {

                }
            }
        };


        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userEmailString, userPassString;

                userEmailString = userEmailEdit.getText().toString().trim();
                userPassString = userPasswordEdit.getText().toString().trim();

                if (!TextUtils.isEmpty(userEmailString) && !TextUtils.isEmpty(userPassString)) {
                    firebaseAuth.createUserWithEmailAndPassword(userEmailString, userPassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                DatabaseReference mChildDatabase = mDatabaseref.child("Users").push();

                                String key_user = mChildDatabase.getKey();

                                mChildDatabase.child("isVerified").setValue("unverified");
                                mChildDatabase.child("userKey").setValue(key_user);
                                mChildDatabase.child("emailUser").setValue(userEmailString);
                                mChildDatabase.child("passWordUser").setValue(userPassString);

                                Toast.makeText(MainActivity.this, "User Account Cteated", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, Welcome.class));
                            } else {
                                Toast.makeText(MainActivity.this, "failed user Account not Crated", Toast.LENGTH_SHORT).show();
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

        while (iterator.hasNext())
        {

            DataSnapshot dataUser = (DataSnapshot) iterator.next();

            if( dataUser.child("emailUser").getValue().toString().equals(emailForVer))
            {

                if (dataUser.child("isVerified").getValue().toString().equals("unverified"))
                {

                    Intent in = new Intent( MainActivity.this, LoginActivity.class);
                    in.putExtra("USER_KEY" , dataUser.child("userKey").getValue().toString());
                    startActivity(in);

                }else
                {

                    startActivity(new Intent(MainActivity.this, Welcome.class));

                }

            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}

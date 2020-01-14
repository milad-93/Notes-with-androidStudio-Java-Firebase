package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examapplikation.Models.NotesList;
import com.example.examapplikation.Models.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteInputActivity extends AppCompatActivity {

    private EditText titleInput, textInput;
    private Button btn_saveNote;
    private TextView SignedInUser,timeandDate;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_input);
        this.setTitle("-Create note-");

        viewSetUp();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =  firebaseDatabase.getReference("NoteList"); // put note
        loggedInUser = firebaseDatabase.getReference(firebaseAuth.getUid());// read user

        btn_saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNote(); // calls function
            }
        });

        CurrentLoggedInUser();
        getCurrentTimeAndDate();
    }






//#Region displayLoggedInUser  //https://www.youtube.com/watch?v=_ZbM6b5SEw0&t=95s
    public void CurrentLoggedInUser(){
        loggedInUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Userprofile does not refer to this class of this activity but to the Constructor made in the class of Userprofile)
                // object of the class userprofile
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                SignedInUser.setText("Signed in as: " +userProfile.getUserEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { // if database error
                Toast.makeText(NoteInputActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });

    }
    //#EndRegion

    //#Region create note
  private void  CreateNote(){ // send to dataBase
      MediaPlayer buttonSound = MediaPlayer.create(getApplicationContext(),R.raw.buttonsoumd);
      buttonSound.start(); //butt
        final String title = titleInput.getText().toString();
        String text = textInput.getText().toString();
        String time = timeandDate.getText().toString();

        if(!TextUtils.isEmpty(title)&& !TextUtils.isEmpty(text)){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference(firebaseAuth.getUid());
            NotesList NewNoteToDataBase = new NotesList(title,text,time); // object of class Todolist in models

            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).push().setValue(NewNoteToDataBase).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(NoteInputActivity.this, "note added", Toast.LENGTH_SHORT).show();
                    titleInput.setText("");
                    textInput.setText("");
                    timeandDate.setText("");
                    finish();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(NoteInputActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        }
  }
    //#endRegion

    private void getCurrentTimeAndDate(){
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String dateTime= simpleDateFormat.format(calender.getTime());
        timeandDate.setText(dateTime);

    }

    //#Region setting views
  private void viewSetUp(){
      titleInput = findViewById(R.id.EditText_note_descripton);
      textInput= findViewById(R.id.EditText_note_text);
      btn_saveNote = findViewById(R.id.btn_add_note);
      SignedInUser = findViewById(R.id.TextView_current_user);
      timeandDate = findViewById(R.id.TextView_time_date);
  }
  //#Endregion

    //#Region Menu
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(NoteInputActivity.this,MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //create menu on toolbar
        getMenuInflater().inflate(R.menu.menu,menu); //inflated inside
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // handle on click events on items on menu
        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
            }
            case R.id.HomeMenu:{
                startActivity(new Intent(NoteInputActivity.this,HomeActivity.class));
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    //#EndRegion

}




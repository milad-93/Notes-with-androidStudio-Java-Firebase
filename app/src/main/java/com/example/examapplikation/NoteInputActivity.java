package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.examapplikation.Models.NotesList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class NoteInputActivity extends AppCompatActivity {

    private EditText titleInput, textInput;
    private Button btn_saveNote;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_input);
        SetUpUiViews();

        // instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


       databaseReference =  firebaseDatabase.getReference("NoteList");



        btn_saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToDataBase(); // calls function
            }
        });

    }


  private void  SendDataToDataBase(){ // send to dataBase
        final String title = titleInput.getText().toString();
        String text = textInput.getText().toString();

        if(!TextUtils.isEmpty(title)&& !TextUtils.isEmpty(text)){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference(firebaseAuth.getUid());


            NotesList NewNoteToDataBase = new NotesList(title,text); // object of class Todolist in models

            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).push().setValue(NewNoteToDataBase).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(NoteInputActivity.this, "note added", Toast.LENGTH_SHORT).show();
                    titleInput.setText("");
                    textInput.setText("");
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



  private void SetUpUiViews(){
      titleInput = findViewById(R.id.etNoteDescripton);
      textInput= findViewById(R.id.etNoteText);
      btn_saveNote = findViewById(R.id.btn_add_note);

  }

}




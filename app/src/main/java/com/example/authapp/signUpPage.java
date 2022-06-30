package com.example.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signUpPage extends AppCompatActivity implements View.OnClickListener {

     private FirebaseAuth mAuth;

     private TextView txtVHead;
     private EditText txtEName,txtEAge,txtEEmail,txtEPassword;
     private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        mAuth = FirebaseAuth.getInstance();

        txtVHead = (TextView)findViewById(R.id.txtVHead);
        txtVHead.setOnClickListener(this);

        txtEPassword = (EditText)findViewById(R.id.txtEPassword);
        txtEName = (EditText)findViewById(R.id.txtEName);
        txtEAge = (EditText)findViewById(R.id.txtEAge);
        txtEEmail=  (EditText)findViewById(R.id.txtEEmail);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.txtVHead:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.btnSignUp:
                SignUpUser();
                break;

        }
    }

    private void SignUpUser() {

        String email = txtEEmail.getText().toString().trim();
        String password = txtEPassword.getText().toString().trim();
        String age = txtEAge.getText().toString().trim();
        String name = txtEName.getText().toString().trim();
        if(name.isEmpty()){
            txtEName.setError("Name is required");
            txtEName.requestFocus();
            return;
        }

        for(int i=0;i<age.length();i++) {
            if (age.charAt(i) > '9') {
                txtEAge.setError("Enter a Valid Age");
                txtEAge.requestFocus();
                return;
            }
        }
        if(age.isEmpty()){
            txtEAge.setError("Age is required");
            txtEAge.requestFocus();
            return;
        }
        if(email.isEmpty()){
            txtEEmail.setError("email is required");
            txtEEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEEmail.setError("Enter a valid email Address");
            txtEEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            txtEPassword.setError("Set a password");
            txtEPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            txtEPassword.setError("Minimum length should be 6");
            txtEPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name,age,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(signUpPage.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(signUpPage.this, "failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(signUpPage.this, "failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }
}
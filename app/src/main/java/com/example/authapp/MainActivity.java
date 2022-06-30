package com.example.authapp;

import static android.util.Patterns.EMAIL_ADDRESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txtVSignUp,txtVForgotPassword;
    private EditText txtEMail,txtEPassword;
    private Button  btnLogin;
    private ProgressBar progressbar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        txtEPassword = (EditText) findViewById(R.id.txtEPassword);
        txtEMail = (EditText)findViewById(R.id.txtEEmail);
        progressbar = (ProgressBar)findViewById(R.id.progressbar);

        txtVForgotPassword = (TextView)findViewById(R.id.txtVForgotPassword);
        txtVForgotPassword.setOnClickListener(this);

        txtVSignUp = (TextView)findViewById(R.id.txtVSignUp);
        txtVSignUp.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.txtVSignUp:
                startActivity(new Intent(this,signUpPage.class));
                break;

            case R.id.btnLogin:
                userLogin();
                break;
            case R.id.txtVForgotPassword:
                startActivity(new Intent(this,forgot_password.class));
                break;
        }
    }

    private void userLogin() {

        String email = txtEMail.getText().toString().trim();
        String password = txtEPassword.getText().toString().trim();

        if(email.isEmpty()){
            txtEMail.setError("Enter a valid Email!");
            txtEMail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEMail.setError("Enter a valid Email!");
            txtEMail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            txtEPassword.setError("password can't be empty");
            txtEMail.requestFocus();
            return;
        }
//        if(password.length()<6){
//            txtEPassword.setError("password ");
//            txtEMail.requestFocus();
//            return;
//        }

        progressbar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Please,verify your email",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                }
                progressbar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
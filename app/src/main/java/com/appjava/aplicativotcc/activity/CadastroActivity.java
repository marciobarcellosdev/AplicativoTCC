package com.appjava.aplicativotcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.appjava.aplicativotcc.R;
import com.appjava.aplicativotcc.helper.HelperFirebase;
import com.appjava.aplicativotcc.model.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextSignupUser, editTextSignupEmail, editTextSignupPassword;
    private Button buttonSignup;
    private ProgressBar progressBarSignup;
    FirebaseAuth authentication;
    ModelUser modelUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        methodInitializeSignupComponents();
        progressBarSignup.setVisibility(View.INVISIBLE);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodValidateSignupFields(v);
            }
        });
    }
    public void methodInitializeSignupComponents(){
        editTextSignupUser = findViewById(R.id.editTextSignupUser);
        editTextSignupEmail = findViewById(R.id.editTextSignupEmail);
        editTextSignupPassword = findViewById(R.id.editTextSignupPassword);
        buttonSignup = findViewById(R.id.buttonSignup);
        progressBarSignup = findViewById(R.id.progressBarSignup);
    }
    public void methodValidateSignupFields(View view){

        String storageUser = editTextSignupUser.getText().toString();
        String storageEmail = editTextSignupEmail.getText().toString();
        String storagePassword = editTextSignupPassword.getText().toString();

        boolean emptyUser = false;
        boolean emptyEmail = false;
        boolean emptyPassword = false;

        if(storageUser.isEmpty()) emptyUser = true;
        if(storageEmail.isEmpty()) emptyEmail = true;
        if(storagePassword.isEmpty()) emptyPassword = true;

        if(emptyUser || emptyEmail || emptyPassword){
            Toast.makeText( CadastroActivity.this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
        }else{
            modelUser = new ModelUser();
            modelUser.setUser(storageUser);
            modelUser.setEmail(storageEmail);
            modelUser.setPassword(storagePassword);
            methodSignupUser(modelUser);
        }
    }
    public void methodSignupUser(ModelUser parameterModelUser){

        progressBarSignup.setVisibility(View.VISIBLE);
        authentication = HelperFirebase.getFirebaseAuthentication();
        authentication.createUserWithEmailAndPassword(parameterModelUser.getEmail(), parameterModelUser.getPassword())

        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    progressBarSignup.setVisibility(View.INVISIBLE);
                    Toast.makeText( CadastroActivity.this, getString(R.string.user_registered), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }else{
                    progressBarSignup.setVisibility(View.INVISIBLE);
                    String exception;
                    try
                    {
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch( FirebaseAuthWeakPasswordException e)
                    {
                        exception = getString(R.string.need_stronger_password);
                    }catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        exception = getString(R.string.invalid_email);
                    }catch (FirebaseAuthUserCollisionException e)
                    {
                        exception = getString(R.string.user_already_registered);
                    }catch (Exception e)
                    {
                        exception = getString(R.string.signup_error) + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText( CadastroActivity.this, exception, Toast.LENGTH_SHORT).show();
                } // if else
            }; // onComplete
        }); // addOnCompleteListener
    }; // methodSignupUser
}
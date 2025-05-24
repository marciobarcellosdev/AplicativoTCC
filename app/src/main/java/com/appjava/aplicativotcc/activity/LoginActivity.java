package com.appjava.aplicativotcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPassword;
    private Button buttonLogin;
    private ProgressBar progressBarLogin;
    FirebaseAuth authentication;
    ModelUser modelUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        methodInitializeLoginComponents();
        progressBarLogin.setVisibility(View.INVISIBLE);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodValidateLoginFields(v);
            }
        });
    }
    public void methodInitializeLoginComponents(){
        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBarLogin = findViewById(R.id.progressBarLogin);
    }

    public void methodValidateLoginFields(View view){

        String storageEmail = editTextLoginEmail.getText().toString();
        String storagePassword = editTextLoginPassword.getText().toString();

        boolean emptyEmail = false;
        boolean emptyPassword = false;

        if(storageEmail.isEmpty()) emptyEmail = true;
        if(storagePassword.isEmpty()) emptyPassword = true;

        if(emptyEmail || emptyPassword){
            Toast.makeText( LoginActivity.this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
        }else{
            modelUser = new ModelUser();
            modelUser.setEmail(storageEmail);
            modelUser.setPassword(storagePassword);
            methodValidateLogin(modelUser);
        }
    }

    public void methodValidateLogin(ModelUser modelUser){
        progressBarLogin.setVisibility(View.VISIBLE);
        authentication = HelperFirebase.getFirebaseAuthentication();
        authentication.signInWithEmailAndPassword(modelUser.getEmail(), modelUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            progressBarLogin.setVisibility(View.INVISIBLE);
                            Toast.makeText( LoginActivity.this, getString(R.string.user_logged), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        }else{
                            progressBarLogin.setVisibility(View.INVISIBLE);
                            Toast.makeText( LoginActivity.this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                        } // if else
                    }; // onComplete
                }); // addOnCompleteListener

    }
    public void openScreenSignup(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
}
package com.example.trabajocm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginRegisterActivity extends AppCompatActivity {

    Button btn_login;
    EditText emailreg, emaillog, passwordreg, passwordlog;
    Button btn_register;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        emailreg = findViewById(R.id.correoReg);
        emaillog = findViewById(R.id.correoLog);

        passwordreg = findViewById(R.id.contraseñaReg);
        passwordlog = findViewById(R.id.contraseñaLog);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_registrar);



        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String emaillogUser = emaillog.getText().toString().trim();
                String passlogUser = passwordlog.getText().toString().trim();

                if (emaillogUser.isEmpty() && passlogUser.isEmpty()){
                    Toast.makeText( LoginRegisterActivity.this, "Ingresar los datos", Toast.LENGTH_SHORT).show();

                }else{
                    loginUser(emaillogUser, passlogUser);
                }

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String emailregUser, passregUser;
                emailregUser = String.valueOf(emailreg.getText());
                passregUser = String.valueOf(passwordreg.getText());

                if (TextUtils.isEmpty(emailregUser)) {
                    Toast.makeText(LoginRegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passregUser)) {
                    Toast.makeText(LoginRegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(emailregUser, passregUser)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    showSuccessDialog();
                                    emailreg.setText("");
                                    passwordreg.setText("");
                                } else {
                                    Toast.makeText(LoginRegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        });

    }

    private void loginUser(String emaillogUser, String passlogUser) {
        mAuth.signInWithEmailAndPassword(emaillogUser, passlogUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(LoginRegisterActivity.this, MainActivity.class));
                    Toast.makeText(LoginRegisterActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginRegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginRegisterActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginRegisterActivity.this);
        builder.setTitle("Registro Exitoso");
        builder.setMessage("El usuario se ha creado correctamente.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cerrar el diálogo
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
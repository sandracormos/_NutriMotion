package com.example.nutrimotion.security;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nutrimotion.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ImageView btnBack = findViewById(R.id.btn_back);

        EditText inputEmail = findViewById(R.id.inputEmail);


        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        AppCompatButton btnSendEmail = findViewById(R.id.btn_send_email);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                if (!TextUtils.isEmpty(email) || !email.contains("@")) {
                    resetPassword(email);
                } else {
                    inputEmail.setError("Email must be valid");
                }
            }
        });

    }

    private void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> {
            Toast.makeText(ForgotPasswordActivity.this, "Reset link was sent", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
        })
        .addOnFailureListener(e -> Toast.makeText(ForgotPasswordActivity.this, "Error :- " +e.getMessage(), Toast.LENGTH_LONG).show());
    }

}

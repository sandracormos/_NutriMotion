package com.example.nutrimotion.security;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nutrimotion.BMICalculator;
import com.example.nutrimotion.MainActivity;
import com.example.nutrimotion.Navigation_Menu;
import com.example.nutrimotion.R;
import com.example.nutrimotion.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth firebaseAuth;
    private TextView forgotPassword;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    ImageView googleButton;
    Button register_now_button;
    Button loginButton;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        loginButton = findViewById(R.id.btn_login);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        googleButton = findViewById(R.id.btn_google_login);

        ImageView arrowButton = findViewById(R.id.btn_back);
        arrowButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, WelcomeScreen.class);
            startActivity(intent);
            finish();
        });

        forgotPassword = findViewById(R.id.textForgotPassword);
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String password, email;
            email = String.valueOf(inputEmail.getText());
            password = String.valueOf(inputPassword.getText());

            // Check if either email or password is empty
            if (email.isEmpty() || password.isEmpty()) {
                // Set EditText with the issue in focus and change font color to red
                if (email.isEmpty()) {
                    inputEmail.requestFocus();
                    inputEmail.setTextColor(Color.RED);
                } else {
                    inputEmail.setTextColor(Color.WHITE);
                }

                if (password.isEmpty()) {
                    inputPassword.requestFocus();
                    inputPassword.setTextColor(Color.RED);
                } else {
                    inputPassword.setTextColor(Color.WHITE);
                }

                // Show a message or handle the case where fields are empty
                Toast.makeText(LoginActivity.this, "Email or password cannot be empty.", Toast.LENGTH_SHORT).show();
            } else {
                // Reset text color to default
                inputEmail.setTextColor(Color.WHITE);
                inputPassword.setTextColor(Color.WHITE);

                // Proceed with authentication
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                User.email = email;
                                Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Navigation_Menu.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        googleButton.setOnClickListener(v -> signInViaGoogle());
    }

    private void signInViaGoogle() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToMainActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went terribly wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void navigateToMainActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
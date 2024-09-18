package com.example.nutrimotion.security;

import static android.content.ContentValues.TAG;

import static com.example.nutrimotion.security.InputValidator.isEmailValid;
import static com.example.nutrimotion.security.InputValidator.isNameValid;
import static com.example.nutrimotion.security.InputValidator.isPasswordValid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.content.Intent;
import android.os.Build;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;


import com.example.nutrimotion.R;
import com.example.nutrimotion.User;
import com.example.nutrimotion.security.InputValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputFirstName, inputLastName, inputConfirmPassword;
    private AppCompatButton registerButton;
    FirebaseAuth mAuth;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();

        // Find the TextView by its ID
        TextView textView = findViewById(R.id.titleText);
        String text = "Welcome to NutriMotion!";
        SpannableString spannableString = new SpannableString(text);
        // Set the first two words (0-10) to white color
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set the next two words (11-21) to a different color
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#6FFFE9")), 11, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Apply the modified text to the TextView
        textView.setText(spannableString);
        ImageView arrowButton = findViewById(R.id.btn_back);
        arrowButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, WelcomeScreen.class);
            startActivity(intent);
            finish();
        });

        registerButton = findViewById(R.id.registerButton);
        registerButton.setBackgroundColor(Color.parseColor("#6FFFE9"));
        String text2 = "Register";
        String formattedText = text2.substring(0, 1).toUpperCase() + text2.substring(1).toLowerCase();
        registerButton.setText(formattedText);

        inputEmail = findViewById(R.id.inputEmail);
        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDataAndDoRegister();
            }
        });
    }

    private void validateDataAndDoRegister() {
        String firstName = inputFirstName.getText().toString().trim();
        String lastName = inputLastName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();

        boolean isValid = true; // Flag to track overall validation status

        // Validate Email
        if (email.isEmpty()) {
            inputEmail.setError("Enter Email Address");
            inputEmail.requestFocus();
            isValid = false;
        } else if (email.length() <= 10) {
            inputEmail.setError("Enter a valid email address");
            inputEmail.requestFocus();
            isValid = false;
        } else if (!isEmailValid(email)) {
            inputEmail.setError("Enter a valid email address");
            inputEmail.requestFocus();
            isValid = false;
        }

        // Validate First Name
        if (firstName.isEmpty()) {
            inputFirstName.setError("Enter First Name");
            inputFirstName.requestFocus();
            isValid = false;
        } else if (!isNameValid(firstName)) {
            inputFirstName.setError("Your name should not contain special characters or numbers");
            inputFirstName.requestFocus();
            isValid = false;
        }

        // Validate Last Name
        if (lastName.isEmpty()) {
            inputLastName.setError("Enter Last Name");
            inputLastName.requestFocus();
            isValid = false;
        } else if (!isNameValid(lastName)) {
            inputLastName.setError("Your name should not contain special characters or numbers");
            inputLastName.requestFocus();
            isValid = false;
        }

        // Validate Password
        if (password.isEmpty()) {
            inputPassword.setError("Enter Password");
            inputPassword.requestFocus();
            isValid = false;
        } else if (password.length() <= 8) {
            inputPassword.setError("Password should be more than 8 characters");
            inputPassword.requestFocus();
            isValid = false;
        } else if (!isPasswordValid(password)) {
            inputPassword.setError("Password should contain uppercase, lowercase, and at least one number");
            inputPassword.requestFocus();
            isValid = false;
        }

        // Validate Confirm Password
        if (!confirmPassword.equals(password)) {
            inputConfirmPassword.setError("Passwords do not match");
            inputConfirmPassword.requestFocus();
            isValid = false;
        }

        // If all validation passes, proceed with registration
        if (isValid) {
            doRegister(firstName, lastName, password, email);
        }
    }

    private void doRegister(String firstName, String lastName, String password, String email) {
        // disable the button while process and enable when process complete or failed
        registerButton.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String hashedPassword = User.hashPassword(password);

                    Map<String, Object> user = new HashMap<>();
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    user.put("password", hashedPassword);
                    user.put("email", email);

                    db.collection("users").add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(RegisterActivity.this, "Successful save", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Failed save", Toast.LENGTH_LONG).show();
                                }
                            });

                    sendVerificationEmail();
                    // Redirect to another activity
                    //De dat redirect la sign in care sa dea mai departe la BMICalculator
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthUserCollisionException){
                    inputEmail.setError("Email already registered");
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Ooops! Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Email verification sent to your email address", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to send email verification", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

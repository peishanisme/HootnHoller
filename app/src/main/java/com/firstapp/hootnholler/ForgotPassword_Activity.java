package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPassword_Activity extends AppCompatActivity {
    private Button buttonPwdReset;
    private ImageView back_button;

    private EditText editTextPwdResetEmail;
    private FirebaseAuth authProfile;
    private final static String TAG = "ForgotPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Get references to UI elements
        back_button=(ImageView)findViewById(R.id.back_button);
        editTextPwdResetEmail = findViewById(R.id.forgotPassword_email);
        buttonPwdReset = findViewById(R.id.ResetPassword);

        //set click listener for the back button
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ForgotPassword_Activity.this, Login_Activity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the reset password button
        buttonPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextPwdResetEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    editTextPwdResetEmail.setError("Email is required");
                    editTextPwdResetEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextPwdResetEmail.setError("Valid email is required");
                    editTextPwdResetEmail.requestFocus();
                } else {
                    resetPassword(email);
                }
            }
        });
    }

    // Method to reset the user's password
    private void resetPassword(String email) {
        authProfile = FirebaseAuth.getInstance();

        // Check if the email is registered
        authProfile.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    if (result.getSignInMethods().isEmpty()) {
                        // Email is not registered
                        Toast.makeText(ForgotPassword_Activity.this, "Email is not registered. Please register first.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email is registered, proceed with password reset
                        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Password reset email sent successfully
                                    Toast.makeText(ForgotPassword_Activity.this, "Please check your email for the password reset link", Toast.LENGTH_SHORT).show();

                                    // Redirect to the starting activity
                                    Intent intent = new Intent(ForgotPassword_Activity.this, Login_Activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Handle other exceptions
                                    Log.e(TAG, task.getException().getMessage());
                                    Toast.makeText(ForgotPassword_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    // Handle other exceptions
                    Log.e(TAG, task.getException().getMessage());
                    Toast.makeText(ForgotPassword_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
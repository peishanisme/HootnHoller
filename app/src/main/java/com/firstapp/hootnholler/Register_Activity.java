package com.firstapp.hootnholler;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.hootnholler.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText UserFullname,UserEmail,UserPassword,UserConfirmPassword;
    private ProgressDialog loadingBar;
    private Button CreateAccountButton;

    private User user;
    private RadioGroup UserRole;
    private RadioButton RoleSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth= FirebaseAuth.getInstance();

        UserFullname=(EditText) findViewById(R.id.register_fullname);
        UserEmail=(EditText) findViewById(R.id.register_email);
        UserPassword=(EditText) findViewById(R.id.register_password);
        UserConfirmPassword=(EditText) findViewById(R.id.register_cofirmpassword);
        UserRole=(RadioGroup)findViewById(R.id.roleselection);
        CreateAccountButton=(Button) findViewById(R.id.register_create_account);

        loadingBar=new ProgressDialog(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //method of creating new account
                CreateNewAccount();
            }
        });

    }
    public void CreateNewAccount(){

        //get text from user input
        String fullname=UserFullname.getText().toString();
        String email=UserEmail.getText().toString();
        String password=UserPassword.getText().toString();
        String confirmPassword=UserConfirmPassword.getText().toString();
        RoleSelection= (RadioButton) findViewById(UserRole.getCheckedRadioButtonId());
        String role=RoleSelection.getText().toString();

        //prompt user to key in the information if the column is still empty
        if(TextUtils.isEmpty(fullname)){
            Toast.makeText(Register_Activity.this,"Please insert your fullname...",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please insert your email...",Toast.LENGTH_SHORT).show();


        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please insert your password...",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this,"Please insert your password...",Toast.LENGTH_SHORT).show();

        }else if(!(password.equals(confirmPassword))) {
            Toast.makeText(this, "Your password do not match with your confirm password...", Toast.LENGTH_SHORT).show();

        }else if (!RoleSelection.isChecked()) {
                Toast.makeText(this, "Please select the your role...", Toast.LENGTH_SHORT).show();

        }else{

            //loading bar message
            loadingBar.setTitle("Creating New Account...");
            loadingBar.setMessage("Please wait, we are creating your new account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            //use email and password to create
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // Tell the user whether they were successful in creating an account or not
                    if (task.isSuccessful()) {
                        Toast.makeText(Register_Activity.this, "You are authenticated successfully...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                        // Create a new User object with the provided information
                        user = new User(FirebaseAuth.getInstance().getUid(), fullname, email, role,"","","");

                        // Store the user object in the "Users" node of the Firebase Realtime Database
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(user);

                        System.out.println(role);
                        if(role.equalsIgnoreCase("student")) {
                            // Start the Setup_Activity
                            Intent mainIntent = new Intent(Register_Activity.this, StudentSetup_Activity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else if (role.equalsIgnoreCase("parent")) {
                            Intent mainIntent = new Intent(Register_Activity.this, ParentSetup_Activity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();

                        }else if (role.equalsIgnoreCase("educator")) {
                            Intent mainIntent = new Intent(Register_Activity.this, EducatorSetup_Activity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();}
                    } else {
                        // Display the error message to the user
                        String message = task.getException().getMessage();
                        Toast.makeText(Register_Activity.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

            });
        }

    }

}
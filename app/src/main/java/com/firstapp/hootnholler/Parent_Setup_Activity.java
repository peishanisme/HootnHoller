package com.firstapp.hootnholler;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firstapp.hootnholler.entity.Parent;
import com.firstapp.hootnholler.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Parent_Setup_Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText birthday, phonenumber;
    private RadioGroup gender;
    private RadioButton genderSelection;
    private ImageView back_button;
    private Button SubmitButton,AddKey;
    LinearLayout layoutList1;
    ArrayList<String> ConnectionKey = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ProgressDialog loadingBar;
    String currentUserID;
    private User user;

    private Parent parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_setup);

        // Initialize Firebase authentication, retrieve the current user ID, and get the database reference
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Initialize UI elements and set up event listeners
        back_button=(ImageView)findViewById(R.id.back_button);
        birthday = (EditText) findViewById(R.id.parent_birthday);
        phonenumber = (EditText) findViewById(R.id.parent_phonenumber);
        gender = (RadioGroup) findViewById(R.id.parent_gender);
        SubmitButton = (Button) findViewById(R.id.SubmitButton);

        //dynamic view for addHobby
        layoutList1 = findViewById(R.id.key_layout);
        AddKey = findViewById(R.id.addKey);
        AddKey.setOnClickListener(this);


        loadingBar = new ProgressDialog(this);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Handle the click event on the SaveInfoButton
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            // Method to save the user account setup information
            private void SaveAccountSetupInformation() {

                // Retrieve input values from UI elements
                String Birthday = birthday.getText().toString();
                String Phonenumber = phonenumber.getText().toString();
                genderSelection = (RadioButton) findViewById(gender.getCheckedRadioButtonId());
                String genderParent;

                // Check if any required field is empty, display a toast if true
                if (TextUtils.isEmpty(Birthday) || TextUtils.isEmpty(Phonenumber) ||
                        gender==null) {
                    Toast.makeText(Parent_Setup_Activity.this, "Please insert your information...", Toast.LENGTH_SHORT).show();

                    // Validate the birthday format
                }else if (!isValidBirthdayFormat(Birthday)) {
                    Toast.makeText(Parent_Setup_Activity.this, "Invalid birthday format. Please use DD/MM/YYYY.", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if the format is invalid


                } else {
                    loadingBar.setTitle("Setting up account...");
                    loadingBar.setMessage("Please wait, we are saving your account information...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    //Retrieve input from addHobby dynamic view and add into ArrayList. Pass the array list into database for storing
                    ConnectionKey.clear();
                    for (int i = 1; i < layoutList1.getChildCount(); i++) {
                        EditText ETKey = (EditText) layoutList1.getChildAt(i).findViewById(R.id.newInput);
                        if (ETKey != null && ETKey.getText() != null) {
                            ConnectionKey.add(ETKey.getText().toString());
                        }

                    }

                    // Store the ConnectionKey in the "Parent" collection under the current user ID
                    if (!ConnectionKey.isEmpty()) {
                        DatabaseReference ConnectionKeyReference = FirebaseDatabase.getInstance().getReference().child("Parent");
                        ConnectionKeyReference.child(currentUserID).child("ConnectionKey").setValue(ConnectionKey);
                    }

                    // Update the user object with the entered information
                    // Update the user data in the database and handle the completion
                    genderParent=genderSelection.getText().toString();
                    user.setBirthday(Birthday);
                    user.setPhone_number(Phonenumber);
                    user.setGender(genderParent);
                    reference.setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Parent_Setup_Activity.this, "Saved", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        SendUserToMainActivity();
                                    } else {
                                        Toast.makeText(Parent_Setup_Activity.this, "Something error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    @Override
    //methods to control dynamic view
    public void onClick(View v) {
        if (v.getId() == R.id.addKey) {
            addKeyView();

        }
    }


    private void addKeyView() {
        final View keyView = getLayoutInflater().inflate(R.layout.dynamic_view, null, false);
        final EditText keyEdit = (EditText) keyView.findViewById(R.id.newInput);
        ImageView removeKey = (ImageView) keyView.findViewById(R.id.remove_button);

        removeKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeKeyView(keyView);
            }
        });

        layoutList1.addView(keyView);

        // Only add the key to the list if the user has entered some text
        String key = keyEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(key)) {
            ConnectionKey.add(key);
        }
    }


    public void removeKeyView(View v) {
        layoutList1.removeView(v);
    }

    //after setting up the account, users will be sent to main activity
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(Parent_Setup_Activity.this, Starting_Activity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    //Method to validate the birthday format (dd/MM/yyyy)
    public static boolean isValidBirthdayFormat(String birthday) {
        String regex = "\\d{2}/\\d{2}/\\d{4}";
        return birthday.matches(regex);
    }
}
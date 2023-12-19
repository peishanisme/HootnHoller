package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.firstapp.hootnholler.entity.Student;
import com.firstapp.hootnholler.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Random;
import java.util.ArrayList;

public class StudentSetup_Activity extends AppCompatActivity {

    private EditText birthday, phonenumber, school,student_class;
    private String Level;
    private RadioGroup gender;
    private RadioButton genderSelection;
    private ImageView back_button;
    private Button SubmitButton;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ProgressDialog loadingBar;
    String currentUserID;
    private User user;

    private Student student;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_setup);

        // Initialize Firebase authentication, retrieve the current user ID, and get the database reference
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    user=snapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //spinner
        Spinner level=findViewById(R.id.student_level);
        level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Level= adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String>schoolLevel=new ArrayList<>();
        schoolLevel.add("Standard 1");
        schoolLevel.add("Standard 2");
        schoolLevel.add("Standard 3");
        schoolLevel.add("Standard 4");
        schoolLevel.add("Standard 5");
        schoolLevel.add("Standard 6");

        ArrayAdapter<String>adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,schoolLevel);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        level.setAdapter(adapter);

        // Initialize UI elements and set up event listeners
        back_button=(ImageView)findViewById(R.id.back_button);
        birthday = (EditText) findViewById(R.id.student_birthday);
        phonenumber = (EditText) findViewById(R.id.student_phonenumber);
        school = (EditText) findViewById(R.id.student_school);
        student_class=(EditText)findViewById(R.id.student_class);
        gender = (RadioGroup) findViewById(R.id.student_gender);
        SubmitButton = (Button) findViewById(R.id.SubmitButton);
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
                String Phonenumber=phonenumber.getText().toString();
                String School = school.getText().toString();
                genderSelection = (RadioButton) findViewById(gender.getCheckedRadioButtonId());
                String Class=student_class.getText().toString();
                String ConnectionKey=generateRandomCode();
                String genderStudent;




                // Check if any required field is empty, display a toast if true
                if (TextUtils.isEmpty(Birthday) || TextUtils.isEmpty(Phonenumber) || TextUtils.isEmpty(School) || TextUtils.isEmpty(Class) ||
                        gender==null||TextUtils.isEmpty(Level) ) {
                    Toast.makeText(StudentSetup_Activity.this, "Please insert your information...", Toast.LENGTH_SHORT).show();
                } // Validate the birthday format
                else if (!isValidBirthdayFormat(Birthday)) {
                    Toast.makeText(StudentSetup_Activity.this, "Invalid birthday format. Please use DD/MM/YYYY.", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if the format is invalid
                }else {
                    loadingBar.setTitle("Setting up account...");
                    loadingBar.setMessage("Please wait, we are saving your account information...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    student = new Student(School, Level,Class,ConnectionKey);

                    // Store the user object in the "Users" node of the Firebase Realtime Database
                    FirebaseDatabase.getInstance().getReference().child("Student").child(FirebaseAuth.getInstance().getUid()).setValue(student);
                    // Update the user object with the entered information
                    // Update the user data in the database and handle the completion
                    genderStudent=genderSelection.getText().toString();
                    user.setBirthday(Birthday);
                    user.setPhone_number(Phonenumber);
                    user.setGender(genderStudent);
                    reference.setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(StudentSetup_Activity.this, "Saved", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        SendUserToMainActivity();
                                    } else {
                                        Toast.makeText(StudentSetup_Activity.this, "Something error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    //after setting up the account, users will be sent to main activity
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(StudentSetup_Activity.this, Main_Activity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    //Method to validate the birthday format (dd/MM/yyyy)
    public static boolean isValidBirthdayFormat(String birthday) {
        String regex = "\\d{2}/\\d{2}/\\d{4}";
        return birthday.matches(regex);
    }
    //method to generate random code
    public static String generateRandomCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    }

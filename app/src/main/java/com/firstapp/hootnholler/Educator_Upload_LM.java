package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firstapp.hootnholler.databinding.ActivityTeacherUploadLmBinding;
import com.firstapp.hootnholler.entity.Learning_Materials;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;

public class Educator_Upload_LM extends AppCompatActivity {
    private String currentClassCode,timestamp;
    Button uploadButton;
    ImageView addFileButton,back_button;
    EditText LMTitle,LMDescription,LMFileName;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ActivityTeacherUploadLmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherUploadLmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentClassCode = getIntent().getStringExtra("classCode");

        uploadButton=findViewById(R.id.uploadButton);
        addFileButton=findViewById(R.id.addFile);
        LMTitle=findViewById(R.id.LMTitle);
        LMDescription=findViewById(R.id.LMDescription);
        LMFileName=findViewById(R.id.LMFileName);
        back_button=binding.back;

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode).child("Learning Materials");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Educator_Upload_LM.this, Educator_LearningMaterials.class);
                startActivity(intent);
            }
        });

        uploadButton.setEnabled(false);
        addFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });


    }

    private void selectPDF() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF Files"),101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            Uri uri=data.getData();
            
            String uriString=uri.toString();
            File myFile=new File(uriString);
            String path=myFile.getAbsolutePath();
            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (displayNameIndex != -1) {
                            displayName = cursor.getString(displayNameIndex);
                        } else {

                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }
            
            uploadButton.setEnabled(true);
            LMFileName.setText(displayName);
            
            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(LMTitle.getText().toString())) {
                        Toast.makeText(Educator_Upload_LM.this, "Please enter learning materials title", Toast.LENGTH_SHORT).show();
                        return;

                    }else {
                        uploadPDF(data.getData());
                    }
                }
            });
        }
    }

    private void uploadPDF(Uri data) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("File Uploading...");
        pd.show();

        final String timestamp = String.valueOf(System.currentTimeMillis());
        final StorageReference reference = storageReference.child("uploads/" + timestamp + ".pdf");
        // store in upload folder of the Firebase storage
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        Learning_Materials learningMaterials = new Learning_Materials(
                                timestamp,
                                LMTitle.getText().toString(),
                                LMDescription.getText().toString(),
                                LMFileName.getText().toString(),
                                uri.toString()
                        );

                        databaseReference.child(databaseReference.push().getKey()).setValue(learningMaterials);// push the value into the realtime database
                        Toast.makeText(Educator_Upload_LM.this, "File Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        finish();


                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100 * snapshot.getBytesTransferred())/ snapshot.getTotalByteCount();
                        pd.setMessage("Uploaded : "+ (int) percent + "%");
                    }
                });

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
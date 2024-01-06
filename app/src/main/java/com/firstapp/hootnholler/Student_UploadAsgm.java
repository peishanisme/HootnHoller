package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.firstapp.hootnholler.databinding.ActivityStudentUploadAsgmBinding;

public class Student_UploadAsgm extends AppCompatActivity {

    public ImageView backButton;
    //int requestCode = 1;
    //public Button btnChooseFile;
    //public TextView txtResult;
    ActivityStudentUploadAsgmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentUploadAsgmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backButton = binding.back;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Student_UploadAsgm.this, Student_AsgmDetails.class);
                startActivity(intent);
            }
        });
    }
}
        /*btnChooseFile = (Button) findViewById(R.id.btnChoose);
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("");
        //intent.addCategory(Intent.CATEGORY_OPENABLE);

        try{
            startActivityForResult(Intent.createChooser(intent, "Select a file"),100);
        }catch(Exception exception){
            Toast.makeText(this, "Please install a file manager", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data){
        if (requestCode == 100 && resultCode == RESULT_OK && data!= null){
            Uri uri = data.getData();
            String path = uri.getPath();
            File file = new File(path);

            txtResult.setText("Path: " + path + "\n" + "\n" + "File name: " + file.getName());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
    /*public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Context context = getApplicationContext();
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK){
            if (data == null){
                return;
            }
            Uri uri = data.getData();
            Toast.makeText(context,uri.getPath(),Toast.LENGTH_SHORT).show();
        }
    }

    public void openFileChooser(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("");
        /*startActivityForResult(intent,requestCode);
    }*/

package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quiztesting.Adapters.SetAdapter;
import com.example.quiztesting.Models.CategoryModel;
import com.example.quiztesting.Models.SetModel;
import com.example.quiztesting.databinding.ActivityQuizEducatorSetBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuizEducatorSetActivity extends AppCompatActivity implements RecyViewInterface {

    ActivityQuizEducatorSetBinding binding;
    FirebaseDatabase database;
    SetAdapter adapter;
    String keyCtg, imageUrl;
    ArrayList<SetModel> list;
    Dialog progressDialog, dialog;
    DatabaseReference referenceSets, referenceSetNum;
    CircleImageView image;
    EditText setName;
    Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizEducatorSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        keyCtg = intent.getStringExtra("key");
        imageUrl = intent.getStringExtra("categoryImage");

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_add_set_dialog);

        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
        }
        image = dialog.findViewById(R.id.ctgImage);
        Picasso.get().load(imageUrl).placeholder(R.drawable.loading).into(image);
        setName = dialog.findViewById(R.id.inputSetName);
        upload = dialog.findViewById(R.id.uploadSetBtn);

        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.item_upload_progress_dialog);
        if(progressDialog.getWindow()!=null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
        }



        list = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.recySet.setLayoutManager(layoutManager);

        adapter = new SetAdapter(this, this,list);
        binding.recySet.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        referenceSets = database.getReference().child("Categories").child(keyCtg).child("Sets");
        referenceSetNum = database.getReference().child("Categories").child(keyCtg).child("setNum");

        referenceSets.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    list.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SetModel model = dataSnapshot.getValue(SetModel.class);
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizEducatorSetActivity.this, "Fail to access.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setName.setText("");
                dialog.show();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = setName.getText().toString();
                boolean checkDuplicity = false;
                for(SetModel model : list) {
                    if(model.getSetName().equals(name)) {
                        checkDuplicity = true;
                        break;
                    }
                }

                if(name.isEmpty()) {
                    setName.setError("Please enter category name");
                } else if(checkDuplicity) {
                    setName.setText("");
                    setName.setError("Repeated category name");
                    Toast.makeText(QuizEducatorSetActivity.this, "Set '" + name + "' already exists", Toast.LENGTH_SHORT).show();
                } else {
                    uploadSet(name);
                }
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizEducatorSetActivity.this, QuizEducatorCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void uploadSet(String name) {
        progressDialog.show();
        String key = referenceSets.push().getKey();
        SetModel model = new SetModel(name, key);
        referenceSets.child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                list.add(model);
                referenceSetNum.setValue(list.size()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(QuizEducatorSetActivity.this, "A new set '" + name + "' is created.", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuizEducatorSetActivity.this, "Fail to upload set", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizEducatorSetActivity.this, "Fail to upload set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemLongClick(int position) {
        SetModel selectedSet = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete '" + selectedSet.getSetName() + "' ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.notifyItemRemoved(position);
                list.remove(position);
                deleteSetFromFirebase(selectedSet);
            }
        }).setNegativeButton("No", null);
        builder.show();
    }

    private void deleteSetFromFirebase(SetModel selectedSet) {
        referenceSets.child(selectedSet.getSetKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                list.remove(selectedSet);
                referenceSetNum.setValue(list.size()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(QuizEducatorSetActivity.this, "Set '" + selectedSet.getSetName() + "' is deleted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuizEducatorSetActivity.this, "Fail to delete set", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizEducatorSetActivity.this, "Fail to delete set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, QuizEducatorQuestionActivity.class);
        SetModel model = list.get(position);

        intent.putExtra("key", keyCtg);
        intent.putExtra("keySet", model.getSetKey());
        intent.putExtra("categoryImage", imageUrl);

        this.startActivity(intent);
    }
}
package com.example.quiztesting;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quiztesting.Adapters.CategoryAdapter;
import com.example.quiztesting.Models.CategoryModel;
import com.example.quiztesting.databinding.ActivityQuizEducatorCategoryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuizEducatorCategoryActivity extends AppCompatActivity implements RecyViewInterface {

    ActivityQuizEducatorCategoryBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ImageView addCategory;
    Button uploadCategory;
    Dialog dialog, progressDialog;
    CircleImageView categoryImage;
    EditText categoryName;
    View fetchImage;
    Uri imageUri;
    ArrayList<CategoryModel> list;
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuizEducatorCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        list = new ArrayList<>();
        addCategory = binding.addCategory;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_add_category_dialog);

        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
        }

        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.item_upload_progress_dialog);

        if(progressDialog.getWindow()!=null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
        }

        fetchImage = dialog.findViewById(R.id.fetchImage);
        categoryImage = dialog.findViewById(R.id.categoryImages);
        categoryName = dialog.findViewById(R.id.inputCategoryName);
        uploadCategory = dialog.findViewById(R.id.btnUpload);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.recyCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(this, list,this);
        binding.recyCategory.setAdapter(adapter);

        database.getReference().child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if(snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
                        list.add(model);
                        adapter.notifyItemInserted(list.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizEducatorCategoryActivity.this, "Category not exist", Toast.LENGTH_SHORT).show();
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearContent();
                dialog.show();
            }
        });

        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    imageUri = uri;  // Store the selected image URI in a variable
                    categoryImage.setImageURI(uri);  // Set the image to the ImageView
                }
            }
        });

        fetchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });

        uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = categoryName.getText().toString();

                boolean checkDuplicity = false;
                for(CategoryModel model : list) {
                    if(model.getCategoryName().equals(name)) {
                        checkDuplicity = true;
                        break;
                    }
                }
                if(name.isEmpty()) {
                    categoryName.setError("Please enter category name");
                } else if(imageUri == null) {
                    Toast.makeText(QuizEducatorCategoryActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
                } else if(checkDuplicity) {
                    categoryName.setText("");
                    categoryName.setError("Repeated category name");
                    Toast.makeText(QuizEducatorCategoryActivity.this, "Category '" + name + "' already exists", Toast.LENGTH_SHORT).show();
                } else {
                    uploadCategory();
                }
            }
        });

    }

    private void clearContent() {
        categoryImage.setImageResource(R.drawable.upload);
        categoryName.setText("");
        categoryName.clearFocus();
        categoryName.setError(null);
    }

    private void uploadCategory() {
        progressDialog.show();
        StorageReference storageReference = storage.getReference().child("Categories").child(new Date().getTime() + "");

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference databaseReference = database.getReference().child("Categories");
                        String key = databaseReference.push().getKey();

                        CategoryModel categoryModel = new CategoryModel();
                        categoryModel.setCategoryName(categoryName.getText().toString());
                        categoryModel.setCategoryImage(uri.toString());
                        categoryModel.setSetNum(0);
                        categoryModel.setCtgKey(key);

                        databaseReference.child(key).setValue(categoryModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                dialog.dismiss();
                                Toast.makeText(QuizEducatorCategoryActivity.this, "Data uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(QuizEducatorCategoryActivity.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });


    }

    @Override
    public void onItemLongClick(int position) {
        CategoryModel selectedCategory = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete Category '" + selectedCategory.getCategoryName()
                + "' together with " + selectedCategory.getSetNum() + " set(s)?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.remove(position);
                deleteCategoryFromFirebase(selectedCategory, position);
            }
        }).setNegativeButton("No", null);
        builder.show();
    }

    @Override
    public void onItemClick(int position) {
        CategoryModel model = list.get(position);
        Intent intent = new Intent(QuizEducatorCategoryActivity.this, QuizEducatorSetActivity.class);
        intent.putExtra("key", model.getCtgKey());
        intent.putExtra("categoryImage", model.getCategoryImage());

        startActivity(intent);
    }

    private void deleteCategoryFromFirebase(CategoryModel categoryModel, int position) {
        String key = categoryModel.getCtgKey();
        database.getReference().child("Categories").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(QuizEducatorCategoryActivity.this, "Category '" + categoryModel.getCategoryName() + "' is deleted", Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizEducatorCategoryActivity.this, "Fail to delete category", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.firstapp.hootnholler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firstapp.hootnholler.Models.CategoryModel;
import com.firstapp.hootnholler.adapter.CategoryAdapter;
import com.firstapp.hootnholler.databinding.FragmentEducatorQuizBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class Educator_Quiz_Fragment extends Fragment implements RecyViewInterface {

    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference referenceEducator;
    ImageView addCategory;
    Button uploadCategory;
    Dialog dialog, progressDialog;
    CircleImageView categoryImage;
    EditText categoryName;
    View fetchImage;
    Uri imageUri;
    ArrayList<String> quizCategory;
    ArrayList<CategoryModel> list;
    CategoryAdapter adapter;
    FirebaseAuth auth;
    FirebaseUser user;
    String uid;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private FragmentEducatorQuizBinding binding;

    public Educator_Quiz_Fragment() {
        // Required empty public constructor
    }

    public static Educator_Quiz_Fragment newInstance(String param1, String param2) {
        Educator_Quiz_Fragment fragment = new Educator_Quiz_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEducatorQuizBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        list = new ArrayList<>();
        quizCategory = new ArrayList<>();
        addCategory = binding.addCategory;

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            uid = user.getUid();
        } else {
            Toast.makeText(getContext(), "Error in identifying user", Toast.LENGTH_SHORT).show();
        }

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.item_add_category_dialog);

        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
        }

        progressDialog = new Dialog(getContext());
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

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.recyCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(getContext(), list,this);
        binding.recyCategory.setAdapter(adapter);

        referenceEducator = database.getReference().child("Educator").child(uid).child("quizCategory");
        referenceEducator.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded() && snapshot.exists()) {
                    list.clear();
                    quizCategory = (ArrayList<String>) snapshot.getValue();

                    if (quizCategory != null && !quizCategory.isEmpty()) {
                        database.getReference().child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (isAdded() && snapshot.exists()) {
                                    list.clear();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        CategoryModel model = dataSnapshot.getValue(CategoryModel.class);

                                        for (String ctgKey : quizCategory) {
                                            if (model != null && model.getCtgKey().equals(ctgKey)) {
                                                list.add(model);
                                            }
                                        }
                                    }
                                    if (isAdded()) {
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                if (isAdded()) {
                                    Toast.makeText(getContext(), "Category not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error in accessing educator's quiz category", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Please upload image", Toast.LENGTH_SHORT).show();
                } else if(checkDuplicity) {
                    categoryName.setText("");
                    categoryName.setError("Repeated category name");
                    Toast.makeText(getContext(), "Category '" + name + "' already exists", Toast.LENGTH_SHORT).show();
                } else {
                    uploadCategory();
                }
            }
        });

        return view;
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

                        quizCategory.add(key);
                        referenceEducator.setValue(quizCategory).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReference.child(key).setValue(categoryModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                        list.add(categoryModel);
                                        adapter.notifyItemInserted(list.size());
                                        Toast.makeText(getContext(), "Category created", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
        database.getReference().child("Categories").child(selectedCategory.getCtgKey()).child("postedSet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Unable to Delete " + selectedCategory.getCategoryName());
                    builder.setMessage("You cannot delete this category as some sets inside it have been shared with students. " +
                            "\n\nTo delete this category, you need to cancel the posting of these sets. " +
                            "\nYou can cancel the posting by long-clicking on the respective classroom.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete " + selectedCategory.getCategoryName()
                            + " together with " + selectedCategory.getSetNum() + " set(s)?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCategoryFromFirebase(selectedCategory, position);
                        }
                    }).setNegativeButton("No", null);
                    builder.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        CategoryModel model = list.get(position);
        Intent intent = new Intent(getContext(), Educator_Quiz_Set_Activity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("key", model.getCtgKey());
        intent.putExtra("categoryImage", model.getCategoryImage());

        startActivity(intent);
    }

    private void deleteCategoryFromFirebase(CategoryModel categoryModel, int position) {
        if (position >= 0 && position < list.size()) {
            String key = categoryModel.getCtgKey();

            quizCategory.remove(position);
            referenceEducator.setValue(quizCategory);

            database.getReference().child("Categories").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Category '" + categoryModel.getCategoryName() + "' is deleted", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Fail to delete category", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Log an error or handle the situation where the position is invalid
            Toast.makeText(getContext(), "Invalid position to delete", Toast.LENGTH_SHORT).show();
        }
    }

}

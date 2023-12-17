package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.quiztesting.Adapters.CategoryAdapter;
import com.example.quiztesting.Models.CategoryModel;
import com.example.quiztesting.databinding.ActivityQuizStudentCategoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizStudentCategoryActivity extends AppCompatActivity implements RecyViewInterface {

    ActivityQuizStudentCategoryBinding binding;
    FirebaseDatabase database;
    DatabaseReference referenceClassKey, referenceCtg;
    FirebaseAuth auth;
    FirebaseUser user;
    String uid;
    CategoryAdapter adapter;
    ArrayList<CategoryModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizStudentCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        /*auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            String uid = user.getUid();
        } else {
            Toast.makeText(this, "Error in identifying user", Toast.LENGTH_SHORT).show();
            finish();
        }*/

        uid = "ILVlGWiDRbQa9xgYRi5BT2sYEec2";
        list = new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.recyCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(this, list, this);
        binding.recyCategory.setAdapter(adapter);

        referenceClassKey = database.getReference().child("Student").child(uid).child("classroom");
        referenceCtg = database.getReference().child("Categories");

        referenceClassKey.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot classKeySnapshot : snapshot.getChildren()) {
                        referenceClassKey.child(classKeySnapshot.getKey()).child("category").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                if(snapshot1.exists()) {
                                    for(DataSnapshot ctgKeySnapshot : snapshot1.getChildren()) {
                                        CategoryModel model = ctgKeySnapshot.getValue(CategoryModel.class);
                                        referenceCtg.child(model.getCtgKey()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                if(snapshot2.exists()) {
                                                    model.setCategoryName(snapshot2.child("categoryName").getValue(String.class));
                                                    model.setCategoryImage(snapshot2.child("categoryImage").getValue(String.class));
                                                    list.add(model);
                                                    adapter.notifyItemInserted(list.size());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(QuizStudentCategoryActivity.this, "Error in retrieving category details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(QuizStudentCategoryActivity.this, "Error in retrieving category model", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizStudentCategoryActivity.this, "Error in retrieving class key", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onItemClick(int position) {
        CategoryModel model = list.get(position);
        Intent intent = new Intent(QuizStudentCategoryActivity.this, QuizStudentSetActivity.class);
        intent.putExtra("keyCtg", model.getCtgKey());
        intent.putExtra("uid", uid);
        intent.putExtra("keySetList", model.getSetKey());

        this.startActivity(intent);
    }
}
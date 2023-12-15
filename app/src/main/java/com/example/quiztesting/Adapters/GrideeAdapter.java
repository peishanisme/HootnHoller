package com.example.quiztesting.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quiztesting.Models.QuestionModel;
import com.example.quiztesting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GrideeAdapter extends BaseAdapter {

    private Activity context;
    public int sets = 0;
    private String keyCtg;
    private ArrayList<String> list;

    public GrideeAdapter(Activity context, int sets, String key, ArrayList<String> list) {
        this.context = context;
        this.sets = sets;
        this.keyCtg = key;
        this.list = list;
    }

    public void deleteSetAndUpdate(int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference setReference = database.getReference().child("Sets").child(keyCtg).child(list.get(position));
        setReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                list.remove(position);
                sets = list.size();
                database.getReference().child("Categories").child(keyCtg).child("setNum").setValue(sets)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Set " + (position + 1) + " is deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to delete set", Toast.LENGTH_SHORT).show();
                            }
                        });
                notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to delete set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if(convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
        }
        else {
            view = convertView;
        }

        ((TextView)view.findViewById(R.id.setName)).setText(String.valueOf(position+1));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<QuestionModel> models = new ArrayList<>();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference().child("Sets").child(keyCtg).child(list.get(position)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                models.add(dataSnapshot.getValue(QuestionModel.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Fail to access question details", Toast.LENGTH_SHORT).show();
                    }
                });

                /*Intent intent = new Intent(context, QuizDoQuiz.class);
                intent.putExtra("keyCtg", keyCtg);
                intent.putExtra("keySet", list.get(position));
                intent.putExtra("currSetNum", (position + 1));
                intent.putExtra("totalQuestion", list.size());
                intent.putExtra("questionModels", models);


                try {
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed to start QuizDoQuiz activity", Toast.LENGTH_SHORT).show();
                }*/

            }
        });


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        return view;
    }
}


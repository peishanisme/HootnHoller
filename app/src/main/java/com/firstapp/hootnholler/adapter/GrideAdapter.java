package com.firstapp.hootnholler.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firstapp.hootnholler.Educator_Quiz_Question_Activity;
import com.firstapp.hootnholler.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GrideAdapter extends BaseAdapter {

    private Activity context;
    public int sets = 0;
    private String keyCtg;
    private ArrayList<String> list;

    public GrideAdapter(Activity context, int sets, String key, ArrayList<String> list) {
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

    public void addSetAndUpdate() {
        sets = list.size() + 1; // Update sets count after adding item to list
        notifyDataSetChanged();
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
                Intent intent = new Intent(context, Educator_Quiz_Question_Activity.class);
                intent.putExtra("key", keyCtg);
                intent.putExtra("keySet", list.get(position));
                intent.putExtra("currSetNum", (position + 1));

                try {
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed to start QuizQuestion activity", Toast.LENGTH_SHORT).show();
                }

            }
        });


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete Set " + (position + 1) + " ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSetAndUpdate(position);
                    }
                }).setNegativeButton("No", null);
                builder.show();
                return true;
            }
        });

        return view;
    }
}

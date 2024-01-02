package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.entity.Classroom;
import com.firstapp.hootnholler.entity.Quiz;

import java.util.ArrayList;
import java.util.List;

public class Student_Classroom_List extends ArrayAdapter<Classroom> {
    public Student_Classroom_List(@NonNull Context context, int resource, @NonNull List<Classroom> classrooms) {
        super(context, resource, classrooms);
    }
}

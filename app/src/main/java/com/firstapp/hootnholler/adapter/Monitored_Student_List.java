package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.entity.Student;
import java.util.List;

public class Monitored_Student_List extends ArrayAdapter<Student> {
    public Monitored_Student_List(Context context, List<Student> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_list_spinner_item, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.Student_Profile);
        TextView text = convertView.findViewById(R.id.Monitored_Student_Name);

        Student item = getItem(position);

        if (item != null) {
            text.setText(item.getUserName());
        }

        return convertView;
    }
}

package com.firstapp.hootnholler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firstapp.hootnholler.adapter.ContactItemAdapter;
import com.firstapp.hootnholler.models.ContactItem;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment {

    private LinearLayout educatorLayout;
    private LinearLayout parentLayout;
    private LinearLayout studentLayout;

    private TextView educatorTitle;
    private TextView parentTitle;
    private TextView studentTitle;

    // Constants to identify different view types
    private static final int EDUCATOR_TYPE = 1;
    private static final int STUDENT_TYPE = 2;
    private static final int PARENT_TYPE = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        // Initialize layouts
        educatorLayout = view.findViewById(R.id.EducatorLayout);
        parentLayout = view.findViewById(R.id.ParentLayout);
        studentLayout = view.findViewById(R.id.StudentLayout);

        // Initialize titles
        educatorTitle = view.findViewById(R.id.educatorTitle);
        parentTitle = view.findViewById(R.id.parentTitle);
        studentTitle = view.findViewById(R.id.studentTitle);

        // Initialize RecyclerView and adapter
        RecyclerView educatorRecyclerView = view.findViewById(R.id.EducatorRecyclerView);
        ContactItemAdapter adapter = new ContactItemAdapter(requireContext(), getSampleContactItemList());

        // Set layout manager and adapter
        educatorRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        educatorRecyclerView.setAdapter(adapter);


        return view;
    }

    private List<ContactItem> getSampleContactItemList() {
        List<ContactItem> contactItemList = new ArrayList<>();

        // Add sample data
        contactItemList.add(new ContactItem(R.drawable.profile_circle_svgrepo_com, "User1", "Teacher"));
        contactItemList.add(new ContactItem(R.drawable.profile_circle_svgrepo_com, "User2", "Teacher"));
        // Add more items as needed

        return contactItemList;
    }
    public void showEducatorLayout() {
        if (educatorLayout != null) {
            educatorLayout.setVisibility(View.VISIBLE);
        }
    }
    public void showStudentLayout() {
        if (studentLayout != null) {
            studentLayout.setVisibility(View.VISIBLE);
        }
    }
    public void showParentLayout() {
        if (parentLayout != null) {
            parentLayout.setVisibility(View.VISIBLE);
        }
    }
}



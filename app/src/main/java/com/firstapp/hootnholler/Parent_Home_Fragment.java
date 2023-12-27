package com.firstapp.hootnholler;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.firstapp.hootnholler.databinding.FragmentParentHomeBinding;

import java.util.Arrays;
import java.util.List;


public class Parent_Home_Fragment extends Fragment {

    private View TaskStatusBtn, QuizScoreBtn, FeedbackBtn;
    private FragmentParentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment using View Binding
        binding = FragmentParentHomeBinding.inflate(inflater, container, false);

        TaskStatusBtn = binding.TaskStatusBtn;
        QuizScoreBtn = binding.QuizBtn;
        FeedbackBtn = binding.FeedbackBtn;

        // Set click listeners for each button
        TaskStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the TaskStatusFragment
                Intent intent=new Intent(getActivity(), TaskStatus_Activity.class);
                startActivity(intent);
            }
        });

        QuizScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the QuizScoreFragment
                Intent intent=new Intent(getActivity(), Quiz_Score_Activity.class);
                startActivity(intent);
            }
        });

        FeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the FeedbackFragment
                Intent intent=new Intent(getActivity(), Feedback_Activity.class);
                startActivity(intent);
            }
        });



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Child name dropdown field
        List<String> items = Arrays.asList("John Tiew", "Andy Tiew");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, items);
        binding.dropdownField.setAdapter(adapter);


    }


}

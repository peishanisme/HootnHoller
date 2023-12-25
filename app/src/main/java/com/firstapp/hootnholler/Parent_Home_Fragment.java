package com.firstapp.hootnholler;

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
                Navigation.findNavController(v).navigate(R.id.action_parent_Home_Fragment2_to_task_status);
            }
        });

        QuizScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the QuizScoreFragment
                Navigation.findNavController(v).navigate(R.id.action_parent_Home_Fragment2_to_qui_score);
            }
        });

        FeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the FeedbackFragment
                Navigation.findNavController(v).navigate(R.id.action_parent_Home_Fragment2_to_parent_feedback);
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

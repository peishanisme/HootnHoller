package com.firstapp.hootnholler;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firstapp.hootnholler.adapter.VPAdapter;
import com.google.android.material.tabs.TabLayout;

public class educator_monitoring_feedback_view extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ImageButton addFeedbackBtn;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_educator_monitoring_feedback_view, container, false);

        // Find views using rootView
        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapterFeedbackView = new VPAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapterFeedbackView.addFragment(new educator_positive_feedback_view(), "Positive Feedback");
        vpAdapterFeedbackView.addFragment(new educator_negative_feedback_view(), "Negative Feedback");
        viewPager.setAdapter(vpAdapterFeedbackView);

//        //Dialog
//        dialog = new Dialog(FeedbackView.this);
//        dialog.setContentView(R.layout.upload_feedback_dialog);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.upload_feedback_dialog))
////        }
//
//        dialog.getWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setCancelable(false);
//
//        Button uploadBtn = dialog.findViewById(R.id.uploadBtn);
//        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
//
//        uploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(FeedbackView.this,"Uploaded",Toast.LENGTH_SHORT).show();
//                dialog.dismiss();;
//            }
//        });
//
//        uploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(FeedbackView.this,"Cancelled",Toast.LENGTH_SHORT).show();
//                dialog.dismiss();;
//            }
//        });
//
//        //addFeedbackButton
//        addFeedbackBtn = findViewById(R.id.addFeedbackBtn);
//        addFeedbackBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.show();
//            }
//        });

// Dialog
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.upload_feedback_dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        dialog.setCancelable(false);

        Button uploadBtn = dialog.findViewById(R.id.uploadBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

// addFeedbackButton
        addFeedbackBtn = rootView.findViewById(R.id.addFeedbackBtn);
        addFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        return rootView;
    }
}
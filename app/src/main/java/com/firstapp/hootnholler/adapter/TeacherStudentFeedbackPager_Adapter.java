package com.firstapp.hootnholler.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.firstapp.hootnholler.Student_Negative_Feedback_Fragment;
import com.firstapp.hootnholler.Student_Positive_Feedback_Fragment;

public class TeacherStudentFeedbackPager_Adapter extends FragmentStateAdapter {
    private String classCode, studentUID;

    public TeacherStudentFeedbackPager_Adapter(@NonNull FragmentActivity fragmentActivity, String classCode, String studentUID) {
        super(fragmentActivity);
        this.classCode = classCode;
        this.studentUID = studentUID;
    }

    public TeacherStudentFeedbackPager_Adapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public TeacherStudentFeedbackPager_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Student_Positive_Feedback_Fragment(this.classCode, this.studentUID);
            case 1:
                return  new Student_Negative_Feedback_Fragment(this.classCode, this.studentUID);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

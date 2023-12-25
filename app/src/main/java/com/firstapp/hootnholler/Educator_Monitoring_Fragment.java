package com.firstapp.hootnholler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.firstapp.hootnholler.adapter.VPAdapter;
import com.google.android.material.tabs.TabLayout;


public class Educator_Monitoring_Fragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_educator__monitoring_, container, false);

        // Find views using rootView
        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapterEducatorMain = new VPAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapterEducatorMain.addFragment(new educator_monitoring_submission(), "Submission");
        vpAdapterEducatorMain.addFragment(new educator_monitoring_quiz_score(), "Quiz Score");
        vpAdapterEducatorMain.addFragment(new educator_monitoring_feedback(), "Feedback");
        viewPager.setAdapter(vpAdapterEducatorMain);

        return rootView;
    }
}

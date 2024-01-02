package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.firstapp.hootnholler.databinding.ActivityParentMainBinding;

public class Parent_MainActivity extends AppCompatActivity {

    ActivityParentMainBinding binding;
    public static String studentUID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityParentMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.BottomNavigationView.setOnItemReselectedListener(item -> {
            int id = item.getItemId();
            if (id==R.id.home) {
                replaceFragment(new Parent_Home_Fragment());}
            else if (id == R.id.statistic) {
                replaceFragment(new Parent_Statistic_Fragment(studentUID));
            }else if (id == R.id.chat) {
                replaceFragment(new Parent_Chat_Fragment());
            }else if (id == R.id.profile) {
                replaceFragment(new Profile_Fragment());}

        });


    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

    }
}
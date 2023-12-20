package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.firstapp.hootnholler.databinding.ActivityEducatorMainBinding;

public class Educator_MainActivity extends AppCompatActivity {

    ActivityEducatorMainBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEducatorMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.BottomNavigationView.setOnItemReselectedListener(item -> {
            int id = item.getItemId();
            if (id==R.id.home) {
                replaceFragment(new Educator_Classroom_Fragment());}
            else if (id == R.id.quiz) {
                replaceFragment(new Educator_Quiz_Fragment());
            }else if (id == R.id.monitoring) {
                replaceFragment(new Educator_Monitoring_Fragment());
            }else if (id == R.id.chat) {
                replaceFragment(new Educator_Chat_Fragment());
            }else if (id == R.id.profile) {
                replaceFragment(new Educator_Profile_Fragment());}

        });


    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

    }

}
package com.example.classroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView class2;
    public FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        class2 = (CardView) findViewById(R.id.c2);
        class2.setOnClickListener(this);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Student_JoinClass.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i;

        if (v.getId() == R.id.c2) {
            i = new Intent(this, Student_Class.class);
            startActivity(i);
        }
    }
}


    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView GVClass = findViewById(R.id.gridView);
        // Replace the array below with your actual array of image resources
        int[] imageArray = {R.drawable.class1, R.drawable.class2, R.drawable.class3, R.drawable.class4, R.drawable.class5, R.drawable.class6};

        GridAdapter adapter = new GridAdapter(this, imageArray);
        //ArrayAdapter adapter = new ArrayAdapter(this, imageArray);
        GVClass.setAdapter(adapter);

        /*GVClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView TVClass = findViewById(R.id.TVClass);
                TVClass.setText("Chapter " + (position+1) + ": " + GVClass.getItemAtPosition(i).toString());
            }
        });
        }
    }


}*/
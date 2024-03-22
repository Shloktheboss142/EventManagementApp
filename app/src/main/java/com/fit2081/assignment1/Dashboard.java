package com.fit2081.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    // Method for when the user clicks the new event category button
    public void onClickNewEventCategory(View view) {

        // Switch the user to the new event category activity
        Intent intent = new Intent(this, NewEventCategory.class);
        startActivity(intent);

    }

    // Method for when the user clicks the new event button
    public void onClickNewEvent(View view) {

        // Switch the user to the acc event activity
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);

    }
}
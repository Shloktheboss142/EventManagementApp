package com.fit2081.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserLogin extends AppCompatActivity {

    // Declare input variables
    EditText etUsernameInput;
    EditText etPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Connect the variables to their respective fields
        etUsernameInput = findViewById(R.id.registerUsernameInput);
        etPasswordInput = findViewById(R.id.passwordInput);

        // Get the saved username from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString(KeyStore.USERNAME, "");

        // Set the retrieved username in the username field
        etUsernameInput.setText(savedUsername);

    }

    @Override
    public void onResume() {
        super.onResume();
        etPasswordInput.setText("");
    }

    // Method for when the user clicks the register button
    public void onClickRegister(View view) {

        // Switch to the register activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    // Method for when the user clicks on the login button
    public void onClickLogin(View view) {

        // Get the text the user has entered in the fields
        String usernameInput = etUsernameInput.getText().toString();
        String passwordInput = etPasswordInput.getText().toString();

        // Initiate a shared preferences object
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        // Get the details stored in the shared preferences
        String savedUsername = sharedPreferences.getString(KeyStore.USERNAME, "");
        String savedPassword = sharedPreferences.getString(KeyStore.PASSWORD, "");

        // Check if the username & password are the same as the one stored and are not empty
        if (usernameInput.equals(savedUsername) && passwordInput.equals(savedPassword) && !savedUsername.isEmpty()) {

            // Inform the user using a toast
            Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show();

            // Switch to the dashboard activity
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);

//            etPasswordInput.setText("");

        } else {

            // Give the user an error message in the form of a toast
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();

        }

    }

}
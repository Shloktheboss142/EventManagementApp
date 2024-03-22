package com.fit2081.assignment1;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Declare input variables
    EditText etUsernameInput;
    EditText etPasswordInput;
    EditText etConfirmPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        // Connect the variables to their respective fields
        etUsernameInput = findViewById(R.id.registerUsernameInput);
        etPasswordInput = findViewById(R.id.passwordInput);
        etConfirmPasswordInput = findViewById(R.id.confirmPasswordInput);

    }

    // Method for when the user clicks on register
    public void onClickRegister(View view) {

        // Get the values that the user has entered from the respective fields
        String usernameInput = etUsernameInput.getText().toString();
        String passwordInput = etPasswordInput.getText().toString();
        String confirmPasswordInput = etConfirmPasswordInput.getText().toString();

        // Check if the password matches the confirm password and make sure neither of the inputs are empty
        if (passwordInput.equals(confirmPasswordInput) && !passwordInput.isEmpty() && !usernameInput.isEmpty()) {

            // Call the save data function with username and password as parameters
            saveDataToSharedPreference(usernameInput, passwordInput);

            // Switch to the login activity
            Intent intent = new Intent(this, UserLogin.class);
            startActivity(intent);

        } else {

            // Send an error message to the user in the form of a toast
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();

        }

    }

    // Method for saving the user input details to shared preferences
    private void saveDataToSharedPreference(String usernameInput, String passwordInput) {

        // Initiate a shared preferences object
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store the username and password
        editor.putString(KeyStore.USERNAME, usernameInput);
        editor.putString(KeyStore.PASSWORD, passwordInput);

        // Apply the changes and send the user a toast
        editor.apply();
        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();

    }

    // Method for when the user clicks on the login button
    public void onClickLogin(View view) {

        // Switch to the login activity
        Intent intent = new Intent(this, UserLogin.class);
        startActivity(intent);

    }

}
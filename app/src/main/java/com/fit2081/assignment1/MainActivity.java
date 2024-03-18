package com.fit2081.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etUsernameInput;
    EditText etPasswordInput;
    EditText etConfirmPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsernameInput = findViewById(R.id.usernameInput);
        etPasswordInput = findViewById(R.id.passwordInput);
        etConfirmPasswordInput = findViewById(R.id.confirmPasswordInput);
    }

    public void onClickRegister(View view) {
        String usernameInput = etUsernameInput.getText().toString();
        String passwordInput = etPasswordInput.getText().toString();
        String confirmPasswordInput = etConfirmPasswordInput.getText().toString();

        if (passwordInput.equals(confirmPasswordInput) && !passwordInput.isEmpty() && !usernameInput.isEmpty()) {
            saveDataToSharedPreference(usernameInput, passwordInput);
            Intent intent = new Intent(this, UserLogin.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDataToSharedPreference(String usernameInput, String passwordInput) {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KeyStore.USERNAME, usernameInput);
        editor.putString(KeyStore.PASSWORD, passwordInput);

        editor.apply();

        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
    }

    public void onClickLogin(View view) {
        Intent intent = new Intent(this, UserLogin.class);

        startActivity(intent);
    }
}
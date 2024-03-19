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

        etUsernameInput = findViewById(R.id.cCategoryIdInput);
        etPasswordInput = findViewById(R.id.passwordInput);

        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        String savedUsername = sharedPreferences.getString(KeyStore.USERNAME, "");

        etUsernameInput.setText(savedUsername);
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    public void onClickLogin(View view) {
        String usernameInput = etUsernameInput.getText().toString();
        String passwordInput = etPasswordInput.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        String savedUsername = sharedPreferences.getString(KeyStore.USERNAME, "");
        String savedPassword = sharedPreferences.getString(KeyStore.PASSWORD, "");

        if (usernameInput.equals(savedUsername) && passwordInput.equals(savedPassword)) {
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
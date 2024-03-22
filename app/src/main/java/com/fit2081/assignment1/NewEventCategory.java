package com.fit2081.assignment1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class NewEventCategory extends AppCompatActivity {

    EditText etCategoryId;
    EditText etCategoryNameInput;
    EditText etEventCountInput;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch etIsActiveInput;
    private SMSReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_event_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        smsReceiver = new SMSReceiver();
        registerReceiver(smsReceiver,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"), RECEIVER_EXPORTED);

        etCategoryId = findViewById(R.id.cCategoryId);
        etCategoryNameInput = findViewById(R.id.cCategoryNameInput);
        etEventCountInput = findViewById(R.id.cEventCountInput);
        etIsActiveInput = findViewById(R.id.cIsActiveInput);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register SMSReceiver
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"), RECEIVER_EXPORTED);
    }

    class SMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive (Context context, Intent intent) {

            SmsMessage[] messages= Telephony.Sms.Intents.getMessagesFromIntent(intent);

            SmsMessage currentMessage = messages[0];
            String message = currentMessage.getDisplayMessageBody();

            boolean check = true;

            String[] newMessage = message.split(":", 2);

            if (!newMessage[0].equals("category")) {
                check = false;
            }

            if (newMessage.length == 2) {
                String[] messageDetails = newMessage[1].split(";", -1);

                if (messageDetails.length == 3) {

                    String categoryName = messageDetails[0];

                    if (categoryName.isEmpty()) {
                        check = false;
                    }

                    String eventCount = messageDetails[1];
                    int eventCountInt = 0;
                    if (!eventCount.isEmpty()) {
                        try {
                            eventCountInt = Integer.parseInt(messageDetails[1]);
                            if (eventCountInt <= 0) {
                                check = false;
                            }
                        } catch (Exception e) {
                            check = false;
                        }
                    } else {
                        eventCountInt = -1;
                    }

                    String isActive = messageDetails[2];
                    boolean isActiveBool = false;
                    if (!isActive.isEmpty()) {
                        if (isActive.equals("TRUE")) {
                            isActiveBool = true;
                        } else if (isActive.equals("FALSE")) {
                            isActiveBool = false;
                        } else {
                            check = false;
                        }
                    }

                    if (check) {
                        String categoryId = generateCategoryId();

                        etCategoryId.setText(categoryId);

                        etCategoryNameInput.setText(categoryName);

                        if (eventCountInt != -1) {
                            etEventCountInput.setText(String.valueOf(eventCountInt));
//                            saveDataToSharedPreference(categoryId, categoryName, String.valueOf(eventCountInt), String.valueOf(isActiveBool));
                        } else {
                            etEventCountInput.setText("");
//                            saveDataToSharedPreference(categoryId, categoryName, "", String.valueOf(isActiveBool));
                        }

                        etIsActiveInput.setChecked(isActiveBool);

                    } else {
                        Toast.makeText(context, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(context, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void onClickSaveCategory(View view) {
        String categoryId = etCategoryId.getText().toString();
        String categoryName = etCategoryNameInput.getText().toString();
        int eventCount = Integer.parseInt(etEventCountInput.getText().toString());
        boolean isActive = etIsActiveInput.isChecked();

        if (!categoryName.isEmpty()) {
            if (categoryId.isEmpty()) {
                categoryId = generateCategoryId();
            }

            etCategoryId.setText(categoryId);

            saveDataToSharedPreference(categoryId, categoryName, eventCount, isActive);
        } else {
            Toast.makeText(this, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateCategoryId() {
        Random random = new Random();
        String categoryId = "C";
        categoryId +=  (char)(random.nextInt(26) + 'a');
        categoryId +=  (char)(random.nextInt(26) + 'a');
        categoryId +=  "-";
        categoryId += String.format("%04d", random.nextInt(10000));
        return categoryId.toUpperCase();
    }

    private void saveDataToSharedPreference(String categoryId, String categoryName, int eventCount, boolean isActive) {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KeyStore.NEW_CATEGORY_CATEGORY_ID, categoryId);
        editor.putString(KeyStore.NEW_CATEGORY_CATEGORY_NAME, categoryName);
        editor.putInt(KeyStore.NEW_CATEGORY_EVENT_COUNT, eventCount);
        editor.putBoolean(KeyStore.NEW_CATEGORY_IS_ACTIVE, isActive);

        editor.apply();

        Toast.makeText(this, "Category saved successfully: " + categoryId, Toast.LENGTH_SHORT).show();
    }

}
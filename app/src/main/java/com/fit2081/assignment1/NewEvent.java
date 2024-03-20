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

public class NewEvent extends AppCompatActivity {

    EditText etEventId;
    EditText etEventNameInput;
    EditText etCategoryIdInput;
    EditText etTicketsAvailable;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch etIsActiveInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        registerReceiver(new SMSReceiver(),new IntentFilter("android.provider.Telephony.SMS_RECEIVED"), RECEIVER_EXPORTED);

        etEventId = findViewById(R.id.eEventIdInput);
        etEventNameInput = findViewById(R.id.eEventNameInput);
        etCategoryIdInput = findViewById(R.id.eCategoryIdInput);
        etTicketsAvailable = findViewById(R.id.eTicketsAvailableInput);
        etIsActiveInput = findViewById(R.id.eIsActiveInput);

    }

    class SMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive (Context context, Intent intent) {

            SmsMessage[] messages= Telephony.Sms.Intents.getMessagesFromIntent(intent);

            SmsMessage currentMessage = messages[0];
            String message = currentMessage.getDisplayMessageBody();

            boolean check = true;

            String[] newMessage = message.split(":");

            if (!newMessage[0].equals("event")) {
                check = false;
            }

            if (newMessage.length == 2) {
                String[] messageDetails = newMessage[1].split(";", -1);

                if (messageDetails.length == 4) {

                    String eventName = messageDetails[0];

                    if (eventName.isEmpty()) {
                        check = false;
                    }

                    String categoryId = messageDetails[1];

                    if (categoryId.isEmpty()) {
                        check = false;
                    }

                    String ticketsAvailable = messageDetails[2];
                    int ticketsAvailableInt = 0;
                    if (!ticketsAvailable.isEmpty()) {
                        try {
                            ticketsAvailableInt = Integer.parseInt(messageDetails[2]);
                            if (ticketsAvailableInt <= 0) {
                                check = false;
                            }
                        } catch (Exception e) {
                            check = false;
                        }
                    } else {
                        ticketsAvailableInt = -1;
                    }

                    String isActive = messageDetails[3];
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
                        String eventId = generateEventId();

                        etEventId.setText(eventId);

                        etEventNameInput.setText(eventName);

                        etCategoryIdInput.setText(categoryId);

                        if (ticketsAvailableInt != -1) {
                            etTicketsAvailable.setText(String.valueOf(ticketsAvailableInt));
//                            saveDataToSharedPreference(eventId, eventName, categoryId, String.valueOf(ticketsAvailableInt), String.valueOf(isActiveBool));
                        } else {
                            etTicketsAvailable.setText("");
//                            saveDataToSharedPreference(eventId, eventName, categoryId, "", String.valueOf(isActiveBool));
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

    public void onClickSaveEvent(View view) {
        String eventId = etEventId.getText().toString();
        String eventName = etEventNameInput.getText().toString();
        String categoryId = etCategoryIdInput.getText().toString();
        String ticketsAvailable = etTicketsAvailable.getText().toString();
        boolean isActive = etIsActiveInput.isChecked();

        if (!eventName.isEmpty() && !categoryId.isEmpty()) {
            if (eventId.isEmpty()) {
                eventId = generateEventId();
            }

            etEventId.setText(eventId);

            saveDataToSharedPreference(eventId, eventName, categoryId, ticketsAvailable, isActive);
        } else {
            Toast.makeText(this, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateEventId() {
        Random random = new Random();
        String eventId = "E";
        eventId +=  (char)(random.nextInt(26) + 'a');
        eventId +=  (char)(random.nextInt(26) + 'a');
        eventId +=  "-";
        eventId += String.format("%05d", random.nextInt(100000));
        return eventId.toUpperCase();
    }

    private void saveDataToSharedPreference(String eventId, String eventName, String categoryId, String ticketsAvailable, boolean isActive) {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KeyStore.NEW_EVENT_EVENT_ID, eventId);
        editor.putString(KeyStore.NEW_EVENT_EVENT_NAME, eventName);
        editor.putString(KeyStore.NEW_EVENT_CATEGORY_ID, categoryId);
        editor.putString(KeyStore.NEW_EVENT_TICKETS_AVAILABLE, ticketsAvailable);
        editor.putBoolean(KeyStore.NEW_EVENT_IS_ACTIVE, isActive);

        editor.apply();

        Toast.makeText(this, "Event saved: " + eventId + " to " + categoryId,  Toast.LENGTH_SHORT).show();
    }

}
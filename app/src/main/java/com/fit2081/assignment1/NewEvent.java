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

    // Declare input variables
    EditText etEventId;
    EditText etEventNameInput;
    EditText etCategoryIdInput;
    EditText etTicketsAvailable;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch etIsActiveInput;
    private SMSReceiver smsReceiver;

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

        // Request SMS permissions from user
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        // Instantiate a new SMSReceiver object and register the receiver
        smsReceiver = new SMSReceiver();
        registerReceiver(smsReceiver,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"), RECEIVER_EXPORTED);

        // Link the variables to their respective input fields
        etEventId = findViewById(R.id.eEventIdInput);
        etEventNameInput = findViewById(R.id.eEventNameInput);
        etCategoryIdInput = findViewById(R.id.eCategoryIdInput);
        etTicketsAvailable = findViewById(R.id.eTicketsAvailableInput);
        etIsActiveInput = findViewById(R.id.eIsActiveInput);

    }

    // Override the onPause method to unregister the SMSReceiver when the user leaves the activity
    @Override
    protected void onPause() {

        super.onPause();
        unregisterReceiver(smsReceiver);

    }

    // Override the onResume method to re-register the SMSReceiver when the user re-enters the activity
    @Override
    protected void onResume() {

        super.onResume();
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"), RECEIVER_EXPORTED);

    }

    // SMSReceiver class to handle SMSs
    class SMSReceiver extends BroadcastReceiver {

        // Method to handle incoming SMSs
        @Override
        public void onReceive (Context context, Intent intent) {

            // Get the message content
            SmsMessage[] messages= Telephony.Sms.Intents.getMessagesFromIntent(intent);

            // Extract the message and convert it into a string
            SmsMessage currentMessage = messages[0];
            String message = currentMessage.getDisplayMessageBody();

            // Initialise the check variable, which is used to keep track of the message validation
            boolean check = true;

            // Split the message by the : delimiter, and se the limit to 2 (As valid messages will only have one occurrence of :)
            String[] firstPart = message.split(":",2);

            // Check if the first part of the message matches "event"
            if (!firstPart[0].equals("event")) {

                // Set the check variable to false if the condition fails
                check = false;

            }

            // Make sure the first part of the message is only 2 indexes long
            if (firstPart.length == 2) {

                // Extract the second part of the message and split it with the ; delimiter and limit of -1 (which splits ";;" into ["","",""])
                String[] eventDetails = firstPart[1].split(";", -1);

                // Make sure the category details are only 4 indexes long
                if (eventDetails.length == 4) {

                    // Extract the event name
                    String eventName = eventDetails[0];

                    // Make sure that the event name is not blank
                    if (eventName.isEmpty()) {

                        // Set the check variable to false if the condition fails
                        check = false;

                    }

                    // Extract the category id
                    String categoryId = eventDetails[1];

                    // Make sure hte category id is not empty
                    if (categoryId.isEmpty()) {

                        // Set the check variable to false if the condition fails
                        check = false;

                    }

                    // Extract the tickets available
                    String ticketsAvailable = eventDetails[2];
                    int ticketsAvailableInt = 0;

                    // Check if tickets available is blank
                    if (!ticketsAvailable.isEmpty()) {

                        // Try converting the tickets available into an integer to make sure its a number
                        try {

                            ticketsAvailableInt = Integer.parseInt(eventDetails[2]);
                            if (ticketsAvailableInt <= 0) {

                                // Set the check variable to false if the condition fails
                                check = false;

                            }
                        } catch (Exception e) {

                            // Set the check variable to false if the condition fails
                            check = false;

                        }
                    } else {

                        // Set the tickets available to -1, which means that it was blank
                        ticketsAvailableInt = -1;

                    }

                    // Extract the is active boolean
                    String isActive = eventDetails[3];

                    // Set the default value to false
                    boolean isActiveBool = false;

                    // Check if is active is empty
                    if (!isActive.isEmpty()) {

                        // If the value if TRUE, set the variable to true, and same for FALSE
                        if (isActive.equals("TRUE")) {

                            isActiveBool = true;

                        } else if (isActive.equals("FALSE")) {

                            isActiveBool = false;

                        } else {

                            // Set the check variable to false if the condition fails
                            check = false;

                        }

                    }

                    // Check if the check variable has remained true
                    if (check) {

                        // Set the event name in the correct field
                        etEventNameInput.setText(eventName);

                        // Set the category id in the correct field
                        etCategoryIdInput.setText(categoryId);

                        // Check if the tickets available is -1
                        if (ticketsAvailableInt != -1) {

                            // Set the value of the tickets available
                            etTicketsAvailable.setText(String.valueOf(ticketsAvailableInt));

                        } else {

                            // If the tickets available is -1, set the tickets available field back to empty
                            etTicketsAvailable.setText("");

                        }

                        // Set the is active switch respectively
                        etIsActiveInput.setChecked(isActiveBool);

                    } else {

                        // Inform the user that the message is invalid through a toast
                        Toast.makeText(context, "Unknown or invalid command", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    // Inform the user that the message is invalid through a toast
                    Toast.makeText(context, "Unknown or invalid command", Toast.LENGTH_SHORT).show();

                }

            } else {

                // Inform the user that the message is invalid through a toast
                Toast.makeText(context, "Unknown or invalid command", Toast.LENGTH_SHORT).show();

            }

        }

    }

    // Method for when the user clicks save category
    public void onClickSaveEvent(View view) {

        // Get the entered event name
        String eventName = etEventNameInput.getText().toString();

        // Get the entered category id
        String categoryId = etCategoryIdInput.getText().toString();

        // Get the entered tickets available
        String ticketsAvailableInput = etTicketsAvailable.getText().toString();
        int ticketsAvailable = 0;

        // Try convert the event count input to an integer
        try {

            ticketsAvailable = Integer.parseInt(ticketsAvailableInput);

        } catch (Exception ignored) {}

        // Get the is active switch status
        boolean isActive = etIsActiveInput.isChecked();

        // Check if the event name & category id are empty
        if (!eventName.isEmpty() && !categoryId.isEmpty()) {

            // Generate a event id and set it into the field
            String eventId = generateEventId();
            etEventId.setText(eventId);

            // Save all the data to shared preferences
            saveDataToSharedPreference(eventId, eventName, categoryId, ticketsAvailable, isActive);

        } else {

            // Inform the user that the inputs are invalid through a toast
            Toast.makeText(this, "Unknown or invalid command", Toast.LENGTH_SHORT).show();

        }

    }

    // Method to generate event ID's
    private String generateEventId() {
        Random random = new Random();
        String eventId = "E";
        eventId +=  (char)(random.nextInt(26) + 'a');
        eventId +=  (char)(random.nextInt(26) + 'a');
        eventId +=  "-";
        eventId += String.format("%05d", random.nextInt(100000));
        return eventId.toUpperCase();
    }

    // Method to save data to shared preferences
    private void saveDataToSharedPreference(String eventId, String eventName, String categoryId, int ticketsAvailable, boolean isActive) {

        // Instantiate a shared preferences object
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Assign the data to respective keys
        editor.putString(KeyStore.NEW_EVENT_EVENT_ID, eventId);
        editor.putString(KeyStore.NEW_EVENT_EVENT_NAME, eventName);
        editor.putString(KeyStore.NEW_EVENT_CATEGORY_ID, categoryId);
        editor.putInt(KeyStore.NEW_EVENT_TICKETS_AVAILABLE, ticketsAvailable);
        editor.putBoolean(KeyStore.NEW_EVENT_IS_ACTIVE, isActive);

        editor.apply();

        // Inform the user that the category has successfully be saved through a toast
        Toast.makeText(this, "Event saved: " + eventId + " to " + categoryId,  Toast.LENGTH_SHORT).show();

    }

}
package com.fit2081.assignment1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ClipData;
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
import androidx.lifecycle.ViewModelProvider;

import com.fit2081.assignment1.provider.EMAViewModel;
import com.fit2081.assignment1.provider.EventCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class NewEventCategory extends AppCompatActivity {

    // Declare input variables
    EditText etCategoryId;
    EditText etCategoryNameInput;
    EditText etEventCountInput;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch etIsActiveInput;
    EditText etLocationInput;
    private SMSReceiver smsReceiver;
    EMAViewModel mEMAViewModel;

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

        // Request SMS permissions from user
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        // Instantiate a new SMSReceiver object and register the receiver
        smsReceiver = new SMSReceiver();
        registerReceiver(smsReceiver,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"), RECEIVER_EXPORTED);

        // Link the variables to their respective input fields
        etCategoryId = findViewById(R.id.cCategoryId);
        etCategoryNameInput = findViewById(R.id.cCategoryNameInput);
        etEventCountInput = findViewById(R.id.cEventCountInput);
        etIsActiveInput = findViewById(R.id.cIsActiveInput);
        etLocationInput = findViewById(R.id.eventLocation);

        mEMAViewModel = new ViewModelProvider(this).get(EMAViewModel.class);
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
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            // Extract the message and convert it into a string
            SmsMessage currentMessage = messages[0];
            String message = currentMessage.getDisplayMessageBody();

            // Initialise the check variable, which is used to keep track of the message validation
            boolean check = true;

            // Split the message by the : delimiter, and se the limit to 2 (As valid messages will only have one occurrence of :)
            String[] firstPart = message.split(":", 2);

            // Check if the first part of the message matches "category"
            if (!firstPart[0].equals("category")) {

                // Set the check variable to false if the condition fails
                check = false;

            }

            // Make sure the first part of the message is only 2 indexes long
            if (firstPart.length == 2) {

                // Extract the second part of the message and split it with the ; delimiter and limit of -1 (which splits ";;" into ["","",""])
                String[] categoryDetails = firstPart[1].split(";", -1);

                // Make sure the category details are only 3 indexes long
                if (categoryDetails.length == 3) {

                    // Extract the category name
                    String categoryName = categoryDetails[0];

                    // Make sure that the category name is not blank
                    if (categoryName.isEmpty()) {

                        // Set the check variable to false if the condition fails
                        check = false;

                    }

                    // Extract the event count
                    String eventCount = categoryDetails[1];
                    int eventCountInt = 0;

                    // Check if event count is blank
                    if (!eventCount.isEmpty()) {

                        // Try converting the event count into an integer to make sure its a number
                        try {

                            eventCountInt = Integer.parseInt(categoryDetails[1]);
                            if (eventCountInt <= 0) {

                                // Set the check variable to false if the condition fails
                                check = false;

                            }
                        } catch (Exception e) {

                            // Set the check variable to false if the conversion
                            check = false;

                        }
                    } else {

                        // Set the event count to -1, which means that it was blank
                        eventCountInt = -1;

                    }

                    // Extract the is active boolean
                    String isActive = categoryDetails[2];

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

                        // Set the category name in the correct field
                        etCategoryNameInput.setText(categoryName);

                        // Check if the event count is -1
                        if (eventCountInt != -1) {

                            // Set the value of the event count
                            etEventCountInput.setText(String.valueOf(eventCountInt));

                        } else {

                            // If the event count is -1, set the event count field back to empty
                            etEventCountInput.setText("");

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
    public void onClickSaveCategory(View view) {

        // Get the entered category name
        String categoryName = etCategoryNameInput.getText().toString();

        // Get the entered event count input
        String eventCountInput = etEventCountInput.getText().toString();

        String locationName = etLocationInput.getText().toString();

        int eventCount = 0;

        // Try convert the event count input to an integer
        try {

            eventCount = Integer.parseInt(eventCountInput);

        } catch (Exception ignored) {}

        // Get the is active switch status
        boolean isActive = etIsActiveInput.isChecked();

        // Check if the category name is empty
        if (checkCategoryName(categoryName)) {

            // Generate a category id and set it into the field
            String categoryId = generateCategoryId();
            etCategoryId.setText(categoryId);

            EventCategory Test = new EventCategory(categoryId, categoryName, Integer.toString(eventCount), Boolean.toString(isActive), locationName);
            mEMAViewModel.insert(Test);

            Toast.makeText(this, "Category saved successfully: " + categoryId, Toast.LENGTH_SHORT).show();

            finish();

        } else {

            // Inform the user that the inputs are invalid through a toast
            Toast.makeText(this, "Invalid category name", Toast.LENGTH_SHORT).show();

        }

    }

    public boolean checkCategoryName(String name) {
        boolean justSpaces = true;
        if (name.isEmpty()) {
            return false;
        }
        try {
            String tempName = name.replaceAll("\\s+","");
            int invalidCategoryName = Integer.parseInt(tempName);
            return false;
        } catch (Exception ignored) {}
        for (char ch : name.toCharArray()) {
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (Character.getNumericValue(ch) >= 0 && Character.getNumericValue(ch) <= 9)) {
                justSpaces = false;
            } else if (Character.isWhitespace(ch)) {
                continue;
            } else {
                return false;
            }
        }
        return !justSpaces;
    }

    // Method to generate category ID's
    private String generateCategoryId() {
        Random random = new Random();
        String categoryId = "C";
        categoryId +=  (char)(random.nextInt(26) + 'a');
        categoryId +=  (char)(random.nextInt(26) + 'a');
        categoryId +=  "-";
        categoryId += String.format("%04d", random.nextInt(10000));
        return categoryId.toUpperCase();
    }

}
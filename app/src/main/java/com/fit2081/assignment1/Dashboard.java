package com.fit2081.assignment1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class Dashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FloatingActionButton fab;
    FragmentManager fragmentManager;
    FragmentListCategory fragmentListCategory;
    Gson gson = new Gson();
    EditText etEventId;
    EditText etEventNameInput;
    EditText etCategoryIdInput;
    EditText etTicketsAvailable;
    Switch etIsActiveInput;
    Type typeC = new TypeToken<ArrayList<EventCategoryItem>>() {}.getType();
    Type typeE = new TypeToken<ArrayList<EventItem>>() {}.getType();

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

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            onClickSaveEvent(view);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        fragmentManager = getSupportFragmentManager();
        fragmentListCategory = new FragmentListCategory();

        fragmentManager.beginTransaction().replace(R.id.fragment1,fragmentListCategory, "MAIN_FRAGMENT").commit();

        etEventId = findViewById(R.id.eventId);
        etEventNameInput = findViewById(R.id.eventName);
        etCategoryIdInput = findViewById(R.id.categoryIdInput);
        etTicketsAvailable = findViewById(R.id.ticketsAvailable);
        etIsActiveInput = findViewById(R.id.isActiveInput);

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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clearEventForm) {
            clearForm();
//            Toast.makeText(this, "Clear Event Form" , Toast.LENGTH_SHORT).show();
        } else if (id == R.id.deleteAllCategories) {
            ArrayList<EventCategoryItem> allCategories = new ArrayList<>();
            EventCategoryItem categoryRows = new EventCategoryItem("Id", "Name", "Event Count", "Active?");

            allCategories.add(categoryRows);

            String allCategoriesStr = gson.toJson(allCategories);

            SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KeyStore.ALL_EVENT_CATEGORIES, allCategoriesStr);
            editor.apply();
//            Toast.makeText(this, "Delete All Categories" , Toast.LENGTH_SHORT).show();
        } else if (id == R.id.deleteAllEvents) {
            deleteAllEvents();
//            Toast.makeText(this, "Delete All Events" , Toast.LENGTH_SHORT).show();
        } else if (id == R.id.refresh) {
            onRefresh();
//            Toast.makeText(this, "Refresh" , Toast.LENGTH_SHORT).show();
        } else if (id == R.id.viewAllCategories) {
            Intent intent = new Intent(this, ListCategoryActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
//            Toast.makeText(this, "View All Categories" , Toast.LENGTH_SHORT).show();
        } else if (id == R.id.addCategory) {
            Intent intent = new Intent(this, NewEventCategory.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
//            Toast.makeText(this, "Add Category" , Toast.LENGTH_SHORT).show();
        } else if (id == R.id.viewALlEvents) {
            Intent intent = new Intent(this, ListEventActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
//            Toast.makeText(this, "View All Events" , Toast.LENGTH_SHORT).show();
        } else if (id == R.id.logout) {
            finish();
            drawerLayout.closeDrawers();
//            Toast.makeText(this, "Logout" , Toast.LENGTH_SHORT).show();
        }

        // tell the OS
        return true;
    }

    public void deleteAllEvents() {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String savedEventCategories = sharedPreferences.getString(KeyStore.ALL_EVENT_CATEGORIES, "");
        ArrayList<EventCategoryItem> allCategories = gson.fromJson(savedEventCategories,typeC);
        String savedEvents = sharedPreferences.getString(KeyStore.ALL_EVENTS, "");
        ArrayList<EventItem> allEvents = gson.fromJson(savedEvents,typeE);

        for (EventItem event : allEvents) {
            String EcategoryId = event.getCategoryId();

            for (EventCategoryItem category : allCategories) {
                String CcategoryId = category.getCategoryId();

                if (EcategoryId.equals(CcategoryId)) {
                    category.decrementCount();
                }
            }
        }

        ArrayList<EventItem> freshEvents = new ArrayList<>();
        String freshEventsStr = gson.toJson(freshEvents);
        String allCategoriesStr = gson.toJson(allCategories);
        editor.putString(KeyStore.ALL_EVENTS, freshEventsStr);
        editor.putString(KeyStore.ALL_EVENT_CATEGORIES, allCategoriesStr);
        editor.apply();
        onRefresh();
    }

    public void onRefresh() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("MAIN_FRAGMENT");

        System.out.println(fragment);

        if (fragment != null) {
            transaction.remove(fragment);
            transaction.add(R.id.fragment1, new FragmentListCategory(), "MAIN_FRAGMENT");
            transaction.commit();
        }
    }

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

        boolean checkEvent = checkEventName(eventName);

        // Check if the event name & category id are empty
        if (checkEvent) {

            // Generate a event id and set it into the field
            String eventId = generateEventId();


            EventItem newData = new EventItem(eventId, eventName, categoryId, Integer.toString(ticketsAvailable), Boolean.toString(isActive));

            SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

            String savedEventCategories = sharedPreferences.getString(KeyStore.ALL_EVENT_CATEGORIES, "");
            ArrayList<EventCategoryItem> allCategories = gson.fromJson(savedEventCategories,typeC);

            ArrayList<String> addedCategories = new ArrayList<>();
            for (EventCategoryItem item : allCategories) {
                addedCategories.add(item.getCategoryId());
            }

            if (addedCategories.contains(categoryId) && !categoryId.equals("Id")) {
                etEventId.setText(eventId);

                String savedEvents = sharedPreferences.getString(KeyStore.ALL_EVENTS, "");
                ArrayList<EventItem> allEvents = gson.fromJson(savedEvents,typeE);

                allEvents.add(newData);

                for (EventCategoryItem item : allCategories) {
                    if (item.getCategoryId().equals(categoryId)) {
                        item.incrementCount();
                    }
                }

                String allEventsStr = gson.toJson(allEvents);
                String allCategoriesStr = gson.toJson(allCategories);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KeyStore.ALL_EVENTS, allEventsStr);
                editor.putString(KeyStore.ALL_EVENT_CATEGORIES, allCategoriesStr);
                editor.apply();
                String snackBarText = "Event saved: " + eventId + " to " + categoryId;
                Snackbar.make(view, snackBarText, Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                undoLastSave();
                            }
                        })
                        .setAnchorView(fab)
                        .show();
                onRefresh();
            } else {
                Toast.makeText(this, "Category does not exist", Toast.LENGTH_SHORT).show();
            }



        } else {

            // Inform the user that the inputs are invalid through a toast
            Toast.makeText(this, "Invalid event name", Toast.LENGTH_SHORT).show();

        }

    }

    public boolean checkEventName(String name) {
        boolean justSpaces = true;
        if (name.isEmpty()) {
            return false;
        }
        try {
            String tempName = name.replaceAll("\\s+","");
            int invalidEventName = Integer.parseInt(tempName);
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

    private void undoLastSave() {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        String savedEventCategories = sharedPreferences.getString(KeyStore.ALL_EVENT_CATEGORIES, "");
        ArrayList<EventCategoryItem> allCategories = gson.fromJson(savedEventCategories,typeC);
        String savedEvents = sharedPreferences.getString(KeyStore.ALL_EVENTS, "");
        ArrayList<EventItem> allEvents = gson.fromJson(savedEvents,typeE);

        EventItem lastAdded = allEvents.get(allEvents.size()-1);
        allEvents.remove(allEvents.size()-1);

        for (EventCategoryItem category : allCategories) {
            if (lastAdded.getCategoryId().equals(category.getCategoryId())) {
                category.decrementCount();
            }
        }

        String allEventsStr = gson.toJson(allEvents);
        String allCategoriesStr = gson.toJson(allCategories);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KeyStore.ALL_EVENTS, allEventsStr);
        editor.putString(KeyStore.ALL_EVENT_CATEGORIES, allCategoriesStr);
        editor.apply();
        onRefresh();
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

    public void clearForm() {
        etEventId.setText("");
        etEventNameInput.setText("");
        etCategoryIdInput.setText("");
        etTicketsAvailable.setText("");
        etIsActiveInput.setChecked(false);
    }

}
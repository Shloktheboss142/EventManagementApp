package com.fit2081.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.fit2081.assignment1.provider.EMAViewModel;
import com.fit2081.assignment1.provider.Event;
import com.fit2081.assignment1.provider.EventCategory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FloatingActionButton fab;
    private EMAViewModel mEMAViewModel;
    private GestureDetectorCompat mDetector;
    FragmentManager fragmentManager;
    FragmentListCategory fragmentListCategory;
    EditText etEventId, etEventNameInput, etCategoryIdInput, etTicketsAvailable;
    Switch etIsActiveInput;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Getting the fab and setting a listener for it
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            onClickSaveEvent(view);
        });

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting up the drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Initialising the fragment
        fragmentManager = getSupportFragmentManager();
        fragmentListCategory = new FragmentListCategory();
        fragmentManager.beginTransaction().replace(R.id.fragment1,fragmentListCategory, "MAIN_FRAGMENT").commit();

        // Setting the text variables to their respective fields
        etEventId = findViewById(R.id.eventId);
        etEventNameInput = findViewById(R.id.eventName);
        etCategoryIdInput = findViewById(R.id.categoryIdInput);
        etTicketsAvailable = findViewById(R.id.ticketsAvailable);
        etIsActiveInput = findViewById(R.id.isActiveInput);

        // Setting up the ViewModel
        mEMAViewModel = new ViewModelProvider(this).get(EMAViewModel.class);

        // Setting up the gesture detector
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mDetector = new GestureDetectorCompat(this, customGestureDetector);

        // Setting up the special double tap listener
        mDetector.setOnDoubleTapListener(customGestureDetector);

        View touchpad = findViewById(R.id.touchpad);

        // Setting up the touchpad listener
        touchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

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

    // Handle all the different buttons on the toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.clearEventForm) {
            clearForm();
            Toast.makeText(this, "Successfully cleared form" , Toast.LENGTH_SHORT).show();
        } else if (id == R.id.deleteAllCategories) {
            deleteAllCategories();
            Toast.makeText(this, "Deleted all categories" , Toast.LENGTH_SHORT).show();
        } else if (id == R.id.deleteAllEvents) {
            deleteAllEvents();
            Toast.makeText(this, "Deleted all events" , Toast.LENGTH_SHORT).show();
        } else if (id == R.id.viewAllCategories) {
            Intent intent = new Intent(this, ListCategoryActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
        } else if (id == R.id.addCategory) {
            Intent intent = new Intent(this, NewEventCategory.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
        } else if (id == R.id.viewALlEvents) {
            Intent intent = new Intent(this, ListEventActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
        } else if (id == R.id.logout) {
            finish();
            drawerLayout.closeDrawers();
            Toast.makeText(this, "Logged out successfully" , Toast.LENGTH_SHORT).show();
        }

        return true;

    }

    // Method used to clear the form on the dashboard
    public void clearForm() {

        etEventId.setText("");
        etEventNameInput.setText("");
        etCategoryIdInput.setText("");
        etTicketsAvailable.setText("");
        etIsActiveInput.setChecked(false);

    }

    // Method to delete all categories in the system
    public void deleteAllCategories() {

        mEMAViewModel.deleteAllCategories();
        mEMAViewModel.deleteAllEvents();

    }

    // Method used to delete all events in the system
    public void deleteAllEvents() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            List<Event> events = mEMAViewModel.getAllEvents();

            // Decrement the event count for each category
            for (Event event : events) {
                mEMAViewModel.decrementCategoryCount(event.getCategoryId());
            }

            mEMAViewModel.deleteAllEvents();

            uiHandler.post(() -> {

            });
        });

    }

    // Method to save the event from the details entered int the form
    public void onClickSaveEvent(View view) {

        // Get the entered details
        String eventName = etEventNameInput.getText().toString();
        String categoryId = etCategoryIdInput.getText().toString().toUpperCase();
        String ticketsAvailableInput = etTicketsAvailable.getText().toString();
        int ticketsAvailable = 0;

        // Try convert the event count input to an integer
        try {

            ticketsAvailable = Integer.parseInt(ticketsAvailableInput);

        } catch (Exception ignored) {}

        // Get the is active switch status
        boolean isActive = etIsActiveInput.isChecked();

        // Calling a check method to validate the event name entered
        boolean checkEvent = checkEventName(eventName);

        if (checkEvent) {

            String eventId = generateEventId();
            Event Test = new Event(eventId, eventName, categoryId, Integer.toString(ticketsAvailable), Boolean.toString(isActive));

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {

                List<EventCategory> categories = mEMAViewModel.checkIfCategoryIdExists(categoryId);

                if (!categories.isEmpty()) {
                    mEMAViewModel.insert(Test);
                    mEMAViewModel.incrementCategoryCount(categoryId);
                }

                uiHandler.post(() -> {

                    if (categories.isEmpty()) {
                        Toast.makeText(this, "Category does not exist", Toast.LENGTH_SHORT).show();
                    } else {
                        String snackBarText = "Event saved: " + eventId + " to " + categoryId;

                        etEventId.setText(eventId);

                        Snackbar.make(view, snackBarText, Snackbar.LENGTH_LONG)
                                .setAction("UNDO", v -> undoLastSave(eventId, categoryId))
                                .setAnchorView(fab)
                                .show();
                    }
                });
            });

        } else {

            Toast.makeText(this, "Invalid event name", Toast.LENGTH_SHORT).show();

        }

    }

    // Method used to check if the event name is valid
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

    // Method to undo the save
    private void undoLastSave(String eventId, String categoryId) {

        mEMAViewModel.deleteEvent(eventId);
        mEMAViewModel.decrementCategoryCount(categoryId);

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


    // Class for detecting gestures
    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{

        // Method to handle the long press gesture
        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            Toast.makeText(Dashboard.this, "Successfully cleared form", Toast.LENGTH_SHORT).show();
            clearForm();
            super.onLongPress(e);
        }

        // Method to handle the double tap gesture
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            onClickSaveEvent(findViewById(R.id.fab));
            return super.onDoubleTap(e);
        }

    }

}
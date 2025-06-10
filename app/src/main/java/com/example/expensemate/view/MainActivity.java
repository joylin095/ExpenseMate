package com.example.expensemate.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Build;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemate.R;
import com.example.expensemate.service.LocationForegroundService;
import com.example.expensemate.viewModel.SharedDateViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kal.rackmonthpicker.RackMonthPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textViewMonth;
    private int currentYear, currentMonth;
    private SharedDateViewModel sharedDateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        requestPermissionsIfNeeded();
        activateForegroundService();

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);

        // 預設顯示 HomeFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new HomeFragment())
                    .commit();
        }

        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            }
            else if(item.getItemId() == R.id.navigation_report){
                selectedFragment = new ReportFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }

    private void initViews() {
        sharedDateViewModel = new ViewModelProvider(this).get(SharedDateViewModel.class);

        textViewMonth = findViewById(R.id.textViewMonth);
        findViewById(R.id.btnPreviousMonth).setOnClickListener(v -> showPreviousMonth());
        findViewById(R.id.btnNextMonth).setOnClickListener(v -> showNextMonth());

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);

        updateCurrentDate(currentYear, currentMonth);

        textViewMonth.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showPreviousMonth() {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        updateCurrentDate(currentYear, currentMonth);
    }

    private void showNextMonth() {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        updateCurrentDate(currentYear, currentMonth);
    }

    private void showDatePickerDialog() {
        new RackMonthPicker(this).setLocale(Locale.getDefault())
                .setNegativeText("取消")
                .setPositiveText("確認")
                .setPositiveButton((month, startDate, endDate, year, monthLabel) -> {
                    currentYear = year;
                    currentMonth = month - 1;
                    updateCurrentDate(currentYear, currentMonth);
                })
                .setNegativeButton(Dialog::cancel).show();
    }

    @SuppressLint("SetTextI18n")
    private void updateMonthDisplay() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy MM月", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        String monthString = monthFormat.format(calendar.getTime());
        textViewMonth.setText(monthString);
    }

    private void updateCurrentDate(int currentYear, int currentMonth) {
        sharedDateViewModel.setCurrentDate(currentYear, currentMonth);
        updateMonthDisplay();
    }

    private void requestPermissionsIfNeeded() {
        List<String> permissionsNeeded = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.FOREGROUND_SERVICE);
        }

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsNeeded.toArray(new String[0]),
                    101
            );
        }
    }

    private void activateForegroundService() {
        Intent intent = new Intent(this, LocationForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
}

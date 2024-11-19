package com.example.simplemute;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.simplemute.databinding.ActivitySlashBinding;

public class SlashActivity extends AppCompatActivity {

    ActivitySlashBinding activitySlashBinding;
    int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATION = 123;
    private static final int REQUEST_CODE_DND_PERMISSION = 124;

    // Flag to track if all permissions are granted
    private boolean allPermissionsGranted = false;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_slash);
        activitySlashBinding = DataBindingUtil.setContentView(this, R.layout.activity_slash);
        activitySlashBinding.setSlashActivityBind(this);
        sharedPreferences = getSharedPreferences("Don't show slash Activity", Context.MODE_PRIVATE);
        // Initially disable the "Complete" button
        activitySlashBinding.completeBtn.setEnabled(false);
        activitySlashBinding.completeBtn.setAlpha(0.5f);
        boolean slashScreen = sharedPreferences.getBoolean("don't show slash screen", false);// Optionally dim the button
        if(slashScreen){
            startActivity(new Intent(SlashActivity.this,MainActivity.class));
            finish();
        }

        // DND Button Click
        activitySlashBinding.dndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDndPermission();
            }
        });

        // Battery Optimization Button Click
        activitySlashBinding.batteryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bettoryOptimization();
            }
        });

        // Notification Permission Button Click
        activitySlashBinding.notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if notification permission is granted
                if (ContextCompat.checkSelfPermission(SlashActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SlashActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
                    activitySlashBinding.notificationBtn.setBackgroundResource(R.color.bgcolor);
                } else {
                    checkAndEnableCompleteButton();
                }
            }
        });
        //after getting all the permission and enable complate button click
        activitySlashBinding.completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Put data into SharedPreferences (key-value pair)
                editor.putBoolean("don't show slash screen",true);

                // Commit the changes to save the data
                editor.apply();
                startActivity(new Intent(SlashActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    // Battery Optimization
    private void bettoryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
                // Request to ignore battery optimizations
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATION);
            } else {
                // Already ignoring battery optimizations, set the button color
                activitySlashBinding.batteryBtn.setBackgroundResource(R.color.bgcolor);
                checkAndEnableCompleteButton();
            }
        }
    }

    // Handle the result of permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                activitySlashBinding.notificationBtn.setBackgroundResource(R.color.bgcolor);
                checkAndEnableCompleteButton();
            } else {
                // Permission denied, show a message to the user
                showPermissionDeniedMessage();
            }
        }
    }

    // Check if Do Not Disturb permission is granted
    private void checkDndPermission() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the DND permission is already granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!nm.isNotificationPolicyAccessGranted()) {
                // Request DND permission by opening the DND settings screen
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult(intent, REQUEST_CODE_DND_PERMISSION);
            } else {
                // Permission already granted, change button color to indicate success
                activitySlashBinding.dndBtn.setBackgroundResource(R.color.bgcolor);
                checkAndEnableCompleteButton();
            }
        }
    }

    // Show a message if the notification permission is denied
    private void showPermissionDeniedMessage() {
        Toast.makeText(this, "Notification permission is required to send notifications.", Toast.LENGTH_LONG).show();
    }

    // Method to check if all permissions are granted and enable the "Complete" button
    private void checkAndEnableCompleteButton() {
        // Check if all permissions are granted (DND, Battery Optimization, Notification)
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        boolean dndPermissionGranted = nm.isNotificationPolicyAccessGranted();
        boolean batteryPermissionGranted = pm.isIgnoringBatteryOptimizations(getPackageName());
        boolean notificationPermissionGranted = ContextCompat.checkSelfPermission(SlashActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;

        // If all permissions are granted, enable the "Complete" button
        if (dndPermissionGranted && batteryPermissionGranted && notificationPermissionGranted) {
            allPermissionsGranted = true;
            activitySlashBinding.completeBtn.setEnabled(true);
            activitySlashBinding.completeBtn.setAlpha(1f);  // Make the button fully visible
            activitySlashBinding.completeBtn.setBackgroundResource(R.color.bgcolor);  // Set the desired color for the complete button
        }
    }

    // Handle activity result for battery optimization and DND permissions
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATION) {
            // Re-check if the battery optimization is ignored after returning from settings
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm.isIgnoringBatteryOptimizations(getPackageName())) {
                activitySlashBinding.batteryBtn.setBackgroundResource(R.color.bgcolor);
                checkAndEnableCompleteButton();
            }else{
                activitySlashBinding.batteryBtn.setBackgroundResource(R.color.bgcolor);
            }
        }

        if (requestCode == REQUEST_CODE_DND_PERMISSION) {
            // Re-check if the DND permission is granted after returning from settings
            checkDndPermission();  // This will update the color based on the permission state
        }
    }
}

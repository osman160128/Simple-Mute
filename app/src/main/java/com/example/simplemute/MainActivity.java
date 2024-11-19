
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;

import com.example.simplemute.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    int PERMISSION_REQUEST_CODE = 1;

    private static final int REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATION = 123;
    private static final int REQUEST_CODE_DND_PERMISSION = 124;
    // Get SharedPreferences object. Use the same file name used for storing data.
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Allready allow background", Context.MODE_PRIVATE);
        boolean backgroundAllow = sharedPreferences.getBoolean("already allow background", false);
        if(backgroundAllow){

        }else {
            // Check the manufacturer and open auto-start settings
            openAutoStartSettings();
        }


    }

    // Open the device-specific auto-start settings
    private void openAutoStartSettings() {
        String manufacturer = Build.MANUFACTURER.toLowerCase();

        if (manufacturer.contains("xiaomi")) {
            //show the allert dialog before open setting to inform user
            new AlertDialog.Builder(this)
                    .setTitle( "Xaomi Background Autostart")
                    .setMessage("Please Open 'Background AutoStart setting' settings to start app automatically after reboot and make app perfetctly work otherwis" +
                            " it may not be work perfectly.Find this " +
                            "app name 'Simmple Silent' in settings and enable it" )
                    .setPositiveButton("OPEN SETTING", (dialog, which) -> {
                        // Action when "OK" is clicked
                        openXiaomiAutoStartSettings();
                        // Call the function to open settings or perform other actions
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // Action when "Cancel" is clicked
                        Toast.makeText(MainActivity.this, "Action Cancelled", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        } else if (manufacturer.contains("huawei")) {
            //show the allert dialog before open setting to inform user);

            //show the allert dialog before open setting to inform user
            new AlertDialog.Builder(this)
                    .setTitle( "Huawei Background Autostart")
                    .setMessage("Please Open 'Background AutoStart setting' settings to start app automatically after reboot and make app perfetctly work otherwis" +
                            " it may not be work perfectly.Find this " +
                            "app name 'Simmple Silent' in settings and enable it" )
                    .setPositiveButton("OPEN SETTING", (dialog, which) -> {
                        // Action when "OK" is clicked
                        openHuaweiAutoStartSettings();
                        // Call the function to open settings or perform other actions
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // Action when "Cancel" is clicked
                        Toast.makeText(MainActivity.this, "Action Cancelled", Toast.LENGTH_SHORT).show();
                    })
                    .show();
            openOppoAutoStartSettings();
        } else if (manufacturer.contains("vivo")) {
            //show the allert dialog before open setting to inform user
            new AlertDialog.Builder(this)
                    .setTitle( "Vivo Background Autostart")
                    .setMessage("Please Open 'Background AutoStart setting' settings to start app automatically after reboot and make app perfetctly work otherwis" +
                            " it may not be work perfectly.Find this " +
                            "app name 'Simmple Silent' in settings and enable it" )
                    .setPositiveButton("OPEN SETTING", (dialog, which) -> {
                        // Action when "OK" is clicked
                        openVivoAutoStartSettings();
                        // Call the function to open settings or perform other actions
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // Action when "Cancel" is clicked
                        Toast.makeText(MainActivity.this, "Action Cancelled", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        } else if (manufacturer.contains("oppo")) {
            //show the allert dialog before open setting to inform user
            new AlertDialog.Builder(this)
                    .setTitle( "Oppo Background Autostart")
                    .setMessage("Please Open 'Background AutoStart setting' settings to start app automatically after reboot and make app perfetctly work otherwis" +
                            " it may not be work perfectly.Find this " +
                            "app name 'Simmple Silent' in settings and enable it" )
                    .setPositiveButton("OPEN SETTING", (dialog, which) -> {
                        // Action when "OK" is clicked
                        openOppoAutoStartSettings();
                        // Call the function to open settings or perform other actions
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // Action when "Cancel" is clicked
                        Toast.makeText(MainActivity.this, "Action Cancelled", Toast.LENGTH_SHORT).show();
                    })
                    .show();

        }else if (manufacturer.contains("samsung")) {
            //show the allert dialog before open setting to inform user
            new AlertDialog.Builder(this)
                    .setTitle( "Samsung Background Autostart")
                    .setMessage("Please Open 'Background AutoStart setting' settings to start app automatically after reboot and make app perfetctly work otherwis" +
                            " it may not be work perfectly.Find this " +
                            "app name 'Simmple Silent' in settings and enable it" )
                    .setPositiveButton("OPEN SETTING", (dialog, which) -> {
                        // Action when "OK" is clicked
                        openSamsungAutoStartSettings();
                        // Call the function to open settings or perform other actions
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // Action when "Cancel" is clicked
                        Toast.makeText(MainActivity.this, "Action Cancelled", Toast.LENGTH_SHORT).show();
                    })
                    .show();

        } else {
            // Show a generic message for unsupported manufacturers
            Toast.makeText(this, "Auto-start settings are not available for this device.", Toast.LENGTH_SHORT).show();
        }
    }



    // Xiaomi devices
    private void openXiaomiAutoStartSettings() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open auto-start settings for Xiaomi.", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Put data into SharedPreferences (key-value pair)
        editor.putBoolean("already allow background",true);

        // Commit the changes to save the data
        editor.apply();
    }

    // Huawei devices
    private void openHuaweiAutoStartSettings() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.optimize.process.ProtectActivity"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open auto-start settings for Huawei.", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Put data into SharedPreferences (key-value pair)
        editor.putBoolean("already allow background",true);

        // Commit the changes to save the data
        editor.apply();
    }

    // Oppo devices
    private void openOppoAutoStartSettings() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                "com.coloros.securitypermission",
                "com.coloros.securitypermission.ui.activity.AppListActivity"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open auto-start settings for Oppo.", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Put data into SharedPreferences (key-value pair)
        editor.putBoolean("already allow background",true);

        // Commit the changes to save the data
        editor.apply();
    }

    // Vivo devices
    private void openVivoAutoStartSettings() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                "com.iqoo.secure",
                "com.iqoo.secure.ui.phoneoptimize.addapps.AutoStartActivity"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open auto-start settings for Vivo.", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Put data into SharedPreferences (key-value pair)
        editor.putBoolean("already allow background",true);

        // Commit the changes to save the data
        editor.apply();
    }

    // Samsung devices
    private void openSamsungAutoStartSettings() {
        // Samsung uses Device Care
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                "com.samsung.android.lool",
                "com.samsung.android.sm.ui.battery.BatteryActivity"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open auto-start settings for Samsung.", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Put data into SharedPreferences (key-value pair)
        editor.putBoolean("already allow background",true);

        // Commit the changes to save the data
        editor.apply();
    }
}




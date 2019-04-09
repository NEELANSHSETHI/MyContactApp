package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissionAndLoadContacts();

    }

    public void createFragment(){
        Fragment contactFragment = new ContactList();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .replace(R.id.container, contactFragment) //<---replace a view in your layout (id: container) with the newFragment
                .commit();
    }

    private void checkPermissionAndLoadContacts(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,  Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(getApplicationContext(), "Contact Read permission denied! Please grant Contact Read permission from seting to access phone book", Toast.LENGTH_LONG).show();
                Log.d("PHONEBOOK_SAMPLE", "Contact Read permission denied! Please grant Contact Read permission from seting to access phone book");
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }else{
           createFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   createFragment();
                } else {
                    Toast.makeText(getApplicationContext(), "Contact Read permission not granted yet! Please grant Contact Read permission to access phone book", Toast.LENGTH_LONG).show();
                    Log.d("PHONEBOOK_SAMPLE", "Contact Read permission not granted yet! Please grant Contact Read permission to access phone book");
                }
                return;
            }
        }
    }
}

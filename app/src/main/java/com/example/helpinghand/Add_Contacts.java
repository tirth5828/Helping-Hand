package com.example.helpinghand;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class Add_Contacts extends AppCompatActivity {

    public static ArrayList contacts_numbers = new ArrayList(3);
    public static ArrayList contacts_names = new ArrayList(3);


    private static final String TAG = Add_Contacts.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    int number;

    TextView contact_name_1,contact_name_2,contact_name_3;
    TextView contact_number_1,contact_number_2,contact_number_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

    }

    public void Add_Contact_1(View view) {

        if(!hasPhoneContactsPermission())
        {
            requestPermission();
        }else {
            number = 1;
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
        }
    }

    public void Add_Contact_2(View view) {

        if(!hasPhoneContactsPermission())
        {
            requestPermission();
        }else {
            number = 2;
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
        }
    }

    public void Add_Contact_3(View view) {

        if(!hasPhoneContactsPermission())
        {
            requestPermission();
        }else {
            number = 3;
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName(number);
            retrieveContactNumber(number);
        }
    }

    private void retrieveContactNumber(int number) {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);

        if (number == 1) {
            contact_number_1 = findViewById(R.id.editText_Phone1);
            contact_number_1.setText(contactNumber);
        }
        else if (number == 2){
            contact_number_2 = findViewById(R.id.editText_Phone2);
            contact_number_2.setText(contactNumber);
        }

        else if (number == 3){
            contact_number_3 = findViewById(R.id.editText_Phone3);
            contact_number_3.setText(contactNumber);
        }
    }

    private void retrieveContactName(int number) {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

        if(number == 1)
        {
            contact_name_1 = findViewById(R.id.editText_PersonName1);
            contact_name_1.setText(contactName);
        }
        else if (number == 2){
            contact_name_2 = findViewById(R.id.editText_PersonName2);
            contact_name_2.setText(contactName);
        }

        else if (number == 3){
            contact_name_3 = findViewById(R.id.editText_PersonName3);
            contact_name_3.setText(contactName);
        }

    }

    private boolean hasPhoneContactsPermission() {

        boolean returnValue=false;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            int hasPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
            if(hasPermission==PackageManager.PERMISSION_GRANTED){
                returnValue=true;
            }
        }
        return returnValue;
    }

    private void requestPermission() {
        String[] requestPermissionArray = {Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this, requestPermissionArray, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int length = grantResults.length;
        if(length > 0)
        {
            int grantResult = grantResults[0];

            if(grantResult == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "You allowed permission, please click the button again.", Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(getApplicationContext(), "You denied permission.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Done(View view) throws IOException {

        

        contacts_numbers.clear();
        contacts_names.clear();
        contacts_names.add((String) contact_name_1.getText());
        contacts_names.add((String) contact_name_2.getText());
        contacts_names.add((String) contact_name_3.getText());

        contacts_numbers.add((String)contact_number_1.getText());
        contacts_numbers.add((String)contact_number_2.getText());
        contacts_numbers.add((String)contact_number_3.getText());


        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
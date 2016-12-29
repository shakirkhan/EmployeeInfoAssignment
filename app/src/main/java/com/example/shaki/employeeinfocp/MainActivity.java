package com.example.shaki.employeeinfocp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    EditText eID, eName, eDesignation, eCompany;
//    Button addButton; //, viewButton, updateButton, deleteButton, searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eID = (EditText) findViewById(R.id.editText_id);
        eName = (EditText) findViewById(R.id.editText_nam);
        eDesignation = (EditText) findViewById(R.id.editText_des);
        eCompany = (EditText) findViewById(R.id.editText_com);
//        EmployeeProvider.DatabaseHelper dbObject;
    }



    public void addEntry(View view) {
        String id = eID.getText().toString();
        String name = eName.getText().toString();
        String designation = eDesignation.getText().toString();
        String company = eCompany.getText().toString();

        ContentValues colValues = new ContentValues();
        colValues.put(EmployeeProvider.col_1, id);
        colValues.put(EmployeeProvider.col_2, name);
        colValues.put(EmployeeProvider.col_3, designation);
        colValues.put(EmployeeProvider.col_4, company);

        Uri uriA = getContentResolver().insert(EmployeeProvider.CONTENT_URL, colValues);
        if (uriA != null) {
            Toast.makeText(this, "Insert Successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Insert Failed", Toast.LENGTH_LONG).show();
        }
//        Toast.makeText(getBaseContext(), "Insert Successful", Toast.LENGTH_LONG).show();
    }



    public void deleteEntry(View view) {
        String id = eID.getText().toString();
        long isDeleted = getContentResolver().delete(EmployeeProvider.CONTENT_URL, " id = ?", new String[]{id});
        if (isDeleted > 0)
            Toast.makeText(MainActivity.this, "Entry Deleted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MainActivity.this, "Entry not Deleted", Toast.LENGTH_SHORT).show();
    }



    public void viewAll(View v) {
//        String[] projection = {"id", "name", "designation", "company"};
        Cursor all = getContentResolver().query(EmployeeProvider.CONTENT_URL, null, null, null, null); //null instead of "proejction"
        if (all != null && all.getCount() > 0) {
            StringBuffer infoBuffer = new StringBuffer();
            while (all.moveToNext()) {
                infoBuffer.append("ID: " + all.getString(0) + "\n");
                infoBuffer.append("Name: " + all.getString(1) + "\n");
                infoBuffer.append("Designation: " + all.getString(2) + "\n");
                infoBuffer.append("Company: " + all.getString(3) + "\n\n");
            }
            showMessage("All Data", infoBuffer.toString());
            all.close();
        } else {
            showMessage("Error", "Nothing Found");
        }
    }



    public void update(View v) {
        String id = eID.getText().toString();
        String name = eName.getText().toString();
        String designation = eDesignation.getText().toString();
        String company = eCompany.getText().toString();

        ContentValues colValues = new ContentValues();
        colValues.put(EmployeeProvider.col_1, id);
        colValues.put(EmployeeProvider.col_2, name);
        colValues.put(EmployeeProvider.col_3, designation);
        colValues.put(EmployeeProvider.col_4, company);

        int isUpdated = getContentResolver().update(EmployeeProvider.CONTENT_URL, colValues, " id=? ", new String[]{id});
        if (isUpdated > 0)
            Toast.makeText(MainActivity.this, "Entry Updated", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MainActivity.this, "Entry Not Updated", Toast.LENGTH_LONG).show();

    }



    public void search(View v) {
        String id = eID.getText().toString();
        String name = eName.getText().toString();
        String designation = eDesignation.getText().toString();
        String company = eCompany.getText().toString();

//        String[] projection = {"id", "name", "designation", "company"};
        Cursor search = getContentResolver().query(EmployeeProvider.CONTENT_URL, null, "id=? OR name=? OR designation=? OR company=?", new String[]{id, name, designation, company}, null);

        if (search != null && search.getCount() > 0) {
            search.moveToFirst();
            StringBuffer infoBuffer = new StringBuffer();
            while (!search.isAfterLast()) {
                infoBuffer.append("ID: " + search.getString(0) + "\n");
                infoBuffer.append("Name: " + search.getString(1) + "\n");
                infoBuffer.append("Designation: " + search.getString(2) + "\n");
                infoBuffer.append("Company: " + search.getString(3) + "\n\n");
//                                String id = search.getString(search.getColumnIndex(col_1));
//                                String name = search.getString(search.getColumnIndex(col_2));
//                                String designation = search.getString(search.getColumnIndex(col_3));
//                                String company = search.getString(search.getColumnIndex(col_4));
//                                infoBuffer.append("ID: " + id + "\n");
//                                infoBuffer.append("Name: " + name + "\n");
//                                infoBuffer.append("Designation: " + designation + "\n");
//                                infoBuffer.append("Company: " + company + "\n\n");
                search.moveToNext();
            }
            search.close();
            showMessage("Search Result", infoBuffer.toString());
        } else {
            showMessage("Error", "Invalid Search");
        }
    }



    private void showMessage(String text1, String text2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(text1);
        builder.setMessage(text2);
        builder.show();
    }


}



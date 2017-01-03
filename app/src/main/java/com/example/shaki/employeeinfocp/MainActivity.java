package com.example.shaki.employeeinfocp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    EditText eID, eName, eDesignation, eCompany, eSalary, eDoB;
//    Button addButton; //, viewButton, updateButton, deleteButton, searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eID = (EditText) findViewById(R.id.editText_id);
        eName = (EditText) findViewById(R.id.editText_nam);
        eDesignation = (EditText) findViewById(R.id.editText_des);
        eCompany = (EditText) findViewById(R.id.editText_com);
        eSalary = (EditText) findViewById(R.id.editText_salary);
        eDoB = (EditText) findViewById(R.id.editText_dob);
//        EmployeeProvider.DatabaseHelper dbObject;
    }



    public void addEntry(View view) {
        String id = eID.getText().toString();
        String name = eName.getText().toString();
        String designation = eDesignation.getText().toString();
        String company = eCompany.getText().toString();
        String salary = eSalary.getText().toString();
        String dob = eDoB.getText().toString();

        // for employee table
        ContentValues colValuesEM = new ContentValues();
        colValuesEM.put(EmployeeProvider.COL_ID, id);
        colValuesEM.put(EmployeeProvider.COL_NAME, name);
        colValuesEM.put(EmployeeProvider.COL_DESIGNATION, designation);
        colValuesEM.put(EmployeeProvider.COL_COMPANY, company);

        Uri uriAem = getContentResolver().insert(EmployeeProvider.CONTENT_URI_EMPLOYEE, colValuesEM);
        if (uriAem != null) {
            Toast.makeText(this, "Insert Successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Insert Failed", Toast.LENGTH_LONG).show();
        }
//        Toast.makeText(getBaseContext(), "Insert Successful", Toast.LENGTH_LONG).show();

        // for details table
        ContentValues colValuesDT = new ContentValues();
        colValuesDT.put(EmployeeProvider.COL_ID, id);
        colValuesDT.put(EmployeeProvider.COL_SALARY, salary);
        colValuesDT.put(EmployeeProvider.COL_DoB, dob);
        Uri uriAdt = getContentResolver().insert(EmployeeProvider.CONTENT_URI_DETAILS, colValuesDT);
        if (uriAdt != null) {
            Toast.makeText(this, "Insert Successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Insert Failed", Toast.LENGTH_LONG).show();
        }


    }



    public void deleteEntry(View view) {
        String id = eID.getText().toString();
        long isDeleted = getContentResolver().delete(EmployeeProvider.CONTENT_URI_EMPLOYEE, " id = ?", new String[]{id});
        if (isDeleted > 0)
            Toast.makeText(MainActivity.this, "Entry Deleted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MainActivity.this, "Entry not Deleted", Toast.LENGTH_SHORT).show();
    }



    public void viewAll(View v) {
//        String[] projection = {"id", "name", "designation", "company"};
        Cursor allEM = getContentResolver().query(EmployeeProvider.CONTENT_URI_EMPLOYEE, null, null, null, null); //null instead of "proejction"
        Cursor allDT = getContentResolver().query(EmployeeProvider.CONTENT_URI_DETAILS, null, null, null, null);
        if (allEM != null && allEM.getCount() > 0 & allDT != null && allDT.getCount() > 0) {
            StringBuffer infoBuffer = new StringBuffer();
            while (allEM.moveToNext() & allDT.moveToNext()) {
                infoBuffer.append("ID: " + allEM.getString(0) + "\n");
                infoBuffer.append("Name: " + allEM.getString(1) + "\n");
                infoBuffer.append("Designation: " + allEM.getString(2) + "\n");
                infoBuffer.append("Company: " + allEM.getString(3) + "\n\n");

                infoBuffer.append("Salary: " + allDT.getString(1) + "\n");
                infoBuffer.append("DoB: " + allDT.getString(2) + "\n");
            }
            showMessage("All Data", infoBuffer.toString());
            allEM.close();
            allDT.close();
        } else {
            showMessage("Error", "No Data Found");
        }
    }



    public void update(View v) {
        String id = eID.getText().toString();
        String name = eName.getText().toString();
        String designation = eDesignation.getText().toString();
        String company = eCompany.getText().toString();

        ContentValues colValues = new ContentValues();
        colValues.put(EmployeeProvider.COL_ID, id);
        colValues.put(EmployeeProvider.COL_NAME, name);
        colValues.put(EmployeeProvider.COL_DESIGNATION, designation);
        colValues.put(EmployeeProvider.COL_COMPANY, company);

        int isUpdated = getContentResolver().update(EmployeeProvider.CONTENT_URI_EMPLOYEE, colValues, " id=? ", new String[]{id});
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
        Cursor search = getContentResolver().query(EmployeeProvider.CONTENT_URI_EMPLOYEE, null, "id=? OR name=? OR designation=? OR company=?", new String[]{id, name, designation, company}, null);

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



package com.example.studentcrimelab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    CrimeLab mCrimeLab;
    Crime mCurrentCrime;

    TextView tvUUID;
    EditText etCrimeTitle;
    Button bCrimeDate;
    CheckBox cbCrimeSolved;

    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crime_activity);

        mCrimeLab = CrimeLab.get(this);
        Intent intent = getIntent();

        UUID CrimeID = UUID.fromString(getIntent().getStringExtra("CrimeID"));
        mCurrentCrime = mCrimeLab.getCrime(CrimeID);

        tvUUID = findViewById(R.id.tvUUID);
        tvUUID.setText(CrimeID.toString());

        etCrimeTitle = findViewById(R.id.etCrimeTitle);
        etCrimeTitle.setText(mCurrentCrime.getTitle());

        bCrimeDate = findViewById(R.id.bCrimeDate);
        bCrimeDate.setText(mCurrentCrime.getDate().toString());

        cbCrimeSolved = findViewById(R.id.cbCrimeSolved);
        cbCrimeSolved.setChecked(mCurrentCrime.isSolved());
    }
    
    @Override
    public void onPause()
    {
        super.onPause();

        // Update title.
        etCrimeTitle = findViewById(R.id.etCrimeTitle);
        if (etCrimeTitle.getText().toString() != mCurrentCrime.getTitle())
        {
            mCurrentCrime.seTitle(etCrimeTitle.getText().toString());
        }
    }

    public void buttonCrimeDateClicked(View view)
    {
        System.out.println("Date button clicked.");

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CrimeActivity.this, CrimeActivity.this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(CrimeActivity.this, CrimeActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinutes = minute;

        Date newDate = new Date(mYear - 1900, mMonth, mDay, mHour, mMinutes);
        mCurrentCrime.setDate(newDate);

        bCrimeDate.setText(newDate.toString());
    }

    public void checkboxCrimeSolvedClicked(View view)
    {
        System.out.println("Checkbox solved clicked.");

        boolean solved = cbCrimeSolved.isChecked();
        mCurrentCrime.setSolved(solved);
    }

    public void buttonCrimeAddClicked(View view)
    {
        System.out.println("Crime Add button clicked.");
        Crime newCrime = new Crime();
        newCrime.seTitle("Crime #" + mCrimeLab.getCrimes().size());
        newCrime.setSolved(false);
        mCrimeLab.addCrime(newCrime);
        finish();
    }

    public void buttonCrimeDeleteClicked(View view)
    {
        System.out.println("Crime Delete button clicked.");
        mCrimeLab.deleteCrime(mCurrentCrime.getId());
        finish();
    }
}

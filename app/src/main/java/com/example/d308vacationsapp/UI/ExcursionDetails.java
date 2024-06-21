package com.example.d308vacationsapp.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationsapp.R;
import com.example.d308vacationsapp.database.Repository;
import com.example.d308vacationsapp.entities.Excursion;
import com.example.d308vacationsapp.entities.Vacation;
import com.example.d308vacationsapp.UI.VacationList;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    String name;
    Double price;
    int excursionID;
    int vacID;
    EditText editName;
    EditText editPrice;
    EditText editNote;
    TextView editDate;
    Repository repository;
    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    private static final String DATE_FORMAT = "MM/dd/yy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        repository=new Repository(getApplication());
        name = getIntent().getStringExtra("name");
        editName = findViewById(R.id.excursionName);
        editName.setText(name);
        price = getIntent().getDoubleExtra("price", 0.0);
        editPrice = findViewById(R.id.excursionPrice);
        editPrice.setText(Double.toString(price));
        excursionID = getIntent().getIntExtra("id", -1);
        vacID = getIntent().getIntExtra("vacID", -1);
        editNote=findViewById(R.id.note);
        editDate=findViewById(R.id.date);
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Spinner spinner=findViewById(R.id.spinner);
        ArrayList<Vacation> vacationArrayList= new ArrayList<>();
        vacationArrayList.addAll(repository.getmAllVacations());
        ArrayAdapter<Vacation> vacationAdapter= new ArrayAdapter<Vacation>(this, android.R.layout.simple_spinner_item,vacationArrayList);
        spinner.setAdapter(vacationAdapter);
        spinner.setSelection(0);

        startDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }

        };



        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date date;
                //get value from other screen,but I'm going to hard code it right now
                String info=editDate.getText().toString();
                if (editDate.getText().toString().isEmpty()) {
                    myCalendarStart.setTimeInMillis(System.currentTimeMillis());
                }
                try{
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        // return true;
//                Intent intent=new Intent(PartDetails.this,MainActivity.class);
//                startActivity(intent);
//                return true;

        //e.  Include validation that the excursion date is during the associated vacation.
        if (item.getItemId() == R.id.excursionsave) {
            // HIGHLIGHTED CHANGE: Add validation before saving
            if (validateExcursionDate()) {
                Excursion excursion;
                if (excursionID == -1) {
                    if (repository.getAllExcursions().size() == 0)
                        excursionID = 1;
                    else
                        excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionId() + 1;
                    excursion = new Excursion(excursionID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()), vacID);

                    repository.insert(excursion);
                } else {
                    excursion = new Excursion(excursionID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()), vacID);
                    repository.update(excursion);
                    this.finish();
                }
                // Navigate back to VacationListActivity
                Intent intent = new Intent(this, VacationList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
                startActivity(intent);
                finish();

                return true;
            } else {
                Toast.makeText(this, "Excursion date must be within the vacation period.", Toast.LENGTH_SHORT).show();

            }
        }




        /*
        if (item.getItemId() == R.id.excursionsave) {
            Excursion excursion;
            if (excursionID == -1) {
                if (repository.getAllExcursions().size() == 0)
                    excursionID = 1;
                else
                    excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionId() + 1;
                excursion = new Excursion(excursionID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()), vacID);

                repository.insert(excursion);
            } else {
                excursion = new Excursion(excursionID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()), vacID);
                repository.update(excursion);
            }

            return true;
        }
        */

        //Delete Excursion
        if (item.getItemId() == R.id.deleteexcursion) {
            Excursion excursion;
            if (excursionID != -1) {
                // Create a new Excursion object with the ID to delete
                Excursion excursionToDelete = new Excursion(excursionID, "", 0.0, vacID);
                repository.delete(excursionToDelete);
                finish(); // Optionally, navigate back or do any other necessary UI update
            }
            return true;
        }

        
        // Uncomment this if it doesn't work!!!!
        if(item.getItemId()== R.id.notify) {
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try{
                Long trigger = myDate.getTime();
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                intent.putExtra("key", "Reminder: " + editName.getText().toString() + " is today!");
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);}
            catch (Exception e){

            }
            return true;
        }

        return super.onOptionsItemSelected(item);

         /*
        if (item.getItemId() == R.id.notify) {
            String dateFromScreen = editDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (myDate != null) {
                Long trigger = myDate.getTime();
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                intent.putExtra("key", "Reminder: " + editName.getText().toString() + " is today!");
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                    Toast.makeText(this, "Notification set for " + dateFromScreen, Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
*/
        //return super.onOptionsItemSelected(item);

        /*
        RecyclerView recyclerView=findViewById(R.id.vacationrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        excursionAdapter.setExcursions(repository.getAllExcursions()); **/


    }
    // Testing Part B3e remove if doesn't work!!
    private Date getVacationStartDate(Vacation vacation) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            return sdf.parse(vacation.getStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date getVacationEndDate(Vacation vacation) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            return sdf.parse(vacation.getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    // HIGHLIGHTED CHANGE: Validation method to ensure excursion date is within vacation period
    private boolean validateExcursionDate() {
        String DATE_FORMAT = "MM/dd/yy"; // Ensure DATE_FORMAT is defined
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);

        Date excursionDate = null;
        try {
            String excursionDateString = editDate.getText().toString();
            excursionDate = sdf.parse(excursionDateString);
            Log.d("Debug", "Parsed Excursion Date: " + excursionDateString + " -> " + excursionDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Debug", "Failed to parse excursion date");
            return false;
        }

        Vacation vacation = repository.getVacationById(vacID);
        Date vacationStartDate = getVacationStartDate(vacation);
        Date vacationEndDate = getVacationEndDate(vacation);

        Log.d("Debug", "Vacation Start Date: " + vacationStartDate);
        Log.d("Debug", "Vacation End Date: " + vacationEndDate);

        if (excursionDate == null || vacationStartDate == null || vacationEndDate == null) {
            Log.d("Debug", "One or more dates are null");
            return false;
        }

        // Check if the excursion date is within the vacation period
        boolean isWithinRange = !excursionDate.before(vacationStartDate) && !excursionDate.after(vacationEndDate);
        Log.d("Debug", "Excursion Date is within range: " + isWithinRange);

        return isWithinRange;
    }
}
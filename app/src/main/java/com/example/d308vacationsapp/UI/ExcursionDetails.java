package com.example.d308vacationsapp.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        repository=new Repository(getApplication());
        name = getIntent().getStringExtra("name");
        editName = findViewById(R.id.excursionName);
        editName.setText(name);
        price = getIntent().getDoubleExtra("price", -1.0);
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


        //ArrayList<Integer> vacationIdList= new ArrayList<>();



        Spinner spinner=findViewById(R.id.spinner);
        ArrayList<Vacation> vacationArrayList= new ArrayList<>();
        vacationArrayList.addAll(repository.getmAllVacations());
        ArrayAdapter<Vacation> vacationAdapter= new ArrayAdapter<Vacation>(this, android.R.layout.simple_spinner_item,vacationArrayList);
        spinner.setAdapter(vacationAdapter);
        spinner.setSelection(0);

        /*
        for(Vacation vacation:vacationArrayList){
            vacationIdList.add(vacation.getVacationId());
        }
        */
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
                if(info.equals(""))info="06/13/24";
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

        if (item.getItemId()== android.R.id.home){
            this.finish();
            return true;}
        // return true;
//                Intent intent=new Intent(PartDetails.this,MainActivity.class);
//                startActivity(intent);
//                return true;

        if (item.getItemId()== R.id.excursionsave){
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
            return true;}
        if (item.getItemId()== R.id.share) {
            // Share all vacation details via Intent
            String vacationDetails = "Vacation Name: " + editName.getText().toString() + "\n" +
                                    "Vacation Price: $" + editPrice.getText().toString() + "\n" +
                                    "Excursion Name: " + editName.getText().toString() + "\n" +
                                    "Excursion Price: $" + editPrice.getText().toString() + "\n" +
                                    "Excursion Date: " + editDate.getText().toString() + "\n" +
                                    "Notes: " + editNote.getText().toString();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, vacationDetails);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Vacation Details");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
            
            /*
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString());
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Message Title");
            sendIntent.setType("text/plain");
            
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
            */
        }
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
                intent.putExtra("key", "message I want to see");
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);}
            catch (Exception e){

            }
            return true;
        }

        return super.onOptionsItemSelected(item);


        /*
        RecyclerView recyclerView=findViewById(R.id.vacationrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        excursionAdapter.setExcursions(repository.getAllExcursions()); **/
    }
}
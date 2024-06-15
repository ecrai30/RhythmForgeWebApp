package com.example.d308vacationsapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationDetails extends AppCompatActivity {
    String name;
    double price;
    String hotel;
    int vacationID;
    String startDate;
    String endDate;

    EditText editName;
    EditText editPrice;
    EditText editHotel;
    EditText editStartDate;
    EditText editEndDate;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab=findViewById(R.id.floatingActionButton2);

        vacationID = getIntent().getIntExtra("id",-1);
        editName=findViewById(R.id.vacationname);
        editPrice=findViewById(R.id.vacationprice);
        editHotel=findViewById(R.id.hotel);
        editStartDate=findViewById(R.id.startdate);
        editEndDate=findViewById(R.id.enddate);

        name = getIntent().getStringExtra("name");
        price = getIntent().getDoubleExtra("price",0.0);
        hotel = getIntent().getStringExtra("hotel");
        startDate=getIntent().getStringExtra("startDate");
        endDate=getIntent().getStringExtra("endDate");
        editName.setText(name);
        editPrice.setText(Double.toString(price));
        //edit fields
        editHotel.setText(hotel);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacID", vacationID);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationId() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacationdetails,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()== android.R.id.home){
            this.finish();
            return true;
        }

        if(item.getItemId()==R.id.vacationsave){
            Vacation vacation;
            if(vacationID==-1){
                if(repository.getmAllVacations().size()==0) vacationID=1;
                else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() -1).getVacationId() +1;
                vacation = new Vacation(vacationID,editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()),editHotel.getText().toString(), editStartDate.getText().toString(),editEndDate.getText().toString());
                repository.insert(vacation);
                this.finish();
            }
            else{
                vacation = new Vacation(vacationID,editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()),editHotel.getText().toString(),editStartDate.getText().toString(),editEndDate.getText().toString());
                repository.update(vacation);
                this.finish();
            }
        }
        if(item.getItemId()==R.id.vacationdelete) {
            for (Vacation vac : repository.getmAllVacations()) {
                if (vac.getVacationId() == vacationID) currentVacation = vac;

            }
            numExcursions=0;
            for(Excursion excursion: repository.getAllExcursions()){
                if(excursion.getVacationId()==vacationID)++numExcursions;
            }
            if(numExcursions==0){
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this,currentVacation.getVacationName() + " was deleted", Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
            }
            else{
                Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions.", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }
}